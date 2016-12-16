(ns dev.cljs
  (:require [cljs.test :as test]
            [com.stuartsierra.component :as component]
            [weasel.repl :as repl]
            [{{main-ns}}-test :as tc]
            [{{main-ns}} :as core]))

(enable-console-print!)

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
  (set! system (assoc (dev-system)
                      :app-state (:last-app-state state))))

(defn start []
  (set! system (component/start system)))

(defn stop []
  (when system
    (set! system (component/stop system)))
  (set! state (assoc state
                     :last-app-state (:app-state system))))

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
