(ns leiningen.lein-common.file-util-spec
  (:use [speclj.core]
        [leiningen.lein-common.file-utils])
  (:require [clojure.java.io :as io]))

(describe "file-util"

  (describe "fn replace-haml-extension"
    (with-all replace-extension
              #'leiningen.lein-common.file-utils/replace-extension)

    (it "returns the filename with the extension replaced"
      (let [converted-file (.getPath (@replace-extension (io/file "blah.haml") "haml" ".html"))]
        (should= "blah.html" converted-file)))

    (it "returns the filename with the extension replaced when extension doesn't start with a '.'"
      (let [converted-file (.getPath (@replace-extension (io/file "blah.haml") "haml" "html"))]
        (should= "blah.html" converted-file)))

    (it "removes the last extension when the new extension is an empty string"
      (let [converted-file (.getPath (@replace-extension (io/file "blah.haml") "haml" ""))]
        (should= "blah" converted-file)))

    (it "removes the last extension when the new extension is nil"
      (let [converted-file (.getPath (@replace-extension (io/file "blah.haml") "haml" nil))]
        (should= "blah" converted-file)))

    )

  (describe "fn haml-file?"
    (with-all ends-with-extension
              #'leiningen.lein-common.file-utils/ends-with-extension)
    (it "returns true for a haml file"
      (should (@ends-with-extension (io/file "spec/files/multiple/blah.haml") "haml")))

    (it "returns false for non haml file"
      (should-not (@ends-with-extension (io/file "spec/files/mulitiple/blah.sass") "haml"))))

  (describe "fn haml-dest-files-from"
    (with-all single-file-path "spec/files/single")
    (with-all multiple-file-path "spec/files/multiple")
    (with-all haml-dest-files-from (partial dest-files-from "haml"))

    (it "returns an empty sequence if the source directory is not found"
      (should= [] (@haml-dest-files-from "not-there" nil "html")))

    (it "returns the relative haml file paths (from folder given in param)"
      (should= ["spec/files/multiple/blah.haml" "spec/files/multiple/blah2.haml"]
               (map :haml (@haml-dest-files-from @multiple-file-path nil "html"))))

    (it "returns the relative dest file path along with the original haml path"
      (should= [{:haml "spec/files/single/haml/blah.haml"
                 :dest "spec/files/single/haml/blah.html"}]
               (@haml-dest-files-from @single-file-path nil "html")))

    (it "returns the dest file path using the specified dest folder"
      (should= ["other_dest/blah.html", "other_dest/blah2.html"]
               (map :dest (@haml-dest-files-from @multiple-file-path "other_dest" "html"))))

    (it "returns the dest file path keeping the folder hierachy"
      (should= ["other_dest/haml/blah.html"]
               (map :dest (@haml-dest-files-from @single-file-path "other_dest" "html"))))

    (it "returns the dest file path using the specified dest extension"
      (should= ["spec/files/single/haml/blah.ext"]
               (map :dest (@haml-dest-files-from @single-file-path nil "ext")))))

  (describe "fn dir-empty?"
    (it "is true when the directory is empty"
      (should (dir-empty? "spec/files/empty_dir")))

    (it "is false when the directory not is empty"
      (should-not (dir-empty? "spec/files"))))

  (describe "fn delete-directory-recursively!"
    (it "doesn't do anything if the file doesn't exists"
      (should-not (.exists (io/file "spec/file/does_not_exits")))
      (delete-directory-recursively! "spec/file/does_not_exits")
      (should-not (.exists (io/file "spec/file/does_not_exits"))))

    (it "deletes the directory recursively"
      (io/make-parents "spec/out/sub/blah")
      (spit "spec/out/blah" "blah")
      (spit "spec/out/sub/blah" "blah")
      (delete-directory-recursively! "spec/out")
      (should-not (.exists (io/file "spec/out"))))))
