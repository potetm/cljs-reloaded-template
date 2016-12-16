(ns {{main-ns}}
  (:require [com.stuartsierra.component :as component]))

(defn system []
  (component/system-map))

(defn ^:export main []
  (let [s (component/start (system))]
    (try
      (js/console.log "Do things!")
      (finally
        (component/stop s)))))
