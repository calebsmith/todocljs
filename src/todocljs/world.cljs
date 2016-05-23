(ns todocljs.world)

(defonce world (atom
                {:item-filter :all
                 :items []}))

(defn update-world!
  [update-fn & [arg]]
  (swap! world update-fn arg))
