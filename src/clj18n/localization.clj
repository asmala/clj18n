(ns clj18n.localization
  "Localization functionality implemented via the Localization protocol and
  various helper functions."
  (:require [clojure.string :refer [split]])
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
  [loc style]
  (let [[type length] (map keyword (split (name style) #"-"))
        format (time-format (or length :default))]
    (case type
      :date (DateFormat/getDateInstance format loc)
      :datetime (DateFormat/getDateTimeInstance
                 format format loc)
      :time (DateFormat/getTimeInstance format loc))))

(defn- num-formatter
  "Returns a number formatter corresponding to the arguments."
  [loc style]
  (case style
    :number (NumberFormat/getNumberInstance loc)
    :integer (NumberFormat/getIntegerInstance loc)
    :percentage (NumberFormat/getPercentInstance loc)
    :currency (NumberFormat/getCurrencyInstance loc)))

(defprotocol Localization
  "Localized formatting for objects."
  (fmt [obj loc] [obj loc style]
    "Returns a localized string corresponding to obj in the given locale. Can
    optionally be given a style to control how the object is formatted."))

(extend-protocol Localization
  Date
  (fmt
    ([date loc] (fmt date loc :date))
    ([date loc style]
       (.format (date-formatter loc style) date)))
  Number
  (fmt
    ([n loc] (fmt n loc :number))
    ([n loc style]
       (.format (num-formatter loc style) n)))
  nil
  (fmt
    ([obj loc] "")
    ([obj loc style] "")))

(defn parse-date
  "Attempts to parse s as a java.util.Date according to the format indicated by
  the given locale and an optional style."
  ([s loc] (parse-date s loc :date))
  ([s loc style]
     (.parse (date-formatter loc style) s)))

(defn parse-num
  "Attempts to parse s as a java.Util.Number according to the format indicated by
  by the given locale and an optional style."
  ([s loc] (parse-num s loc :number))
  ([s loc style]
     (.parse (num-formatter loc style) s)))

(defn loc-comparator
  "Returns a localized Unicode comparator for the given locale."
  [loc]
  (let [collator (Collator/getInstance loc)]
    (fn [x y] (.compare collator x y))))

(defn loc-sort
  "Sorts the collection using a localized Unicode comparator."
  [coll loc]
  (sort (loc-comparator loc) coll))
