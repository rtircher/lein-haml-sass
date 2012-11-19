(ns leiningen.sass
  (:use [leiningen.lein-common.lein-utils :only [lein2?]])
  (:require [leiningen.tasks   :as tasks]
            [leiningen.help    :as lhelp]
            [leiningen.clean   :as lclean]
            [leiningen.compile :as lcompile]
            [leiningen.deps    :as ldeps]
            [robert.hooke      :as hooke]))

;; TODO remove ref to sass here
(tasks/def-lein-task sass "sass")

(tasks/def-hook compile-hook :sass :once)
(tasks/def-hook clean-hook   :sass :clean)
(tasks/def-hook deps-hook    :sass :deps)

(defn activate
  "Set up hooks for the plugin.  Eventually, this can be changed to just hooks,
   and people won't have to specify :hooks in their project.clj files anymore."
  []
  (hooke/add-hook #'lcompile/compile #'compile-hook)
  (hooke/add-hook #'lclean/clean #'clean-hook)
  (hooke/add-hook #'ldeps/deps   #'deps-hook))

; Lein1 hooks have to be called manually, in lein2 the activate function will
; be automatically called.
(when-not lein2? (activate))
