(ns bulmaBook.components.vertical-menu
  (:require [bulmaBook.utils :refer [cs]]
            [bulmaBook.components.basic :refer [icon]]
            [reagent.core :refer [atom]]
            [reagent.session :as session]))


(def menu-state (atom {}))

(defn defitem [& {:keys [type title href icon-img id items class]
                  :or {type :item
                       href "#"
                       id (keyword (gensym "menu-item-"))}}]
  {:type type
   :title title
   :href href
   :icon-img icon-img
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
   [:a {:class (cs (:class i)
                   (when (is-active? model (:id i)) "is-active"))
        :href  (:href i)
        :id    (:id i)
        :on-click (fn []
                    (set-active model (:id i)))}
    (if (:icon-img i)
      [icon (:icon-img i) :title (:title i)]
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
    [:nav {:class (cs "menu" (:class item))}
     (-make-menu item model)]))
