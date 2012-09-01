(ns lein-haml.core
  (:use leiningen.lein-haml
        clojure.contrib.classpath))

(def template
"%html
  %head
    %title
      Hello Clojure!
  %body
    %h2
      Hello Clojure from Haml!")


(defn -main [& args]
  (println (render template)))
