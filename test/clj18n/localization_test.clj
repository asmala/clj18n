(ns clj18n.localization-test
  (:require [clojure.test :refer :all]
            [clj18n.localization :refer :all])
  (:import [java.text DateFormat]
           [java.util Locale]))

(deftest localize-Date-test
  (let [loc (Locale. "en" "US")
        formatter (DateFormat/getDateInstance DateFormat/SHORT loc)
        date (.parse formatter "6/22/2013")]
    (is (= (fmt date loc) "Jun 22, 2013"))
    (is (= (fmt date loc :time) "12:00:00 AM"))
    (is (= (fmt date loc :time-short) "12:00 AM"))
    (is (= (fmt date loc :datetime-medium) "Jun 22, 2013 12:00:00 AM"))
    (is (= (fmt date loc :date-long) "June 22, 2013"))
    (is (= (fmt date loc :date-full) "Saturday, June 22, 2013"))))

(deftest localize-Number-test
  (let [loc (Locale. "en" "US")
        n 0.8]
    (is (= (fmt n loc) "0.8"))
    (is (= (fmt n loc :integer) "1"))
    (is (= (fmt n loc :percentage) "80%"))
    (is (= (fmt n loc :currency) "$0.80"))))

(deftest localize-nil-test
  (let [loc (Locale. "en")]
    (is (= (fmt nil loc) ""))))

(deftest parse-date-test
  (let [loc (Locale. "en" "US")
        formatter (DateFormat/getDateInstance DateFormat/SHORT loc)
        date (.parse formatter "6/22/2013")]
    (is (= date (parse-date "Jun 22, 2013" loc)))
    (is (= date (parse-date "Jun 22, 2013 12:00:00 AM" loc :datetime-medium)))))

(deftest parse-num-test
  (let [loc (Locale. "en" "US")]
    (is (= 2 (parse-num "2" loc)))
    (is (= 0.8 (parse-num "80.0%" loc :percentage)))
    (is (= 0.8 (parse-num "$0.80" loc :currency)))))

(deftest loc-comparator-test
  (let [loc-compare (loc-comparator (Locale. "fi"))]
    (is (= ["Kuopio" "Köyliö"]
           (sort loc-compare ["Köyliö" "Kuopio"])))))

(deftest loc-sort-test
  (is (= ["Kuopio" "Köyliö"]
         (loc-sort ["Köyliö" "Kuopio"] (Locale. "fi")))))
