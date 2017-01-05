(ns user
  (:require [cemerick.piggieback :as pb]
            [cljs.build.api :as build]
            [cljs.repl.node :as node]
            [clojure.java.io :as io]
            [clojure.tools.namespace.repl :as r]
            [com.potetm.tire-iron :as ti]
            [prod-build :as prod]
            [weasel.repl.server :as wserver]
            [weasel.repl.websocket :as weasel])
  (:import (java.io File)))

(def browser-paths ["src/prod"
                    "src/dev/browser"
                    "test"])

(def browser-build-opts
  {:output-to "target/public/js/{{ artifact-id }}.js"
   :output-dir "target/public/js"
   :asset-path "js"
   :source-map true
   :optimizations :none
   :pretty-print true
   :verbose false
   :parallel-build true})

(defn browser-build []
  (build/build (apply build/inputs browser-paths)
               (merge {:main '{{browser-ns}}}
                      browser-build-opts)))

(defn browser-repl []
  (apply pb/cljs-repl (weasel/repl-env :src (apply build/inputs browser-paths)
                                       :working-dir "target/cljs-repl")
         (apply concat (merge {:main '{{browser-ns}}
                               :analyze-path browser-paths
                               :special-fns (ti/special-fns
                                              :source-dirs browser-paths
                                              :state '{{browser-ns}}/state
                                              :before '{{browser-ns}}/stop
                                              :after '{{browser-ns}}/resume)}
                              browser-build-opts))))

(defn stop-repl-server []
  (wserver/stop))


(def node-paths ["src/prod"
                 "src/dev/node"
                 "test"])

(defn node-build []
  (build/build (apply build/inputs node-paths)
               {:output-dir "target/public/js"}))

(defn node-repl []
  (apply pb/cljs-repl (node/repl-env)
         (apply concat (merge {:output-dir "target/public/js"
                               :analyze-path node-paths
                               :special-fns (ti/special-fns
                                              :source-dirs node-paths
                                              :state '{{node-ns}}/state
                                              :before '{{node-ns}}/stop
                                              :after '{{node-ns}}/resume)}
                              browser-build-opts))))

(defn target-index []
  (io/file "target/public/index.html"))

(defn copy-index-to-target
  ([]
   (copy-index-to-target target-index))
  ([^File target-index]
   (let [target-dir (.getParentFile target-index)]
     (.mkdirs target-dir)
     (io/copy (io/file "resources/dev/index.html")
              target-index))))

(defn clean []
  (letfn [(del [f]
            (when (.isDirectory f)
              (doseq [c (.listFiles f)]
                (del c)))
            (io/delete-file f true))]
    (del (io/file "target"))))

(defn fresh-browser-repl []
  (let [idx (target-index)]
    (r/refresh)
    (clean)
    (browser-build)
    (copy-index-to-target idx)
    (println "Navigate to " (.toString (.toURI idx)))
    (browser-repl)))

(defn fresh-node-repl []
  (r/refresh)
  (clean)
  (node-build)
  (node-repl))
