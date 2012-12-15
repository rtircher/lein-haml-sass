(ns leiningen.scss
  (:use [leiningen.lein-common.lein-utils :only [lein2?]])
  (:require [leiningen.tasks   :as tasks]
            [leiningen.help    :as lhelp]
            [leiningen.clean   :as lclean]
            [leiningen.compile :as lcompile]
            [robert.hooke      :as hooke]))

(tasks/def-lein-task scss)

(defn activate
  "Set up hooks for the plugin.  Eventually, this can be changed to just hooks,
   and people won't have to specify :hooks in their project.clj files anymore."
  []
  (hooke/add-hook #'lcompile/compile (tasks/standard-hook :scss :once))
  (hooke/add-hook #'lclean/clean     (tasks/standard-hook :scss :clean)))

; Lein1 hooks have to be called manually, in lein2 the activate function will
; be automatically called.
(when-not lein2? (activate))
