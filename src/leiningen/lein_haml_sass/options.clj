(ns leiningen.lein-haml-sass.options)

(def ^:private default-options {:src "resources"
                                :output-extension ""
                                :delete-output-dir true
                                :auto-compile-delay 250
                                })

(defn- normalize-hooks [options]
  (let [hooks            (into #{} (:ignore-hooks options))
        normalized-hooks (if (:compile hooks) (conj hooks :once) hooks)] 
    (assoc options :ignore-hooks normalized-hooks)))

(defn- normalize-gem-name [src-type]
  (case src-type
    :haml {:gem-name "haml"}
    :sass {:gem-name "sass"}
    :scss {:gem-name "sass"}))

(defn- normalize-options [src-type options]
  (->> (src-type options)
       normalize-hooks
       (merge (normalize-gem-name src-type))
       (merge default-options)
       (merge {:src-type src-type})))

(defn extract-options [src-type project]
  (when (nil? (src-type project))
    (println "WARNING: no " src-type " entry found in project definition."))
  (normalize-options src-type project))
