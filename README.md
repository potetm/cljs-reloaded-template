# cljs-reloaded
A Leiningen template for kick-starting a reloaded cljs app.

# Provided functionality
* A default clojure namespace (`build/user.clj`) with a bunch of utility functions, including:
  * `(fresh-browser-repl)`: Cleans, builds, and starts a repl
  * `(browser-repl)`: Just starts a repl
  * `(fresh-node-repl)`: Cleans, builds, and starts a repl
  * `(node-repl)`: Just starts a repl
* Both ClojureScript REPLs are wrapped with Piggieback, so you should be able to use them seamlessly with your favorite IDE/Editor
* A `browser.user` ClojureScript namespace with all of your reloaded functions plus `(run-tests)`
* A `node.user` ClojureScript namespace with all of your reloaded functions plus `(run-tests)`
* A build script `bin/build-cljs`
* Test namespaces are automatically added to the REPLs, so you can run your tests.

# Example Usage
```
lein new cljs-reloaded my.group/my-awesome-project
cd my-awesome-project/
lein repl
```

Now in the repl:
```
user=> (fresh-browser-repl)
Navigate to  file:/private/tmp/my-awesome-project/target/public/index.html
:reloading (prod-build user)
<< started Weasel server on ws://127.0.0.1:9001 >>
<< waiting for client to connect ...  connected! >>
To quit, type: :cljs/quit
nil
cljs.user=> (refresh)
:reloading ()
:rebuilding
:requesting-reload (my.group.my-awesome-project my.group.my-awesome-project-test browser.user)
:ok
nil
cljs.user=> (in-ns 'browser.user)
nil
browser.user=> (run-tests)

Testing my.group.my-awesome-project-test

FAIL in (a-test) (at file:432:14)
FIXME, I fail.
expected: (= 0 1)
  actual: (not (= 0 1))

Ran 1 tests containing 1 assertions.
1 failures, 0 errors.
nil
cljs.user=> :cljs/quit
;; You may get a stacktrace here. That's weasel's way of saying "I'll miss you."

;; You can also use a node repl
user=> (fresh-node-repl)
cljs.user=> (refresh :before nil) ;; :before nil is useful for initially loading a node app
:reloading ()
:rebuilding
:requesting-reload (my.group.my-awesome-project my.group.my-awesome-project-test node.user)
:ok
nil
cljs.user=> (in-ns 'node.user)
nil
cljs.user=> (run-tests)

Testing my.group.my-awesome-project-test

FAIL in (a-test) (at /private/tmp/my-awesome-project/target/public/js/cljs/test.js:432:14)
FIXME, I fail.
expected: (= 0 1)
  actual: (not (= 0 1))

Ran 1 tests containing 1 assertions.
1 failures, 0 errors.
nil
```


## License
Copyright © 2016 Timothy Pote

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
