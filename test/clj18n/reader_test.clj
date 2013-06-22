(ns clj18n.reader-test
  (:require [clojure.test :refer :all]
            [clj18n.reader :refer :all])
  (:import [java.util Locale]))

(deftest make-locale-test
  (is (= (make-locale "en" "US")
         (Locale. "en" "US"))))

(deftest parse-locale-test
  (let [english (parse-locale "en")
        boston (parse-locale :en_US_Boston)]
    (is (= english (Locale. "en")))
    (is (= boston (Locale. "en" "US" "Boston")))))

(deftest read-dict-test
  (let [dict (read-dict "{#clj18n/locale :en {:hi \"Hello\"}}")
        en (Locale. "en")]
    (is (= ((dict en) :hi) "Hello"))))
