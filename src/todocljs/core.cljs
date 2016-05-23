(ns todocljs.core
  (:require [quiescent.core :as q :include-macros true]
            [quiescent.dom :as d]
            [todocljs.world :refer [world]]
            [todocljs.components :refer [Root]]))

(enable-console-print!)

(def root-element-id "todoapp")

(defn render
  "Initiate rendering of the application"
  [dom-root-el]
  (do
    (q/render (Root @world) dom-root-el)
    (.requestAnimationFrame js/window #(render dom-root-el))))

(defn ^:export main
  "Application entry point"
  []
  (let [root-el (.getElementById js/document root-element-id)]
    (render root-el)))

(defn on-js-reload [])


(comment

  (:items @world)

  )
