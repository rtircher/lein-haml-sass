(ns leiningen.lein-common.lein-utils
  (:import [org.jruby.embed ScriptingContainer LocalContextScope]))

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
  (let [container (ScriptingContainer. LocalContextScope/THREADSAFE)]
    (println (str "Installing: " gem-name "-" gem-version))
    (.runScriptlet container (str "require 'rubygems'
require 'rubygems/dependency_installer'
Gem::DependencyInstaller.new(:install_dir => 'lib/gems').install('" gem-name "', '" gem-version "')"))))
