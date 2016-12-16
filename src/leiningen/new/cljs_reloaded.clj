(ns leiningen.new.cljs-reloaded
  (:require [clojure.string :as str]
            [leiningen.new.templates :as lt]
            [leiningen.core.main :as main]))

(def render (lt/renderer "cljs-reloaded"))

(defn cljs-reloaded [full-name & [install-dir]]
  (let [group-id (lt/group-name full-name)
        artifact-id (lt/project-name full-name)
        browser-ns 'browser.user
        node-ns 'node.user
        browser-ns-path (str (lt/name-to-path browser-ns) ".cljs")
        node-ns-path (str (lt/name-to-path node-ns) ".cljs")
        data {;; lein apparently needs install-dir named :name
              :name (or install-dir artifact-id)
              :full-name full-name
              :group-id group-id
              :artifact-id artifact-id
              :main-ns (lt/sanitize-ns full-name)
              :main-path (lt/name-to-path full-name)
              :year (lt/year)
              :browser-ns browser-ns
              :node-ns node-ns}]
    (main/info "Generating your cljs-reloaded project.")
    (lt/->files data
                [".gitignore" (render ".gitignore")]
                ["README.md" (render "README.md" data)]
                ["project.clj" (render "project.clj" data)]

                ["resources/dev/index.html" (render "dev-index.html" data)]
                ["build/user.clj" (render "user.clj" data)]
                ["build/prod_build.clj" (render "prod_build.clj" data)]
                [(str "src/dev/" browser-ns-path) (render "browser.cljs" data)]
                [(str "src/dev/" node-ns-path) (render "node.cljs" data)]
                ["test/{{ main-path }}_test.cljs" (render "test.cljs" data)]
                ["resources/prod/index.html" (render "prod-index.html" data)]
                ["src/prod/{{ main-path }}.cljs" (render "main.cljs" data)]

                ["bin/build-cljs" (render "build.sh" data) :executable true])))
