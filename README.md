Clj18n: Simple Clojure Internationalization [![Build Status](https://secure.travis-ci.org/asmala/clj18n.png?branch=master)](http://travis-ci.org/asmala/clj18n)
===========================================

Clj18n is a simple Clojure internationalization library aiming to for simplicity
in the [Hicksian meaning](http://www.infoq.com/presentations/Simple-Made-Easy)
of the term.

Note that Clj18n does not attempt to be compatible with older versions of
Clojure. For a good Clojure I18n library that supports older versions, please
see [Tower](https://github.com/ptaoussanis/tower).


## Installation

Add the following to your `project.clj`:

```clojure
[clj18n "0.1.0"]
```

For other options, please refer to the library
[Clojars page](https://clojars.org/clj18n).


## Documentation

You can find examples below and for more extensive documentation you
can refer to the [API docs](http://asmala.github.io/clj18n).


## Usage

```clojure
; Write your dictionary in EDN
{#clj18n/locale :en
 {:hi "Hello %s"
  :bye "Goodbye"}
 #clj18n/locale :en_US
 {:hi "Howdy %s"}}

; Load the dictionary from the EDN file using Ring middleware.
(def app
  (-> routes
      compojure.handler/site
      (wrap-i18n (load-dict "dictionary.edn")
                 :default :en_US)))

; Access translations via the translation function bound to :t in the request.
(defn index
  [{:keys [t]}]
  (t [:hi] "Jim"))
```


## Contributing

If you have suggestions for the library, you are welcome to open up a
[new issue](https://github.com/asmala/clj18n/issues/new). I also
welcome code contributions, in which case I would recommend a
[pull request](https://help.github.com/articles/using-pull-requests)
with a feature branch.


## License

Copyright © 2013 Janne Asmala

Distributed under the
[Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html),
the same as Clojure.
