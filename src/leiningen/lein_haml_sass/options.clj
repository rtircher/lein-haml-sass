(ns leiningen.lein-haml-sass.options)

(def ^:private default-options {:src "resources"
                                 :output-extension "html"
                                 :delete-output-dir true
                                 :auto-compile-delay 250})

(defn- normalize-hooks [options]
  (let [hooks            (into #{} (:ignore-hooks options))
        normalized-hooks (if (:compile hooks) (conj hooks :once) hooks)] 
    (assoc options :ignore-hooks normalized-hooks)))

(defn- normalize-options [options]
  (->> options
       normalize-hooks
       (merge default-options)))

(defn extract-options [project]
  (when (nil? (:haml project))
    (println "WARNING: no :haml entry found in project definition."))
  (normalize-options (:haml project)))
