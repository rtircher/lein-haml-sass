(ns leiningen.file-util-spec
  (:use [speclj.core]
        [leiningen.file-utils])
  (:require [clojure.java.io :as io]))

(describe "file-util"

  (describe "fn replace-haml-extension"

    (it "returns the filename with the extension replaced"
        (let [converted-file (.getPath (replace-haml-extension (io/file "blah.haml") ".html"))]
          (should (re-matches #".*.html$" converted-file))
          (should-not (re-matches #".*.haml" converted-file))))


    )

  (let [single-file-path "spec/files/single"
        multiple-file-path "spec/files/multiple"]

    (describe "fn haml-dest-files-from"

      (it "returns an empty sequence if the source directory is not found"
          (should= [] (haml-dest-files-from "not-there")))

      (it "returns the relative haml file paths (from folder given in param)"
          (should= ["spec/files/multiple/blah.haml" "spec/files/multiple/blah2.haml"]
                   (map :haml (haml-dest-files-from multiple-file-path))))

      (it "returns the relative dest file path along with the original haml path"
          (should= [{:haml "spec/files/single/haml/blah.haml"
                     :dest "spec/files/single/haml/blah.html"}]
                   (haml-dest-files-from single-file-path)))

      (it "returns the dest file path using the specified dest folder"
          (should= ["other_dest/blah.html"]
                   (map :dest (haml-dest-files-from single-file-path {:dest "other_dest"}))))

      (it "returns the dest file path using the specified dest extension"
          (should= ["spec/files/single/haml/blah.ext"]
                   (map :dest (haml-dest-files-from single-file-path {:ext "ext"}))))


      ))

  )


;; (run-specs)
