(ns leiningen.lein-common.file-utils
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defn- ends-with-extension [file ext]
  (and (.isFile file) (.endsWith (.getName file) (str "." ext))))

(defn- files-from [ext dir]
  (let [f (io/file dir)
        fs (file-seq f)]
    (filter #(ends-with-extension % ext) fs)))

(defn- replace-extension [file src-ext dest-ext]
  (io/file (string/replace (.getPath file) (re-pattern (str "." src-ext "$")) (str "." dest-ext))))

(defn- replace-dest-dir [file root-dir dest-dir]
  (if dest-dir
    (let [rel-file (string/replace (.getCanonicalPath file) (.getCanonicalPath (io/file root-dir)) "")]
      (str dest-dir rel-file))
    (.getPath file)))

(defn dest-files-from [src-ext src-dir dest-dir dest-ext]
  (map #(hash-map (keyword src-ext) (.getPath %)
                  :dest (replace-dest-dir (replace-extension % src-ext dest-ext) src-dir dest-dir))
       (files-from src-ext src-dir)))

(def haml-dest-files-from (partial dest-files-from "haml"))

(defn exists [dir]
  (and dir (.exists (io/file dir))))

(defn dir-empty? [dir]
  (not (reduce (fn [memo path] (or memo (.isFile path))) false (file-seq (io/file dir)))))

(defn delete-file! [file]
  (when (.exists file)
    (println (str "Deleting: " file))
    (io/delete-file file)))

(defn delete-directory-recursively! [base-dir]
  (doseq [file (reverse (file-seq (io/file base-dir)))]
    (println (str "Deleting: " file))
    (io/delete-file file)))