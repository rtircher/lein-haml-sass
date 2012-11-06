(ns leiningen.sass
  (:require [leiningen.tasks   :as tasks]
            [leiningen.help    :as lhelp]
            [leiningen.clean   :as lclean]
            [leiningen.compile :as lcompile]
            [leiningen.deps    :as ldeps]
            [robert.hooke      :as hooke]))

(tasks/def-once :sass)
(tasks/def-auto :sass)
(tasks/def-clean :sass)
(tasks/def-deps "sass")

(tasks/def-lein-task :sass)

(defn- compile-hook [task & args]
  (tasks/def-hook :sass task :once args))

(defn- clean-hook [task & args]
  (tasks/def-hook :sass task :clean args))

(defn- deps-hook [task & args]
  (tasks/def-hook :sass task :deps args))

(hooke/add-hook #'lcompile/compile #'compile-hook)
(hooke/add-hook #'lclean/clean #'clean-hook)
(hooke/add-hook #'ldeps/deps   #'deps-hook)
