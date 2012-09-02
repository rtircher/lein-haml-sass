(ns leiningen.file-utils
  (:require [clojure.java.io :as io]))

(def ^:private haml-extension "haml")
(def ^:private default-extension "html")

(defn- ends-with-haml-extension [file]
  (.endsWith (.getName file) (str "." haml-extension)))

(defn- haml-file? [file]
  (and (.isFile file) (ends-with-haml-extension file)))

(defn haml-files-from [dir]
  (let [f (io/file dir)
        fs (file-seq f)]
    (filter haml-file? fs)))

(defn replace-haml-extension [file new-extension]
  (io/file (clojure.string/replace (.getPath file) (re-pattern #".haml$") (str "." new-extension))))

(defn- normalize-dir [dest-dir]
  (str dest-dir
       (when (not (.endsWith dest-dir "/")) "/")))

(defn- replace-dest-dir [file dest-dir]
  (if dest-dir
    (str (normalize-dir dest-dir) (.getName file))
    (.getPath file)))

(defn haml-dest-files-from
  ([dir] (haml-dest-files-from dir {}))
  ([dir opts]
     (let [dest-dir (:dest opts)
           new-ext  (or (:ext opts) default-extension)]
       (map #(hash-map :haml (.getPath %)
                       :dest (replace-dest-dir (replace-haml-extension % new-ext) dest-dir))
            (haml-files-from dir)))))
