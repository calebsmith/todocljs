(ns todocljs.history
  (:require [goog.History :as ghistory]
            [goog.events :as gevents]
            [todocljs.world :refer [world update-world!]]
            [todocljs.data :as data]))

(defn navigate!
  [token]
  (update-world! data/set-filter token))

(defn- init-history
  "Initializes and returns a goog.History JS object. Its initial value is set to
  the current token, or / if none is available. Registers a listener that calls
  `navigate!` with the new token when a navigation event occurs."
  []
  (let [history (goog.History.)]
    (gevents/listen history ghistory/EventType.NAVIGATE
                    (fn [evt]
                      (let [token (.-token evt)
                            token (if (empty? token) "/" token)]
                        (.setToken history token)
                        (navigate! token))))
    history))

(defn init
  "Top level interface for bootstrapping the history/navigation layer."
  []
  (let [history (init-history)
        initial-path (.getToken history)]
    (.setEnabled history true)))

