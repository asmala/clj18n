(defproject clj18n "0.4.0"
  :description "Simple Clojure internationalization library."
  :url "https://github.com/asmala/clj18n"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[markdown-clj "0.9.28"]
                 [org.clojure/tools.reader "0.7.4"]]
  :profiles {:1.4  {:dependencies [[org.clojure/clojure "1.4.0"]]}
             :1.5  {:dependencies [[org.clojure/clojure "1.5.1"]]}
             :dev  {:plugins [[codox "0.6.4"]]}}
  :aliases {"test-all" ["with-profile" "test,1.4:test,1.5" "test"]}
  :min-lein-version "2.0.0")
