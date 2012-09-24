(ns leiningen.lein-common.file-utils
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def ^:private haml-extension "haml")
(def ^:private default-extension "html")

(defn- ends-with-haml-extension [file]
  (.endsWith (.getName file) (str "." haml-extension)))

(defn- haml-file? [file]
  (and (.isFile file) (ends-with-haml-extension file)))

(defn- haml-files-from [dir]
  (let [f (io/file dir)
        fs (file-seq f)]
    (filter haml-file? fs)))

(defn- replace-haml-extension [file new-extension]
  (io/file (string/replace (.getPath file) (re-pattern #".haml$") (str "." new-extension))))

(defn- replace-dest-dir [file root-dir dest-dir]
  (if dest-dir
    (let [rel-file (string/replace (.getCanonicalPath file) (.getCanonicalPath (io/file root-dir)) "")]
      (str dest-dir rel-file))
    (.getPath file)))

(defn haml-dest-files-from
  ([dir] (haml-dest-files-from dir {}))
  ([dir opts]
     (let [dest-dir (:dest opts)
           new-ext  (or (:ext opts) default-extension)]
       (map #(hash-map :haml (.getPath %)
                       :dest (replace-dest-dir (replace-haml-extension % new-ext) dir dest-dir))
            (haml-files-from dir)))))

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