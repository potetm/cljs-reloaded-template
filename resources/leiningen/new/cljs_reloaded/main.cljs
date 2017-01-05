(ns {{main-ns}}
  (:require [com.stuartsierra.component :as component]))

(defn system []
  (component/system-map))

(defn ^:export main []
  (component/start (system))
  (js/console.log "Started!"))
