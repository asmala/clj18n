(ns clj18n.core-test
  (:require [clojure.test :refer :all]
            [clj18n.core :refer :all])
  (:import [java.util Locale]))

(deftest to-locale-test
  (is (= (Locale. "fi" "FI")
         (to-locale (Locale. "fi" "FI"))))
  (is (= (Locale. "fi" "FI")
         (to-locale :fi-FI)))
  (is (= (Locale. "fi" "FI")
         (to-locale "fi_FI")))
  (is (= (Locale. "fi" "FI")
         (to-locale ["fi" "FI"])))
  (is (= (Locale. "fi" "FI")
         (to-locale (seq ["fi" "FI"])))))
