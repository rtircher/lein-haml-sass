(ns leiningen.lein-haml
  (:use clojure.contrib.io)
  (:use clojure.contrib.classpath))

(import '(org.jruby.embed ScriptingContainer LocalContextScope))
(def c (ScriptingContainer. LocalContextScope/THREADSAFE))

(println (classpath))
(println (pwd))

;; Using $LOAD_PATH to load haml gem
;(def gempath [(str (pwd) "/src/haml-3.1.2/gem")])
;(. c setLoadPaths gempath)
;(. c runScriptlet "require 'rubygems'; require 'haml'")

;; Using classpath to load haml gem
(. c runScriptlet "require 'rubygems'; require 'haml'")

(def engineclass (. c runScriptlet "Haml::Engine"))

(defn render [template]
  (let [engine (. c callMethod engineclass "new" template Object)]
    (. c callMethod engine "render" String)))
