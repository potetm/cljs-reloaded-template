(ns prod-build
  (:require [cljs.build.api :as build]
            [clojure.java.io :as io]))

(defn copy-index-to-target []
  (let [target-index (io/file "target/public/index.html")
        target-dir (.getParentFile target-index)]
    (.mkdirs target-dir)
    (io/copy (io/file "resources/prod/index.html")
             target-index)))

(defn build []
  (copy-index-to-target)
  (build/build (build/inputs "src/prod/cljs")
               {:main '{{main-ns}}
                :output-to "target/public/js/{{ artifact-id }}.js"
                :output-dir "target/public/js"
                :asset-path "js"
                :source-map "target/public/js/source-map.js"
                :optimizations :advanced
                :pretty-print false
                :verbose true
                :parallel-build true}))
