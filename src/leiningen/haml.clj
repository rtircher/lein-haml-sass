(ns leiningen.haml
  (:use leiningen.lein-haml.file-utils)
  (:require [clojure.java.io :as io]
            [leiningen.help :as lhelp]
            ;;[leiningen.core.main :as main]
            )
  (:import [org.jruby.embed ScriptingContainer LocalContextScope]))

(def ^:dynamic *auto-compile-delay* 250)

(def c (ScriptingContainer. LocalContextScope/THREADSAFE))
(.runScriptlet c "require 'rubygems'; require 'haml'")

(def ^:private engineclass (.runScriptlet c "Haml::Engine"))

(defn- render [template]
  (let [engine (.callMethod c engineclass "new" template Object)]
    (.callMethod c engine "render" String)))


(defn- render-all! [haml-dir dest-dir dest-extension watch?]
  (println (java.util.Date.))
  (println "------------------------------------------------")
  (println "Ready to compile haml")

  (loop []
    (doseq [haml-descriptor (haml-dest-files-from haml-dir {:dest dest-dir :ext dest-extension})]
      (let [dest-file (io/file (:dest haml-descriptor))
            haml-file (io/file (:haml haml-descriptor))]
        (when (or (not (.exists dest-file))
                  (> (.lastModified haml-file) (.lastModified dest-file)))
          (io/make-parents dest-file)
          (spit dest-file (render (slurp (:haml haml-descriptor))))
          (println (str "    [haml] " haml-file " -> " dest-file )))))

    (when watch?
      (Thread/sleep *auto-compile-delay*)
      (recur))))


(defn- normalize-options [options]
  (merge {:haml-src "resources/haml"
          :output-extension "html"}
         options))

(defn- extract-options [project]
  (when (nil? (:haml project))
    (println "WARNING: no :haml entry found in project definition."))
  (normalize-options (:haml project)))

(defn- once [{:keys [haml-src output-directory output-extension]}]
  (render-all! haml-src output-directory output-extension false))

(defn- auto [{:keys [haml-src output-directory output-extension auto-compile-delay]}]
  (binding [*auto-compile-delay* (or auto-compile-delay *auto-compile-delay*)]
    (render-all! haml-src output-directory output-extension true)))

(defn- clean [options])

(defn- task-not-found [task]
  ;; (main/abort (str "Subtask \"" subtask "\" not found."))
  )

;; Leiningen task
(defn haml
  "Runs the haml compiler plugin."
  {:help-arglists '([once auto clean])
   :subtasks [#'once #'auto #'clean]}
  ([project]
    (println
      (lhelp/help-for "haml"))
    ;; (exit-failure)
    )

  ([project subtask & args]
     (let [options (extract-options project)]
       (case subtask
         "once"  (once options)
         "auto"  (auto options)
         "clean" (clean options)
         (task-not-found subtask)))))


;; do (main/abort errors) on exception
