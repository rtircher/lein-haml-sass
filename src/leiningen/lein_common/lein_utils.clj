(ns leiningen.lein-common.lein-utils
  (:import [org.jruby.embed ScriptingContainer LocalContextScope]))

(def lein2?
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
  ([] (exit-failure ""))
  ([error-msg]
     (if lein2?
       ((resolve 'leiningen.core.main/abort) error-msg)
       (do
         (println error-msg)
         1))))

(defn task-not-found [subtask]
  (exit-failure (str "Subtask \"" subtask "\" not found.")))

(defn install-gem! [{:keys [gem-name gem-version]}]
  (when gem-version
    (let [container (ScriptingContainer. LocalContextScope/THREADSAFE)]
      (.setLoadPaths container [(str "lib/gems/gems/" gem-name "-" gem-version "/lib")])
      (.runScriptlet container (str "
require 'rubygems'
require 'rubygems/dependency_installer'

begin
  Gem::Specification.find_by_name('" gem-name "', '" gem-version "')
rescue Gem::LoadError
  puts 'Installing: " gem-name "-" gem-version "'
  Gem::DependencyInstaller.new(:install_dir => 'lib/gems').install('" gem-name "', '" gem-version "')
end
")))))
