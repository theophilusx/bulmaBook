(ns bulmaBook.components.cards
  (:require [bulmaBook.components.icons :as icons]))

(defn card-header [title & {:keys [icon icon-action class]}]
  [:header.card-header {:class [class]}
   [:p.card-header-title title]
   (when icon
     [:a.card-header-icon {:href "#"
                           :on-click (when (fn? icon-action)
                                       #(icon-action %))}
      [icons/icon-component icon]])])

(defn card-footer [items & {:keys [classes]}]
  (into
   [:footer.card-footer {:class [(:footer classes)]}]
   (for [i items]
     [:p.card-footer-item {:class [(:footer-item classes)]}
      i])))

(defn card [body & {:keys [header footer classes]}]
  [:div.card {:class [(:card classes)]}
   (when header
     header)
   [:div.card-content {:class [(:card-content classes)]}
    body]
   (when footer
     footer)])
