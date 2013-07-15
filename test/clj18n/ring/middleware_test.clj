(ns clj18n.ring.middleware-test
  (:require [clj18n :refer [with-locale]]
            [clojure.test :refer :all]
            [clj18n.ring.middleware :refer :all])
  (:import [java.util Locale]))

(deftest locale-from-header-test
  (is (= (locale-from-header {:headers {"Accept-Language" "en-US"}})
         "en-US")))

(deftest locale-from-param-test
  (is (= (locale-from-param {:params {:locale "en-US"}})
         "en-US")))

(deftest locale-from-uri-test
  (is (= (locale-from-uri {:uri "/en-US/about"})
         "en-US")))

(deftest locale-from-domain-test
  (is (= (locale-from-domain {:server-name "en.example.com"})
         "en"))
  (is (nil? (locale-from-domain {:server-name "www.example.com"})))
  (is (nil? (locale-from-domain {:server-name "tt.co"}))))

(deftest wrap-locale-test
  (let [app (-> (fn [_] (clj18n/locale))
                (wrap-locale :locale-fns [(constantly nil)
                                          (constantly "fi-FI")
                                          (constantly "fi")]))]
    (is (= (Locale. "fi" "FI") (app {}))))
  (let [app (-> (fn [_] (clj18n/locale))
                (wrap-locale :locale-fns [(constantly nil)]
                             :default "fi-FI"))]
    (is (= (Locale. "fi" "FI") (app {})))))

(deftest wrap-translation-test
  (let [app (-> (fn [{:keys [t]}] (t [:hi]))
                (wrap-translation {(Locale. "fi") {:hi "Terve"}}))]
    (is (= "Terve" (with-locale :fi (app {}))))))

(deftest wrap-i18n-test
  (let [app (-> (fn [{:keys [t]}] (t [:hi]))
                (wrap-i18n {(Locale. "fi") {:hi "Terve"}}
                           :default (Locale. "fi")))]
    (is (= "Terve" (app {})))))
