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
    (let [dest (:dest haml-descriptor)]
      (io/make-parents dest)
      (spit (io/file dest) (render (slurp (:haml haml-descriptor)))))))

(defn -main [& args]
  (render-all!))