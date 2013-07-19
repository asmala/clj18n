(ns clj18n.translation-test
  (:require [clojure.test :refer :all]
            [clj18n.translation :refer :all])
  (:import [java.util Locale]))

(def dict {(Locale. "en") {:hi "Hello" :hi-with-name "Hello, %s" :bye "Goodbye"}
           (Locale. "en" "US") {:hi "Howdy"}})

(deftest translate-test
  (is (= "Howdy"
         (translate dict (Locale. "en" "US") [:hi])))
  (is (= "Hello, Janne"
         (translate dict (Locale. "en") [:hi-with-name] "Janne"))))
