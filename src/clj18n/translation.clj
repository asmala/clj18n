(ns clj18n.translation
  "Core translation functionality. The main entry point is create-t."
  (:require [clojure.string :refer [split]]
            [clj18n.reader :refer [make-locale read-dict]]))

(defn- sub-seqs
  "Returns a seq of all truncated versions of coll."
  [coll]
  (take (count coll) (iterate butlast coll)))

(defn lookup-locales
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

(defn- translate
  "Returns a translation corresponding to ks in trans. If passed args, the
  translated string will be formatted using String/format. Designed to be called
  via a function created by create-t."
  ([loc trans ks]
   (get-in trans ks))
  ([loc trans ks & args]
   (String/format loc (translate loc trans ks) (to-array args))))

(defn create-t
  "Creates a translation function for the given locale and dictionary map."
  [loc dict]
  (partial translate loc (dict loc)))
