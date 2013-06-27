(ns clj18n.reader
  "EDN reader for dictionaries."
  (:require [clojure.string :refer [split]]
            [clojure.tools.reader.edn :as edn])
  (:import [java.util Locale]))

(defn make-locale
  "Creates a Java Locale object with a lowercase ISO-639 language code,
  optional uppercase ISO-3166 country code, and optional vendor-specific
  variant code."
  ([lang] (Locale. lang))
  ([lang country] (Locale. lang country))
  ([lang country variant] (Locale. lang country variant)))

(defn parse-locale
  "Returns a Java Locale corresponding to the symbol or string."
  [nameable]
  (apply make-locale (split (name nameable) #"[_-]")))

(defmethod print-method java.util.Locale [loc ^java.io.Writer w]
  (.write w (str "#clj18n/locale :" (str loc))))

(defmethod print-dup java.util.Locale [loc w]
  (print-method loc w))

(defn read-dict
  "Reads a dictionary from an EDN string."
  [s]
  (edn/read-string {:readers {'clj18n/locale #'parse-locale}} s))
