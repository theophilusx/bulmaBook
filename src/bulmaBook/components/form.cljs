(ns bulmaBook.components.form
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
     [:label {:class (cs "label" label-class)} label]
     [:div {:class (cs "control" control-class)}
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
                            (session/assoc-in! path (value-of e)))}]]]))

(defn checkbox [label id & {:keys [field-class label-class control-class]}]
  (let [path (session-path id)]
    [:div {:class (cs "field" field-class)}
     [:div {:class (cs "control" control-class)}
      [:label {:class (cs "checkbox" label-class)}
       [:input {:type "checkbox"
                :id (name id)
                :name (name id)
                :on-change (fn []
                             (session/update-in! path not))}]
       (str " " label)]]]))

(defn button [title action & {:keys [field-class control-class button-class]}]
  [:div {:class (cs "field" field-class)}
   [:div {:class (cs "control" control-class)}
    [:button {:class (cs "button" button-class)
              :type "button"
              :on-click action}
     title]]])
