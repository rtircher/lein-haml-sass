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

    ))
