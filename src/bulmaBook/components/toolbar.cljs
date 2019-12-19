(ns bulmaBook.components.toolbar
  (:require [bulmaBook.utils :as utils]))

(defn defitem [& {:keys [type class content]
                  :or {type :div}}]
  {:type type
   :class class
   :content content})

(defn toolbar [& {:keys [left-items right-items class]}]
  [:nav {:class (utils/cs "level" class)}
   (into
    [:div.level-left]
    (for [i left-items]
      [(:type i) {:class (utils/cs "level-item" (:class i))}
       (:content i)]))
   (when right-items
     (into
      [:div.level-right]
      (for [i right-items]
        [(:type i) {:class (utils/cs "level-item" (:class i))}
         (:content i)])))])

