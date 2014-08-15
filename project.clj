(defproject lein-haml-sass "0.2.7-SNAPSHOT"
  :description "HAML/SASS/SCSS autobuilder plugin"
  :url "https://github.com/rtircher/lein-haml-sass"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}

  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.jruby/jruby-complete "1.6.8"]
                 [com.cemerick/pomegranate "0.2.0"]]

  :profiles {:dev {:dependencies [[speclj "2.5.0"]
                                  [org.rubygems/haml "3.1.7"]
                                  [org.rubygems/sass "3.2.9"]]
                   :plugins [[speclj "2.5.0"]]
                   :test-paths ["spec/"]
                   :repositories [["gem-jars" "http://deux.gemjars.org"]]}

             :plugin-example {
                              ;; Example for adding lein haml hooks
                              ;; :hooks [leiningen.scss leiningen.sass leiningen.haml]

                              ;; Example on how to use lein-haml-sass

                              :haml {:src "spec/files"
                                     :output-directory "spec/out/haml"
                                     ;; Other options (provided are default values)
                                     :output-extension "html"
                                     ;; :auto-compile-delay 250
                                     ;; :delete-output-dir true ;; -> when running lein clean it will delete the output directory if it does not contain any file
                                     ;; :ignore-hooks [:clean :compile] ;; -> if you ue the hooks, allows you to remove some hooks that you don't want to run
                                     :gem-version "3.1.7"
                                     }

                              :sass {:src "spec/files"
                                     :output-directory "spec/out/sass"
                                     ;; Other options (provided are default values)
                                     ;; :output-extension css
                                     ;; :auto-compile-delay 250
                                     ;; :delete-output-dir true ;; -> when running lein clean it will delete the output directory if it does not contain any file
                                     :ignore-hooks [:deps] ;; -> if you ue the hooks, allows you to remove some hooks that you don't want to run
                                     :style :nested ;; valid: :nested, :expanded, :compact, :compressed
                                     }

                              :scss {:src "spec/files"
                                     :output-directory "spec/out/scss"
                                     ;; Other options (provided are default values)
                                     ;; :output-extension css
                                     ;; :auto-compile-delay 250
                                     ;; :delete-output-dir true ;; -> when running lein clean it will delete the output directory if it does not contain any file
                                     ;; :ignore-hooks [:clean :compile] ;; -> if you ue the hooks, allows you to remove some hooks that you don't want to run
                                     :gem-version "3.2.9"
                                     :style :nested ;; valid: :nested, :expanded, :compact, :compressed
                                     }}
                   }


  :eval-in-leiningen true
  :min-lein-version "2.0.0"
  )
