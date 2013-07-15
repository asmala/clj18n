(ns clj18n.localization-test
  (:require [clojure.test :refer :all]
            [clj18n :refer [with-locale]]
            [clj18n.localization :refer :all])
  (:import [java.text DateFormat]
           [java.util Locale]))

(deftest localize-Date-test
  (let [formatter (DateFormat/getDateInstance DateFormat/SHORT
                                              (Locale. "en" "US"))
        date (.parse formatter "6/22/2013")]
    (with-locale :en-US
      (is (= (fmt date) "Jun 22, 2013"))
      (is (= (fmt date :time) "12:00:00 AM"))
      (is (= (fmt date :time-short) "12:00 AM"))
      (is (= (fmt date :datetime-medium) "Jun 22, 2013 12:00:00 AM"))
      (is (= (fmt date :date-long) "June 22, 2013"))
      (is (= (fmt date :date-full) "Saturday, June 22, 2013")))))

(deftest localize-Number-test
  (with-locale :en-US
    (is (= (fmt 0.8) "0.8"))
    (is (= (fmt 0.8 :integer) "1"))
    (is (= (fmt 0.8 :percentage) "80%"))
    (is (= (fmt 0.8 :currency) "$0.80"))))

(deftest localize-nil-test
  (is (= (fmt nil) "")))

(deftest parse-date-test
  (let [loc (Locale. "en" "US")
        formatter (DateFormat/getDateInstance DateFormat/SHORT loc)
        date (.parse formatter "6/22/2013")]
    (with-locale :en-US
      (is (= date (parse-date "Jun 22, 2013")))
      (is (= date (parse-date "Jun 22, 2013 12:00:00 AM" :datetime-medium))))))

(deftest parse-num-test
  (let [loc (Locale. "en" "US")]
    (with-locale :en-US
      (is (= 2 (parse-num "2")))
      (is (= 0.8 (parse-num "80.0%" :percentage)))
      (is (= 0.8 (parse-num "$0.80" :currency))))))

(deftest loc-comparator-test
  (let [loc-compare (with-locale :fi (loc-comparator))]
    (is (= ["Kuopio" "Köyliö"]
           (sort loc-compare ["Köyliö" "Kuopio"])))))

(deftest loc-sort-test
  (is (= ["Kuopio" "Köyliö"]
         (with-locale :fi (loc-sort ["Köyliö" "Kuopio"])))))
