(ns clj18n.reader.formatting-test
  (:require [clojure.test :refer :all]
            [clj18n.reader.formatting :refer :all]))

(deftest parse-markdown-test
  (is (= "<p><b>Bold move!</b></p>"
         (parse-markdown "**Bold move!**"))))

(deftest escape-html-test
  (is (= "&lt;escape&gt; &amp; &lt;run&gt;"
         (escape-html "<escape> & <run>"))))
