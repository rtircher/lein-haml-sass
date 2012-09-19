(defproject lein-haml "0.0.0-SNAPSHOT"
  :description "HAML autobuilder plugin"
  :url "https://github.com/rtircher/lein-haml"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}

  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [org.jruby/jruby-complete "1.6.4"]]

  :dev-dependencies [[speclj "2.1.1"]
                     [speclj-growl "1.0.1"]]
  :test-path "spec/"

  :eval-in-leiningen true

  ;; Example on how to use lein-haml
  :haml {:haml-src "spec/files"
         :output-directory "spec/out"
         ;; Other options (provided are default values)
         ;; :output-extension html
         ;; :auto-compile-delay 250
         ;; :delete-output-dir true ;; -> when running lein clean it will delete the output directory if it does not contain any file
         }
  )

;; Add to local maven repo
;; mvn install:install-file -DgroupId=lein-haml -DartifactId=lein-haml -Dversion=0.0.0-SNAPSHOT -Dpackaging=jar -Dfile=/Users/rtircher/Development/lein-haml/lein-haml-0.0.0-SNAPSHOT.jar