(ns bulmaBook.vertical-menu
  (:require [reagent.core :refer [atom]]
            [reagent.session :as session]
            [bulmaBook.utils :as utils]))


(def menu-state (atom {}))

(defn defitem [& {:keys [type title href icon id items class]
                  :or {type :item
                       href "#"
                       id (keyword (gensym "menu-item-"))}}]
  {:type type
   :title title
   :href href
   :icon icon
   :id id
   :items items
   :class class})

(defn is-active? [model id]
  (if (= (get-in @menu-state [model :active-menu]) id)
    true
    false))

(defn set-active [model id]
  (swap! menu-state assoc-in [model :active-menu] id)
  (session/assoc-in! [model :choice] id))

(defn -make-item [i model]
  [:li 
   [:a {:class (utils/cs (:class i)
                         (when (is-active? model (:id i))
                           "is-active"))
        :href  (:href i)
        :id    (:id i)
        :on-click (fn []
                    (set-active model (:id i)))}
    (if (:icon i)
      [:div
       [:span.icon [:i {:class (utils/cs "fa" (:icon i))}]]
       (str " " (:title i))]
      (:title i))]])

(defn -make-menu [m model]
  [:div
   [:p.menu-label (:title m)]
   (into
    [:ul.menu-list]
    (for [i (:items m)]
      (if (= (:type i) :item)
        (-make-item i model)
        (-make-menu i model))))])

(defn menu [model item]
  (swap! menu-state assoc model {:active-menu nil})
  (fn []
    [:nav {:class (utils/cs "menu" (:class item))}
     (-make-menu item model)]))
