(ns clj18n
  (:require [clojure.string :as string])
  (:import [java.util Locale]))

(defn make-locale
  "Creates a Java Locale object with a lowercase ISO-639 language code,
  optional uppercase ISO-3166 country code, and optional vendor-specific
  variant code."
  ([lang] (Locale. lang))
  ([lang country] (Locale. lang country))
  ([lang country variant] (Locale. lang country variant)))

(defprotocol LocaleConversion
  "Conversion of objects to java.util.Locale instances."
  (to-locale [obj]
    "Returns a java.util.Locale corresponding to obj."))

(extend-protocol LocaleConversion
  Locale
  (to-locale [loc] loc)
  String
  (to-locale [s] (apply make-locale (string/split s #"[_-]")))
  clojure.lang.Keyword
  (to-locale [k] (to-locale (name k))))

(def ^{:dynamic true :private true} *locale* nil)

(defn locale
  "Returns the current locale."
  []
  (or *locale* (Locale/getDefault)))

(defmacro with-locale
  "Executes body within the context of thread-local locale binding, enabling
  use of translation and localization functions. loc can be a java.util.Locale
  or a keyword of the form :en, :en-US, or :en-US-variant."
  [loc & body] `(binding [*locale* (to-locale ~loc)] ~@body))
