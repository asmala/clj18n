(ns clj18n.ring.middleware
  "Ring middleware for loading dictionaries and setting locales."
  (:require [clj18n :refer [with-locale]]
            [clj18n.translation :refer [translate]]))

(defn locale-from-header
  "Gets the locale from the request Accept-Language header."
  [{:keys [headers]}]
  (when headers (headers "Accept-Language")))

(defn locale-from-param
  "Gets the locale based on the :locale param."
  [{:keys [params]}]
  (when params (params :locale)))

(defn locale-from-uri
  "Gets the locale from the request uri, using the first part of the URI as the
  locale."
  [{:keys [uri] :as req}]
  (last (re-find #"^/([^/]+)" uri)))

(defn locale-from-domain
  "Gets the locale from the request server-name, by looking for a two-character
  language subdomain, avoiding capture of www and main domain."
  [{:keys [server-name]}]
  (last (re-find  #"^([^\.]{2})\..+\..+" server-name)))

(defn wrap-locale
  "Wraps app in a clj18n/with-locale call, with locale bound to the first value
  returned by :locale-fns or to :default. :locale-fns are functions that take
  the request as argument and return a locale string or nil."
  [app & {:keys [default locale-fns]
          :or {locale-fns [locale-from-header locale-from-param]}}]
  (fn [req]
    (let [loc (first (filter boolean (map #(% req) locale-fns)))]
      (with-locale (or loc default)
        (app req)))))

(defn wrap-translation
  "Sets request :t to be a function of translations for dict."
  [app dict]
  (let [t (partial translate dict)]
    (fn [req]
      (app (assoc req :t t)))))

(defn wrap-i18n
  "Calls both wrap-locale and wrap-translation with the given arguments."
  [app dict & args]
  (apply wrap-locale (wrap-translation app dict) args))
