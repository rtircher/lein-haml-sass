(ns leiningen.lein-haml
  (:import [org.jruby.embed ScriptingContainer LocalContextScope]))

(def ^:private c (ScriptingContainer. LocalContextScope/THREADSAFE))
(.runScriptlet c "require 'rubygems'; require 'haml'")

(def ^:private engineclass (.runScriptlet c "Haml::Engine"))

(defn render [template]
  (let [engine (.callMethod c engineclass "new" template Object)]
    (.callMethod c engine "render" String)))
