(ns clj18n.ring.middleware-test
  (:require [clojure.test :refer :all]
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
         "en")))

(deftest wrap-locale-test
  (let [app (-> identity
                (wrap-locale :locale-fns [(constantly nil)
                                          (constantly "en-US")
                                          (constantly "en")]))]
    (is (= (app {})
           {:clj18n.ring.middleware/locale "en-US"})))
  (let [app (-> identity
                (wrap-locale :locale-fns [(constantly nil)]
                             :default "fi-FI"))]
    (is (= (app {})
           {:clj18n.ring.middleware/locale "fi-FI"}))))

(deftest wrap-translation-test
  (let [app (-> identity
                (wrap-translation {:en {:hi "Hello"}}))
        resp (app {:clj18n.ring.middleware/locale :en})
        t (resp :t)]
    (is (= (t [:hi])
           "Hello"))))

(deftest wrap-i18n-test
  (let [app (-> identity
                (wrap-i18n {(Locale. "fi") {:hi "Terve"}}
                           :default (Locale. "fi")))
        resp (app {})
        t (resp :t)]
    (is (= (t [:hi]) "Terve"))))
