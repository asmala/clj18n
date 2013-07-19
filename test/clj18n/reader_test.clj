(ns clj18n.reader-test
  (:require [clojure.test :refer :all]
            [clj18n.reader :refer :all])
  (:import [java.util Locale]))

(deftest read-dict-test
  (let [dict (read-dict "{#clj18n/locale :en
                          {:hi \"Hello\"
                           :escape #clj18n/esc \"<escape> & <run>\"
                           :bold #clj18n/md \"**Bold move!**\"
                           :null #test/delete \"I don't exist\"}}"
                        {'test/delete (constantly "--deleted--")})
        loc (Locale. "en")]
    (is (= "Hello"
           ((dict loc) :hi)))
    (is (= "&lt;escape&gt; &amp; &lt;run&gt;"
           ((dict loc) :escape) ))
    (is (= "<p><b>Bold move!</b></p>"
           ((dict loc) :bold) ))
    (is (= "--deleted--"
           ((dict loc) :null) ))))

(def dict {(Locale. "en") {:hi "Hello" :hi-with-name "Hello, %s" :bye "Goodbye"}
           (Locale. "en" "US") {:hi "Howdy"}})

(deftest expand-dict-test
  (let [dict (expand-dict dict)]
    (is (= "Hello"
           ((dict (Locale. "en")) :hi)))
    (is (= "Goodbye"
           ((dict (Locale. "en" "US")) :bye)))))
