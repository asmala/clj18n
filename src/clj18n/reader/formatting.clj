(ns clj18n.reader.formatting
  "Formatting functions for dictionary EDN files."
  (:require [clojure.string :refer [escape]]
            [markdown.core :as md]))

(defn parse-markdown
  "Parses the string as Markdown."
  [s]
  (md/md-to-html-string s))

(defn escape-html
  "Escapes <, >, and &."
  [s]
  (escape s {\< "&lt;" \> "&gt;" \& "&amp;"}))

(def ^:const default-formatters
  "The default formatters for use when reading dictionary EDN files."
  {'clj18n/md #'parse-markdown
   'clj18n/esc #'escape-html})
