(ns leiningen.lein-haml-sass.options-spec
  (:use [speclj.core]
        [leiningen.lein-haml-sass.options]))

(describe "options"

  (describe "fn extract-option"

    (it "returns nil when  it doesn't find the src-type in the project map"
      (with-out-str ;; Just to capture the println output
        (should= nil (extract-options :haml {}))))

    (it "warns the user when it doesn't find the src-type in the project map"
      (should= "WARNING: no :haml entry found in project definition.\n"
               (with-out-str (extract-options :haml {}))))

    (it "doesn't warns the user when it finds the src-type in the project map"
      (def have-called-println (atom false))
      (with-redefs [println (fn [& messages]
                              (reset! have-called-println true))]
        (should-not @have-called-println)
        (extract-options :haml {:haml {}})
        (should-not @have-called-println)))

    (it "references the correct gem name for haml src-type"
      (should= "haml" (:gem-name (extract-options :haml {:haml {}}))))

    (it "references the correct gem name for sass src-type"
      (should= "sass" (:gem-name (extract-options :sass {:sass {}}))))

    (it "references the correct gem name for scss src-type"
      (should= "sass" (:gem-name (extract-options :scss {:scss {}}))))

    (it "ignores the clean hook"
      (should= #{:clean}
               (:ignore-hooks (extract-options :haml {:haml {:ignore-hooks [:clean]}}))))

    (it "ignores the compile hook"
      (should= #{:once}
               (:ignore-hooks (extract-options :haml {:haml {:ignore-hooks [:compile]}}))))

    (it "contains the source type"
      (should= :haml
               (:src-type (extract-options :haml {:haml {}}))))

    (context "defaults"
      (it "uses the 'resources' folder"
        (should= "resources"
                 (:src (extract-options :haml {:haml {}}))))

      (it "doesn't set any default extension"
        (should= ""
                 (:output-extension (extract-options :haml {:haml {}}))))

      (it "deletes the output directory"
        (should (:delete-output-dir (extract-options :haml {:haml {}}))))

      (it "uses a compile delay of 250 ms"
        (should= 250
                 (:auto-compile-delay (extract-options :haml {:haml {}}))))

      (it "contains a :nested formatting style for sass"
        (should= :nested (:style (extract-options :sass {:sass {}}))))

      (it "contains a :nested formatting style for scss"
        (should= :nested (:style (extract-options :scss {:scss {}})))))

    (context "overwriting defaults"
      (it "lets you set sources folder"
        (should= "other/folder"
                 (:src (extract-options :haml {:haml {:src "other/folder"}}))))

      (it "lets you set the output extension"
        (should= "ext"
                 (:output-extension (extract-options :haml {:haml {:output-extension "ext"}}))))

      (it "lets you unset the delete outpout directory flag"
        (should-not (:delete-output-dir (extract-options :haml {:haml {:delete-output-dir false}}))))

      (it "lets you set a compile delay"
        (should= 500
                 (:auto-compile-delay (extract-options :haml {:haml {:auto-compile-delay 500}}))))

      (it "lets you set the formatting style for sass"
        (should= :compressed (:style (extract-options :sass {:sass {:style :compressed}}))))

      (it "lets you set the formatting style for scss"
        (should= :compressed (:style (extract-options :scss {:scss {:style :compressed}})))))))
