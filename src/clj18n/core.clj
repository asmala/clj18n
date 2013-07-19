(ns clj18n.core
  (:require [clojure.string :as string])
  (:import [java.util Locale]))

(defn- make-locale
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
  clojure.lang.PersistentVector
  (to-locale [v] (apply make-locale v))
  clojure.lang.ASeq
  (to-locale [coll] (apply make-locale coll))
  String
  (to-locale [s] (to-locale (string/split s #"[_-]")))
  clojure.lang.Keyword
  (to-locale [k] (to-locale (name k))))
