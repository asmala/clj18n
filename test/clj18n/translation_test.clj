(ns clj18n.translation-test
  (:require [clojure.test :refer :all]
            [clj18n :refer [with-locale]]
            [clj18n.translation :refer :all])
  (:import [java.util Locale]))

(def dict {(Locale. "en") {:hi "Hello" :hi-with-name "Hello, %s" :bye "Goodbye"}
           (Locale. "en" "US") {:hi "Howdy"}})

(deftest translate-test
  (is (= "Howdy"
         (with-locale :en-US (translate dict [:hi]))))
  (is (= "Hello, Janne"
         (with-locale :en (translate dict [:hi-with-name] "Janne")))))
