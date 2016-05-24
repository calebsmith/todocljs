(ns todocljs.components
  (:require [quiescent.core :as q :include-macros true]
            [quiescent.dom :as d]
            [todocljs.world :refer [update-world! world]]
            [todocljs.data :as data]))

(defn class-name
  "Convenience function for creating class names from sets. Nils will
  not be included."
  [classes]
  (apply str (interpose " " (map identity classes))))

(defn hidden?
  "Given an item and the current application filter status, return
  true if the item should be hidden."
  [item item-filter]
  (or (and (= item-filter :active) (:completed item))
      (and (= item-filter :completed) (not (:completed item)))))

(defn enter-key?
  "Return true if an event was the enter key"
  [evt]
  (= 13 (.-keyCode evt)))

(q/defcomponent AutoFocusInput
  :on-render (fn [node item _]
               (when (:editing item)
                 (.focus node)))
  [item]
  (d/input {:className "edit"
            :defaultValue (:text item)
            :onKeyDown (fn [evt] (when (enter-key? evt)
                                  (.blur (.-target evt))))
            :onDoubleClick (fn [evt] (.blur (.-target evt)))
            :onBlur (fn [evt]
                      (let [v (.-value (.-target evt))]
                        (update-world! data/complete-edit [(:id item) v])))}))

(q/defcomponent Item
  "An item in the todo list"
  :keyfn (comp :id first)
  [[{:keys [id text] :as item} item-filter]]
  (let [done (boolean (:completed item))]
    (d/li {:className (class-name #{(when done "completed")
                                    (when (hidden? item item-filter) "hidden")
                                    (when (:editing item) "editing")})}
          (d/div {:className "view"}
                 (d/input {:className "toggle"
                           :type      "checkbox"
                           :checked   done
                           :readOnly  true
                           :onClick #(update-world! data/toggle id)})
                 (d/label {:onDoubleClick #(update-world! data/start-edit id)} text)
                 (d/button {:className "destroy"
                            :onClick #(update-world! data/destroy id)}))
          (AutoFocusInput item))))

(q/defcomponent TodoList
  "The primary todo list"
  [{:keys [item-filter items]}state]
  (apply d/ul {:id "todo-list"}
         (map #(Item [% item-filter]) items)))

(q/defcomponent Header
  "The page's header, which includes the primary input"
  [state]
  (d/header {:id "header"}
            (d/h1 {} "todos")
            (d/input {:id "new-todo"
                      :placeholder "What needs to be done?"
                      :onKeyDown (fn [evt]
                                   (when (enter-key? evt)
                                     (let [v (.-value (.-target evt))]
                                       (when (not (empty? v))
                                         (update-world! data/add-item v)
                                         (set! (.-value (.-target evt)) "")))))
                      :autoFocus true})))

(q/defcomponent FilterItem
  "A filtering button"
  [selected? label href]
  (d/li {} (d/a {:className (when selected? "selected")
                 :href href}
                label)))

(q/defcomponent Filters
  "Buttons to filter the list"
  [item-filter]
  (d/ul {:id "filters"}
        (FilterItem (= :all item-filter) "All" "#/")
        (FilterItem (= :active item-filter) "Active" "#/active")
        (FilterItem (= :completed item-filter) "Completed" "#/completed")))

(q/defcomponent Footer
  "The footer at the bottom of the list"
  [state]
  (let [completed (count (filter :completed (:items state)))
        left (- (count (:items state)) completed)]
    (d/footer {:id "footer"}
              (d/span {:id "todo-count"}
                      (d/strong {} (str left " items left")))
              (Filters (:item-filter state))
              (when (< 0 completed)
                (d/button {:id "clear-completed"
                           :onClick #(update-world! data/clear-completed)}
                          (str "Clear completed (" completed ")"))))))

(q/defcomponent Body
  [state]
  (d/section {:id "main"}
             (d/input {:id "toggle-all"
                       :type "checkbox"
                       :readOnly  true
                       :checked true})
             (d/label {:htmlFor "toggle-all"}
                      "Mark all as complete")
             (TodoList state)))

(q/defcomponent Root
  "The root of the application"
  [state]
  (d/div {}
         (Header nil)
         (Body state)
         (Footer state)))


(comment

  (update-world! data/add-item "Give Talk at TriClojure")

  (:items @world)

  )
