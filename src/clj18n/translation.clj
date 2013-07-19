(ns clj18n.translation
  "Core translation functionality.")

(defn translate
  "Returns a translation corresponding to ks in dict under the given locale. If
  passed args, the translated string will be formatted using String/format."
  ([dict loc ks]
     (get-in dict (cons loc ks)))
  ([dict loc ks & args]
     (String/format loc (translate dict loc ks) (to-array args))))
