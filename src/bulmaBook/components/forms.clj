(ns bulmaBook.components.forms
  (:require [bulmaBook.utils :refer [cs session-path value-of]]
            [reagent.session :as session]))

(defn field [body & {:keys [class]}]
  (into
   [:div {:class (cs "field" class)}]
   (for [el body]
     el)))

(defn horizontal-field [label body & {:keys [field-class label-class]}]
  [:div {:class (cs "field" "is-horizontal" field-class)}
   [:div {:class (cs "field-label" label-class)}
    label]
   (into
    [:div.field-body]
    (for [el body]
      el))])


(defn input [type label id & {:keys [field-class label-class control-class
                                     input-class placeholder icon-class
                                     icon required]}]
  (let [path (session-path id)]
    [:div {:class (cs "field" field-class)}
     [:div {:class (cs "control" control-class)}
      [:label {:class (cs "label" label-class)}
       label]
      (when icon
        [:span {:class (cs "icon" icon-class)}
         [:i {:class (cs icon)}]])
      [:input {:type (name type)
               :class (cs "input" input-class)
               :id (name id)
               :name (name id)
               :placeholder placeholder
               :required required
               :on-change (fn [e]
                            (session/asoc-in! path (value-of e)))}]]]))
