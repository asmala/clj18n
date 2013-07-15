(ns clj18n.localization
  "Localization functionality implemented via the Localization protocol and
  various helper functions."
  (:require [clj18n]
            [clojure.string :refer [split]])
  (:import [java.lang Number]
           [java.text Collator DateFormat NumberFormat]
           [java.util Date]))

(def ^:private time-format
  {:default DateFormat/DEFAULT
   :short   DateFormat/SHORT
   :medium  DateFormat/MEDIUM
   :long    DateFormat/LONG
   :full    DateFormat/FULL})

(defn- date-formatter
  "Returns a date formatter corresponding to the arguments."
  [style]
  (let [[type length] (map keyword (split (name style) #"-"))
        format (time-format (or length :default))]
    (case type
      :date (DateFormat/getDateInstance format (clj18n/locale))
      :datetime (DateFormat/getDateTimeInstance
                 format format (clj18n/locale))
      :time (DateFormat/getTimeInstance format (clj18n/locale)))))

(defn- num-formatter
  "Returns a number formatter corresponding to the arguments."
  [style]
  (case style
    :number (NumberFormat/getNumberInstance (clj18n/locale))
    :integer (NumberFormat/getIntegerInstance (clj18n/locale))
    :percentage (NumberFormat/getPercentInstance (clj18n/locale))
    :currency (NumberFormat/getCurrencyInstance (clj18n/locale))))

(defprotocol Localization
  "Localized formatting for objects."
  (fmt [obj] [obj style]
    "Returns a localized string corresponding to obj. Can optionally be given
    a style to control how the object is formatted."))

(extend-protocol Localization
  Date
  (fmt
    ([date] (fmt date :date))
    ([date style]
       (.format (date-formatter style) date)))
  Number
  (fmt
    ([n] (fmt n :number))
    ([n style]
       (.format (num-formatter style) n)))
  nil
  (fmt
    ([obj] "")
    ([obj style] "")))

(defn parse-date
  "Attempts to parse s as a java.util.Date according to the format indicated by
  the current locale and an optional style."
  ([s] (parse-date s :date))
  ([s style]
     (.parse (date-formatter style) s)))

(defn parse-num
  "Attempts to parse s as a java.Util.Number according to the format indicated by
  by the current locale and an optional style."
  ([s] (parse-num s :number))
  ([s style]
     (.parse (num-formatter style) s)))

(defn loc-comparator
  "Returns a localized Unicode comparator."
  []
  (let [collator (Collator/getInstance (clj18n/locale))]
    (fn [x y] (.compare collator x y))))

(defn loc-sort
  "Sorts the collection using a localized Unicode comparator."
  [coll]
  (sort (loc-comparator) coll))
