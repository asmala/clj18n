Clj18n: Simple Clojure Internationalization [![Build Status](https://secure.travis-ci.org/asmala/clj18n.png?branch=master)](http://travis-ci.org/asmala/clj18n)
===========================================

Clj18n is a simple Clojure internationalization library aiming to for simplicity
in the [Hicksian meaning](http://www.infoq.com/presentations/Simple-Made-Easy)
of the term.

For another take on Clojure internatlization, and the original inspiration for
Clj18n, also check out [Tower](https://github.com/ptaoussanis/tower).


Installation
------------

Add the following to your `project.clj`:

```clojure
[clj18n "0.2.1"]
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


### Translation closure

Load your translations by locale as a closure from the dictionary.

```clojure
; Load your dictionary at compile-time.
(def dict (load-dict "dictionary.edn"))

; Create a closure by locale and dictionary
(defn run-session
  [{:keys [locale] :as user}]
  (let [t (create-t locale dict)]
    (display-window (t [:title]))))
```


### Ring middleware

Use Ring middleware to integrate Clj18n with your web app.

```clojure
; Load the dictionary from the EDN file using Ring middleware.
(def app
  (-> routes
      compojure.handler/site
      (wrap-i18n (load-dict "dictionary.edn")
                 :default :en_US)))

; Access translations via the translation function bound to :t in the request.
; Localizations can be accessed via the :fmt key.
(defn index
  [{:keys [fmt t]}]
  (t [:hi] "Jim" (fmt (Date.))))
```


### Localization

Localization is implemented via the `Localization` protocol which declares one
method called `localize`. Clj18n comes with default implementations for
java.util.Date, java.lang.Number, and nil. The method is meant to be called
via closures created with `create-fmt`.

```clojure
(let [fmt (create-fmt (Locale. "en" "US"))]
  (fmt 20000)
  (fmt 20000 :currency)
  (fmt (Date.))
  (fmt (Date.) :date)
  (fmt (Date.) :date-long)
  (fmt (Date.) :datetime-full)
  (fmt (Date.) :time-short))
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
