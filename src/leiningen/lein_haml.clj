(ns leiningen.lein-haml
  (:import [org.jruby.embed ScriptingContainer LocalContextScope]))

(def ^:private c (ScriptingContainer. LocalContextScope/THREADSAFE))
(. c runScriptlet "require 'rubygems'; require 'haml'")

(def ^:private engineclass (. c runScriptlet "Haml::Engine"))

(defn render [template]
  (let [engine (. c callMethod engineclass "new" template Object)]
    (. c callMethod engine "render" String)))
