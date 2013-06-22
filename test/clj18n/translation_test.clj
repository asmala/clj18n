(ns clj18n.translation-test
  (:require [clojure.test :refer :all]
            [clj18n.translation :refer :all])
  (:import [java.util Locale]))

(def dict {(Locale. "en") {:hi "Hello" :hi-with-name "Hello, %s" :bye "Goodbye"}
           (Locale. "en" "US") {:hi "Howdy"}})

(deftest lookup-locales-test
  (is (= (lookup-locales (Locale. "en" "US" "Boston"))
         [(Locale. "en" "US" "Boston")
          (Locale. "en" "US")
          (Locale. "en")])))

(deftest expand-dict-test
  (let [dict (expand-dict dict)]
    (is (= ((dict (Locale. "en" "US")) :bye)
           "Goodbye"))))

(deftest create-t-test
  (let [t (create-t (Locale. "en" "US") (expand-dict dict))]
    (is (= (t [:hi]) "Howdy"))
    (is (= (t [:hi-with-name] "Janne") "Hello, Janne"))))
