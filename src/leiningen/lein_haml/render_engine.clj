(ns leiningen.lein-haml.render-engine
  (:use leiningen.lein-common.file-utils)
  (:require [clojure.java.io :as io])
  (:import [org.jruby.embed ScriptingContainer LocalContextScope]))

(def ^:private c (ref nil))
(def ^:private engineclass (ref nil))

(defn- ensure-engine-started! []
  (when-not @c
    (dosync
     (ref-set c (ScriptingContainer. LocalContextScope/THREADSAFE))

     (def gempath ["gems/gems/haml-3.1.7/lib"])
     (.setLoadPaths @c gempath)
     (.runScriptlet @c "require 'rubygems'; require 'haml'")
     (ref-set engineclass (.runScriptlet @c "Haml::Engine")))))

(defn- render [template]
  (let [engine (.callMethod @c @engineclass "new" template Object)]
    (.callMethod @c engine "render" String)))

(defn render-all! [{:keys [haml-src output-directory output-extension auto-compile-delay]} watch?]
  (ensure-engine-started!)
  (loop []
    (doseq [haml-descriptor (haml-dest-files-from haml-src output-directory output-extension)]
      (let [dest-file (io/file (:dest haml-descriptor))
            haml-file (io/file (:haml haml-descriptor))]
        (when (or (not (.exists dest-file))
                  (> (.lastModified haml-file) (.lastModified dest-file)))
          (io/make-parents dest-file)
          (spit dest-file (render (slurp (:haml haml-descriptor))))
          (println (str "   [haml] - " (java.util.Date.) " - " haml-file " -> " dest-file)))))

    (when watch?
      (Thread/sleep auto-compile-delay)
      (recur))))
