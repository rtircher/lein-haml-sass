(ns leiningen.haml
  (:use leiningen.lein-haml.file-utils)
  (:require [clojure.java.io :as io]
            ;;[leiningen.core.main :as main]
            )
  (:import [org.jruby.embed ScriptingContainer LocalContextScope]))

(def c (ScriptingContainer. LocalContextScope/THREADSAFE))
(.runScriptlet c "require 'rubygems'; require 'haml'")

(def ^:private engineclass (.runScriptlet c "Haml::Engine"))

(defn- render [template]
  (let [engine (.callMethod c engineclass "new" template Object)]
    (.callMethod c engine "render" String)))

(defn- render-all! [haml-dir dest-dir dest-extension]
  (println (java.util.Date.))
  (println "Compiling haml")
  (doseq [haml-descriptor (haml-dest-files-from haml-dir {:dest dest-dir :ext dest-extension})]
    (let [dest-file (io/file (:dest haml-descriptor))
          haml-file (io/file (:haml haml-descriptor))]
      (when (or (not (.exists dest-file))
                 (> (.lastModified haml-file) (.lastModified dest-file)))
        (io/make-parents dest-file)
        (spit dest-file (render (slurp (:haml haml-descriptor))))
        (println (str "  " haml-file " -> " dest-file)))))
  (println "----------\n"))

(defn- normalize-options [options]
  (merge {:haml-src "resources/haml"
          :output-extension "html"
          :output-directory nil}
         options))

(defn- extract-options [project]
  (when (nil? (:haml project))
    (println "WARNING: no :haml entry found in project definition."))
  (normalize-options (:haml project)))

(defn- once [options]
  (render-all! (:haml-src options) (:output-directory options) (:output-extension options)))

(defn- auto [options])
(defn- clean [options])

(defn- task-not-found [task]
  ;; (main/abort (str "Subtask \"" subtask "\" not found."))
  )

;; Leiningen task
(defn haml [project subtask & args]
  (let [options (extract-options project)]
    (case subtask
      "once"  (once options)
      "auto"  (auto options)
      "clean" (clean options)
      (task-not-found subtask))))

(defn -main [& args]
  (render-all! "spec/files/multiple" "spec/files/out" nil))



;; do (main/abort errors) on exception