(defproject {{full-name}} "0.1.0-SNAPSHOT"
  :description "TODO"
  :url "TODO"
  :license {:name "TODO: Choose a license"
            :url "http://choosealicense.com/"}
  :dependencies [[com.stuartsierra/component "0.3.2"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.293"]]
  :resource-paths ["resources/prod"]
  :source-paths ["src/prod"]
  :test-paths ["test"]
  :profiles {:cljs-dev {:source-paths ["build"
                                       "src/prod"
                                       "src/dev"
                                       "test"]
                        :dependencies [[com.cemerick/piggieback "0.2.1"]
                                       [com.potetm/tire-iron "0.1.0"]
                                       [weasel "0.7.0"]]
                        :repl-options {:init-ns cljs-repl
                                       :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}})
