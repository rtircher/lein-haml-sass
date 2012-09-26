(ns leiningen.lein-haml-sass.render-engine
  (:use leiningen.lein-common.file-utils)
  (:require [clojure.java.io :as io])
  (:import [org.jruby.embed ScriptingContainer LocalContextScope]))

(def ^:private c (ref nil))
(def ^:private haml-engine (ref nil))
(def ^:private sass-engine (ref nil))

(defn- ensure-engine-started! []
  (when-not @c
    (dosync
     (ref-set c (ScriptingContainer. LocalContextScope/THREADSAFE))

     (def gempath ["gems/gems/haml-3.1.7/lib"])
     (.setLoadPaths @c gempath)
     (.runScriptlet @c "require 'rubygems'; require 'haml'; require 'sass'")
     (ref-set haml-engine (.runScriptlet @c "Haml::Engine"))
     (ref-set sass-engine (.runScriptlet @c "Sass::Engine")))))

(defn render [template engine-class]
  (let [engine (.callMethod @c @engine-class "new" template Object)]
    (.callMethod @c engine "render" String)))

(defn render-all! [src-type engine-class src-dest-map auto-compile-delay watch?]
  (ensure-engine-started!)
  (loop []
    (doseq [file-descriptor src-dest-map]
      (let [dest-file (io/file (:dest file-descriptor))
            src-file (io/file (src-type file-descriptor))]
        (when (or (not (.exists dest-file))
                  (> (.lastModified src-file) (.lastModified dest-file)))
          (io/make-parents dest-file)
          (spit dest-file (render (slurp (src-type file-descriptor)) engine-class))
          (println (str "   [" (name src-type) "] - " (java.util.Date.) " - " src-file " -> " dest-file)))))

    (when watch?
      (Thread/sleep auto-compile-delay)
      (recur))))

(def render-all-haml! (partial render-all! :haml haml-engine))
(def render-sass-haml! (partial render-all! :sass sass-engine))

(defn clean-all! [src-dest-map output-directory delete-output-dir]
  (doseq [haml-descriptor src-dest-map]
    (delete-file! (io/file (:dest haml-descriptor))))
  (when (and delete-output-dir (exists output-directory) (dir-empty? output-directory))
    (println (str "Destination folder " output-directory " is empty - Deleting it"))
    (delete-directory-recursively! output-directory)))
