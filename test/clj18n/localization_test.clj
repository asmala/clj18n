(ns clj18n.localization-test
  (:require [clojure.test :refer :all]
            [clj18n.localization :refer :all])
  (:import [java.text DateFormat]
           [java.util Locale]))

(def fmt (create-fmt (Locale. "en" "US")))

(deftest localize-Date-test
  (let [formatter (DateFormat/getDateInstance DateFormat/SHORT
                                              (Locale. "en" "US"))
        date (.parse formatter "6/22/2013")]
    (is (= (fmt date) "Jun 22, 2013"))
    (is (= (fmt date :time) "12:00:00 AM"))
    (is (= (fmt date :time-short) "12:00 AM"))
    (is (= (fmt date :datetime-medium) "Jun 22, 2013 12:00:00 AM"))
    (is (= (fmt date :date-long) "June 22, 2013"))
    (is (= (fmt date :date-full) "Saturday, June 22, 2013"))))

(deftest localize-Number-test
  (is (= (fmt 0.8) "0.8"))
  (is (= (fmt 0.8 :integer) "1"))
  (is (= (fmt 0.8 :percentage) "80%"))
  (is (= (fmt 0.8 :currency) "$0.80")))

(deftest localize-nil-test
  (is (= (fmt nil) "")))
