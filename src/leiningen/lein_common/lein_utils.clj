(ns leiningen.lein-common.lein-utils
  (:use [clojure.java.shell :only [sh]]))

(def ^:private lein2?
  (try
    (require 'leiningen.core.main)
    true
    (catch java.io.FileNotFoundException _
      false)))

(defn exit-success
  "Exit successfully in a way that satisifies lein1 and lein2."
  []
  (when-not lein2? 0))

(defn exit-failure
  "Fail in a way that satisifies lein1 and lein2."
  [error-msg]
  (if lein2?
    ((resolve 'leiningen.core.main/abort) error-msg)
    (do
      (println error-msg)
      1)))

(defn task-not-found [subtask]
  (exit-failure (str "Subtask \"" subtask "\" not found.")))

(defn install-gem! [{:keys [gem-name gem-version]}]
  (let [gem-result (sh "java" "-jar" "lib/dev/jruby-complete-1.6.4.jar" "-S" "gem" "install" "-i" "lib/" gem-name "-v" gem-version "--no-rdoc" "--no-ri")]
    (println (str "Installing: " gem-name "-" gem-version))
    (if (= 0 (:exit gem-result))
      (println (:out gem-result))
      (exit-failure (:out gem-result)))))
