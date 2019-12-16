(ns bulmaBook.basic
  (:require [bulmaBook.utils :refer [cs]]))

(defn icon [& {:keys [icon-image title classes]}]
  [:div
   [:span (cs "icon" classes) 
    [:i {:class (cs "fa" icon-image)}
     (when title (str " " title))]]])
