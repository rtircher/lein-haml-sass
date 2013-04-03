(ns leiningen.integration-spec
  (:use [speclj.core]
        [clojure.java.shell :only [sh]])
  (:require [leiningen.lein-common.file-utils :as futils]
            [clojure.java.io :as io]))

(describe "integration tests on tasks"
  ;; This is not ideal but provides some way of testing the tasks (given
  ;; that I have figured out how to include leiningen dependencoes in
  ;; the tests) especially: we are relying on the project.clj file
  ;; (which can't be changed from here)

  (with-all ends-with-extension #'futils/ends-with-extension)
  (before (with-out-str (futils/delete-directory-recursively! "spec/out")))

  (defn run-plugin [task task-arg] (sh "lein" "with-profile" "plugin-example" task task-arg))

  (describe "haml"
    (def haml (partial run-plugin "haml"))

    (context "once"
      (it "compiles the files in the correct directory"
        (haml "once")

        (let [all-files (file-seq (io/file "spec/out"))
              html-files (filter #(@ends-with-extension % "html") all-files)]
          (should= 8 (count all-files))
          (should= 3 (count html-files)))

        (let [file-content (slurp "spec/out/haml/multiple/blah.html")
              expected-content "<html>\n  <head></head>\n  <body></body>\n</html>\n"]
          (should= expected-content file-content))))

    (context "auto")

    (context "clean"
      (it "removes all artifacts that were created by haml task"
        (haml "once")
        (should (.exists (io/file "spec/out/haml")))
        (haml "clean")
        (should-not (.exists (io/file "spec/out/haml"))))

      (it "only deletes the artifacts that were created by haml task"
        (haml "once")
        (should (.exists (io/file "spec/out/haml")))
        (spit "spec/out/haml/not-generated" "a non generated content")

        (haml "clean")

        (should (.exists (io/file "spec/out/haml/not-generated")))
        (should-not (.exists (io/file "spec/out/haml/multiple/blah.html")))
        (should-not (.exists (io/file "spec/out/haml/multiple/blah2.html")))
        (should-not (.exists (io/file "spec/out/haml/single/haml/blah.html"))))))

  (describe "sass")
  (describe "scss")
  (describe "haml-sass"))
