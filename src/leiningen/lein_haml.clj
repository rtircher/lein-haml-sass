(ns leiningen.lein-haml
  (:use leiningen.file-utils)
  (:require [clojure.java.io :as io])
  (:import [org.jruby.embed ScriptingContainer LocalContextScope]))

(def ^:private c (ScriptingContainer. LocalContextScope/THREADSAFE))
(.runScriptlet c "require 'rubygems'; require 'haml'")

(def ^:private engineclass (.runScriptlet c "Haml::Engine"))

(defn- save-html-to [file]
  )

(defn render [template]
  (let [engine (.callMethod c engineclass "new" template Object)]
    (.callMethod c engine "render" String)))

(defn render-all! []
  (doseq [haml-descriptor (haml-dest-files-from "spec/files/multiple" {:dest "spec/files/out"})]
    (let [dest-file (io/file (:dest haml-descriptor))
          haml-file (io/file (:haml haml-descriptor))]
      (when (or (not (.exists dest-file))
                 (> (.lastModified haml-file) (.lastModified dest-file)))
        (io/make-parents dest-file)
        (spit dest-file (render (slurp (:haml haml-descriptor))))
        (println (str "-> " haml-file " compiled to " dest-file))))))

(defn -main [& args]
  (render-all!))
