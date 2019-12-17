(ns bulmaBook.basic
  (:require [bulmaBook.utils :refer [cs]]))

(defn icon [icon-img & {:keys [title classes]}]
  [:div
   [:span {:class (cs "icon" classes)} 
    [:i {:class (cs "fa" icon-img)}]]
   (when title (str " " title))])

(defn button [& {:keys [title href icon-image class id :on-click]
                 :or [:href "#"
                      :id (keyword (gensym "button-"))
                      :on-click (fn []
                                  (session/update-in! [:buttons id :active] not))]}]
  [:a {:class (cs "button" class)
       :href href
       :id id
       :on-click on-click}
   (if icon-image
     [icon icon-image :title title]
     title)])
