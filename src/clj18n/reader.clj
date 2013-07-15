(ns clj18n.reader
  "EDN reader for dictionaries."
  (:require [clj18n :refer [make-locale]]
            [clojure.string :refer [split]]
            [clojure.tools.reader.edn :as edn]))

(defmethod print-method java.util.Locale [loc ^java.io.Writer w]
  (.write w (str "#clj18n/locale :" (str loc))))

(defmethod print-dup java.util.Locale [loc w]
  (print-method loc w))

(defn read-dict
  "Reads a dictionary from an EDN string."
  [s]
  (edn/read-string {:readers {'clj18n/locale #'clj18n/to-locale}} s))

(defn- sub-seqs
  "Returns a seq of all truncated versions of coll."
  [coll]
  (take (count coll) (iterate butlast coll)))

(defn- lookup-locales
  "Returns an ordered seq of locales containing translations for loc."
  [loc]
  (let [loc-args (sub-seqs (split (str loc) #"_"))]
    (map (partial apply make-locale) loc-args)))

(defn expand-dict
  "Expands the dictionary so that child locales contain the translations from
  their parent locales, e.g. en_US would end up containing translations defined
  under en."
  [dict]
  (into (empty dict)
        (for [loc (keys dict)
              :let [translations (map dict (lookup-locales loc))]]
          [loc (apply merge (reverse translations))])))

(defn load-dict
  "Loads a dictionary from an EDN file and expands it using expand-dict."
  [f]
  (expand-dict (read-dict (slurp f))))
