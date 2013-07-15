(ns clj18n.translation
  "Core translation functionality."
  (:require [clj18n]))

(defn translate
  "Returns a translation corresponding to ks in dict under current locale. If
  passed args, the translated string will be formatted using String/format."
  ([dict ks]
     (get-in dict (cons (clj18n/locale) ks)))
  ([dict ks & args]
     (String/format (clj18n/locale) (translate dict ks) (to-array args))))
