(ns {{browser-ns}}
  (:require [cljs.test :as test]
            [com.stuartsierra.component :as component]
            [weasel.repl :as repl]
            [{{main-ns}}-test :as tc]
            [{{main-ns}} :as core]))

(def system nil)

(defonce state
  {:repl-conn (do (repl/connect "ws://localhost:9001")
                  repl/ws-connection)
   :last-app-state (atom nil)})

(defn dev-system []
  (component/system-map
    :app-state (atom nil)))

(defn init []
  (set! system (dev-system)))

(defn with-prev-state []
  (set! system (merge (dev-system)
                      (when-some [las (:last-app-state state)]
                        {:app-state las}))))

(defn start []
  (set! system (component/start system)))

(defn stop []
  (when system
    (set! system (component/stop system)))
  (set! state (merge state
                     (when-some [as (:app-state system)]
                       {:last-app-state as}))))

(defn new-start []
  (init)
  (start))

(defn resume []
  (with-prev-state)
  (start))

(defn run-tests []
  (test/run-tests '{{main-ns}}-test))

(defn ^:export main []
  (new-start))
