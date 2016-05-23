(ns todocljs.data)

(defn update-world!
  [update-fn arg]
  (swap! world update-fn arg))

(defn add-item
  [state text]
  (update state :items
          (fn [items]
            (conj items {:id (random-uuid) :text text}))))

(defn set-filter
  "Given an application state, set the current filter based on the URL
  token"
  [state token]
  (assoc state :item-filter (case token
                              "/active"    :active
                              "/completed" :completed
                              :all)))

(defn destroy
  "Given an application state, destroy the item with the specified ID"
  [state id]
  (update-in state [:items]
             (fn [items]
               (remove #(= id (:id %)) items))))

(defn toggle
  "Given an application state, toggle the completion status of the
  item with the specified ID"
  [state id]
  (update-in state [:items]
             (fn [items]
               (map (fn [item]
                      (if (= id (:id item))
                        (assoc item :completed
                               (not (:completed item)))
                        item))
                    items))))

(defn clear-completed
  "Given an application state, remove all completed items."
  [state]
  (update-in state [:items]
             (fn [items]
               (remove #(:completed %) items))))

(defn start-edit
  "Given an application state, start editing the specified item."
  [state id]
  (update-in state [:items]
             (fn [items]
               (map (fn [item]
                      (if (= id (:id item))
                        (assoc item :editing true)
                        item))
                    items))))

(defn complete-edit
  "Given an application state, start editing the specified item."
  [state [id txt]]
  (update-in state [:items]
             (fn [items]
               (map (fn [item]
                      (if (= id (:id item))
                        (assoc item :editing false :text txt)
                        item))
                    items))))
