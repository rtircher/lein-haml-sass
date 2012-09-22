(ns leiningen.lein-haml.render-engine-spec
  (:use [speclj.core])
  (:require [leiningen.lein-haml.render-engine :as engine]))

(describe "render-engine"

  (let [render                 #'engine/render
        ensure-engine-started! #'engine/ensure-engine-started!]

    (describe "fn render"
      (before (ensure-engine-started!))

      (it "render the template correctly using haml gem"
          (let [template "%html.a-class" ]
            (should= (render template) "<html class='a-class'></html>\n")))

      ))
  )