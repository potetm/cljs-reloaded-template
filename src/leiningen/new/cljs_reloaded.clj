(ns leiningen.new.cljs-reloaded
  (:require [clojure.string :as str]
            [leiningen.new.templates :as lt]
            [leiningen.core.main :as main]))

(def render (lt/renderer "cljs-reloaded"))

(defn cljs-reloaded [full-name & [install-dir]]
  (let [group-id (lt/group-name full-name)
        artifact-id (lt/project-name full-name)
        data {;; lein apparently needs install-dir named :name
              :name (or install-dir artifact-id)
              :full-name full-name
              :group-id group-id
              :artifact-id artifact-id
              :main-ns (lt/sanitize-ns full-name)
              :main-path (lt/name-to-path full-name)
              :year (lt/year)}]
    (main/info "Generating your cljs-reloaded project.")
    (lt/->files data
                [".gitignore" (render ".gitignore")]
                ["README.md" (render "README.md" data)]
                ["project.clj" (render "project.clj" data)]

                ["resources/dev/index.html" (render "dev-index.html" data)]
                ["build/cljs_repl.clj" (render "cljs_repl.clj" data)]
                ["build/prod_build.clj" (render "cljs_prod_build.clj" data)]
                ["src/dev/dev/user.cljs" (render "user.cljs" data)]
                ["test/{{ main-path }}_test.cljs" (render "test.cljs" data)]
                ["resources/prod/index.html" (render "prod-index.html" data)]
                ["src/prod/{{ main-path }}.cljs" (render "main.cljs" data)]

                ["bin/build-cljs" (render "cljs-build.sh" data) :executable true]
                ["bin/cljs-repl" (render "cljs-repl.sh" data) :executable true])))
