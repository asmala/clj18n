Clj18n: Simple Clojure Internationalization [![Build Status](https://secure.travis-ci.org/asmala/clj18n.png?branch=master)](http://travis-ci.org/asmala/clj18n)
===========================================

A playground for Clojure internationalization. While the library is fairly
complete, most users will likely be better served by the more mature
[Tower](https://github.com/ptaoussanis/tower).


Installation
------------

Add the following to your `project.clj`:

```clojure
[clj18n "0.3.0"]
```

For other options, please refer to the library
[Clojars page](https://clojars.org/clj18n).


Documentation
-------------

You can find examples below and for more extensive documentation you
can refer to the [API docs](http://asmala.github.io/clj18n).


Usage
-----


### Standard dictionary format

Write your dictionary in EDN.

```clojure
{#clj18n/locale :en
 {:hi "Hello %s"
  :bye "Goodbye"}
 #clj18n/locale :en_US
 {:hi "Howdy %s"}}
```

Clj18n parses the `#clj18n/locale` literals into java.util.Locale instances.


### Translation

Access translations directly or create a closure around the dictionary.

```clojure
; Load your dictionary at compile-time.
(def dict (load-dict "dictionary.edn"))

; User string interpolation
(translate dict [:front-page :logged-in] "Joe")

; Use a closure and control locale using with-locale
(let [t (partial translate dict)]
  (with-locale :fi
    (t [:front-page :title])))
```


### Ring middleware

Use Ring middleware to integrate Clj18n with your web app.

```clojure
; Load the dictionary from the EDN file using Ring middleware.
; Set locale based on :locale-fns and :default.
(def app
  (-> routes
      compojure.handler/site
      (wrap-i18n (load-dict "dictionary.edn")
                 :locale-fns [locale-from-session]
                 :default :en_US)))

; Access translations via the translation closure bound to :t in the request.
(defn index
  [{:keys [t]}]
  (t [:hi] "Jim"))
```


### Localization

Localization is implemented via the `Localization` protocol which declares one
method called `fmt`. Clj18n comes with default implementations for
java.util.Date, java.lang.Number, and nil.


```clojure
; Format dates and numbers as strings.
(with-locale :fi
  (fmt 20000)
  (fmt 20000 :currency)
  (fmt (Date.))
  (fmt (Date.) :date)
  (fmt (Date.) :date-long)
  (fmt (Date.) :datetime-full)
  (fmt (Date.) :time-short))

; Parse strings and sort collections.
(with-locale :fi
  (parse-date date-string)
  (parse-num (params :total) :currency)
  (loc-sort ["Köyliö" "Kuopio"]))
```

Contributing
------------

If you have suggestions for the library, you are welcome to open up a
[new issue](https://github.com/asmala/clj18n/issues/new). I also
welcome code contributions, in which case I would recommend a
[pull request](https://help.github.com/articles/using-pull-requests)
with a feature branch.


License
-------

Copyright © 2013 Janne Asmala

Distributed under the
[Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html),
the same as Clojure.
