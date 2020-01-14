(ns bulmaBook.components.basic
  (:require [bulmaBook.utils :refer [cs]]
            [reagent.session :as session]
            [clojure.string :as string]))

(defn icon [icon-img & {:keys [title classes]}]
  [:div
   [:span {:class (cs "icon" classes)} 
    [:i {:class (cs "fa" icon-img)}]]
   (when title (str " " title))])

(defn button [& {:keys [title href icon-image class id model]
                 :or {href "#"
                      id (keyword (gensym "button-"))
                      model [(keyword (gensym "button-"))]}}]
  (let [state (atom false)]
    (fn []
      [:a {:class (cs "button" class
                      (when @state "is-active"))
           :href href
           :id id
           :on-click (fn []
                       (swap! state not)
                       (session/update-in! (conj model id) not))}
       (if icon-image
         [icon icon-image :title title]
         title)])))

(defn media [body & {:keys [left right id class]}]
  [:article {:class (cs "media" class)
             :id id}
   (when left
     (into
      [:aside {:class (cs "media-left" (:class left))
               :id (:id left)}]
      (for [c (:content left)]
        c)))
   (into
    [:div {:class (cs "media-content" (:class body))
           :id (:id body)}]
    (for [c (:content body)]
      c))
   (when right
     (into
      [:aside {:class (cs "media-right" (:class right))
               :id (:id right)}]
      (for [c (:content right)]
        c)))])

(declare render-map)
(declare render-set)

(defn render-vec
  "Vector display component. Will render vector as an un-ordered list"
  [v]
  (into
   [:ul]
   (for [i v]
     (cond
       (vector? i) [:li [:div.box (render-vec i)]]
       (map? i)    [:li [:div.box (render-map  i)]]
       (set? i)    [:li [:div.box (render-set i)]]
       :default    [:li (str i)]))))

(defn render-set [s]
  [:div.box
   (str "(" (string/join ", " s) ")")])

(defn render-map [m]
  (into
   [:table.table]
   (for [k (keys m)]
     (cond
       (map? (k m)) [:tr
                     [:td [:strong (str k)]]
                     [:td (render-map (k m))]]
       (set? (k m)) [:tr
                     [:td [:strong (str k)]]
                     [:td (str (k m))]]
       (vector? (k m)) [:tr
                        [:td [:string (str k)]]
                        [:td (render-vec (k m))]]
       :default [:tr
                 [:td [:strong (str k)]]
                 [:td (str (k m))]]))))


