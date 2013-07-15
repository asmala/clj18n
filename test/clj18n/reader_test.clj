(ns clj18n.reader-test
  (:require [clojure.test :refer :all]
            [clj18n.reader :refer :all])
  (:import [java.util Locale]))

(deftest read-dict-test
  (let [dict (read-dict "{#clj18n/locale :en {:hi \"Hello\"}}")
        en (Locale. "en")]
    (is (= ((dict en) :hi) "Hello"))))

(def dict {(Locale. "en") {:hi "Hello" :hi-with-name "Hello, %s" :bye "Goodbye"}
           (Locale. "en" "US") {:hi "Howdy"}})

(deftest expand-dict-test
  (let [dict (expand-dict dict)]
    (is (= "Hello"
           ((dict (Locale. "en")) :hi)))
    (is (= "Goodbye"
           ((dict (Locale. "en" "US")) :bye)))))
