(defproject lein-haml-sass "0.2.0"
  :description "HAML/SASS/SCSS autobuilder plugin"
  :url "https://github.com/rtircher/lein-haml-sass"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}

  :dependencies [[org.jruby/jruby-complete "1.6.4"]]

  :dev-dependencies [[speclj "2.1.1"]
                     [speclj-growl "1.0.1"]]
  :test-path "spec/"

  :eval-in-leiningen true

  ;; Example for adding lein haml hooks
  ;; :hooks [leiningen.scss]

  ;; Example on how to use lein-haml-sass
  :haml {:src "spec/files"
         :output-directory "spec/out/haml"
         ;; Other options (provided are default values)
         ;; :output-extension html
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
         ;; :ignore-hooks [:clean :compile] ;; -> if you ue the hooks, allows you to remove some hooks that you don't want to run
         :gem-version "3.2.1"
         }

  :scss {:src "spec/files"
         :output-directory "spec/out/scss"
         ;; Other options (provided are default values)
         ;; :output-extension css
         ;; :auto-compile-delay 250
         ;; :delete-output-dir true ;; -> when running lein clean it will delete the output directory if it does not contain any file
         ;; :ignore-hooks [:clean :compile] ;; -> if you ue the hooks, allows you to remove some hooks that you don't want to run
         :gem-version "3.2.1"
         }
    )
