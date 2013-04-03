(ns leiningen.lein-haml-sass.render-engine
  (:use leiningen.lein-common.file-utils)
  (:require [clojure.java.io :as io])
  (:import [org.jruby.embed ScriptingContainer LocalContextScope]
           [org.jruby RubyHash RubySymbol RubyArray]))

(def ^:private c (ref nil))
(def ^:private runtime (ref nil))

(def ^:private rendering-engine (ref nil))

(def ^:private empty-options (ref nil))
(def ^:private sass-options (ref nil))
(def ^:private scss-options (ref nil))

(defn- rb-array [coll]
  (let [array (RubyArray/newArray @runtime)]
    (doseq [v coll] (.add array v))
    array))

(defn- rb-symbol [string]
  (RubySymbol/newSymbol @runtime (name string)))

(defn- rb-options [options]
  (let [rb-hash (RubyHash. @runtime)]
    (doseq [[k v] options]
      (let [key (rb-symbol k)
            value (if (coll? v) (rb-array v) (rb-symbol v))]
        (.put rb-hash key value)))
    rb-hash))

(defn- build-sass-options [src-type {:keys [src output-directory style]}]
  (rb-options {:syntax src-type
               :style  (or style :nested)
               :load_paths [src output-directory]}))

(defn- require-gem [gem-name]
  (.runScriptlet @c (str "require 'rubygems'; require '" (name gem-name) "';")))

;; TODO improve this function (this is messy)
(defn- ensure-engine-started! [options]
  (when-not @c
    (dosync
     (ref-set c (ScriptingContainer. LocalContextScope/THREADSAFE))

     (require-gem (:gem-name options))
     (ref-set runtime (-> (.getProvider @c) .getRuntime))

     (if (= (:gem-name options) "haml")
       (do
         (ref-set rendering-engine (.runScriptlet @c "Haml::Engine"))
         (ref-set empty-options (RubyHash. @runtime)))
       (do
         (ref-set rendering-engine (.runScriptlet @c "Sass::Engine"))
         (ref-set sass-options (build-sass-options :sass options))
         (ref-set scss-options (build-sass-options :scss options)))))))

(defn- engine-options-for [src-type]
  (case src-type
    :sass @sass-options
    :scss @scss-options
    @empty-options))

(defn- files-from [{:keys [src src-type output-directory output-extension]}]
  (dest-files-from (name src-type) src output-directory output-extension))

(defn render [src-type template]
  (try
    (let [args         (to-array [template (engine-options-for src-type)])
          engine       (.callMethod @c @rendering-engine "new" args Object)]
      (.callMethod @c engine "render" String))
    (catch Exception e
      ;; ruby gem will print an error message
      (println "      -> Compilation failed\n\n" ))))

(defn render-all!
  ([options watch?] (render-all! options watch? false))

  ([{:keys [src-type auto-compile-delay] :as options} watch? force?]
     (ensure-engine-started! options)
     (loop []
       (doseq [file-descriptor (files-from options)]
         (let [dest-file (io/file (:dest file-descriptor))
               src-file (io/file (src-type file-descriptor))]
           (when (or force?
                     (not (.exists dest-file))
                     (> (.lastModified src-file) (.lastModified dest-file)))
             (println (str "   [" (name src-type) "] - " (java.util.Date.) " - " src-file " -> " dest-file))
             (io/make-parents dest-file)
             (spit dest-file (render src-type (slurp (src-type file-descriptor)))))))

       (when watch?
         (Thread/sleep auto-compile-delay)
         (recur)))))

(defn clean-all! [{:keys [output-directory delete-output-dir] :as options}]
  (doseq [file-descriptor (files-from options)]
    (delete-file! (io/file (:dest file-descriptor))))

  (when (and delete-output-dir (exists output-directory) (dir-empty? output-directory))
    (println (str "Destination folder " output-directory " is empty - Deleting it"))
    (delete-directory-recursively! output-directory)))
