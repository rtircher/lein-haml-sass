(defproject lein-haml "1.0.0-SNAPSHOT"
  :description "HAML autobuilder plugin"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [org.jruby/jruby-complete "1.6.4"]]

  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}

  :main lein-haml.core

  :extra-classpath-dirs ["gems"])
