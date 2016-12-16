(ns cljs-repl
  (:require [cemerick.piggieback :as pb]
            [cljs.build.api :as build]
            [clojure.java.io :as io]
            [com.potetm.tire-iron :as ti]
            [prod-build :as prod]
            [weasel.repl.server :as wserver]
            [weasel.repl.websocket :as weasel])
  (:import (java.io File)))

(def cljs-paths ["src/prod"
                 "src/dev"
                 "test"])

(def dev-build-opts {:main 'dev.user
                     :output-to "target/public/js/{{ artifact-id }}.js"
                     :output-dir "target/public/js"
                     :asset-path "js"
                     :source-map true
                     :optimizations :none
                     :pretty-print true
                     :verbose false
                     :parallel-build true})

(defn dev-build []
  (build/build (apply build/inputs cljs-paths)
               dev-build-opts))

(defn repl []
  ;; the lesson here is to provide a map-based api for your named-params api
  (apply pb/cljs-repl (weasel/repl-env :src (apply build/inputs cljs-paths)
                                       :working-dir "target/cljs-repl")
         (apply concat (merge {:analyze-path cljs-paths
                               :special-fns (ti/special-fns
                                              :source-dirs cljs-paths
                                              :state 'dev.user/state
                                              :before 'dev.user/stop
                                              :after 'dev.user/resume)}
                              dev-build-opts))))

(defn stop-repl-server []
  (wserver/stop))

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

(defn fresh-repl []
  (let [idx (target-index)]
    (clean)
    (dev-build)
    (copy-index-to-target idx)
    (println "Navigate to " (.toString (.toURI idx)))
    (repl)))
