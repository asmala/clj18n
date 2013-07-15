(ns clj18n-test
  (:require [clojure.test :refer :all]
            [clj18n :refer :all])
  (:import [java.util Locale]))

(deftest make-locale-test
  (is (= (Locale. "en")
         (make-locale "en")))
  (is (= (Locale. "en" "US")
         (make-locale "en" "US")))
  (is (= (Locale. "en" "US" "var1")
         (make-locale "en" "US" "var1"))))

(deftest to-locale-test
  (is (= (Locale. "fi" "FI")
         (to-locale (Locale. "fi" "FI"))))
  (is (= (Locale. "fi" "FI")
         (to-locale :fi-FI)))
  (is (= (Locale. "fi" "FI")
         (to-locale "fi_FI"))))

(deftest locale-test
  (is (= (locale) (Locale/getDefault))))

(deftest with-locale-test
  (is (= (Locale. "fi" "FI")
         (with-locale :fi-FI
           (locale))))
  (is (= (Locale. "fi")
         (with-locale "fi"
           (locale))))
  (is (= (Locale. "fi")
         (with-locale (Locale. "fi")
           (locale)))))
