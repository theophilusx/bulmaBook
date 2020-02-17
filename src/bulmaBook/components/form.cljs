(ns bulmaBook.components.form
  (:require [bulmaBook.utils :refer [cs session-path value-of]]
            [reagent.session :as session]
            [reagent.core :as r]))

(defn field [body & {:keys [class]}]
  (into
   [:div {:class (cs "field" class)}]
   (for [el body]
     el)))

(defn horizontal-field [label body & {:keys [field-class label-class]}]
  [:div {:class (cs "field" "is-horizontal" field-class)}
   [:div {:class (cs "field-label" label-class)}
    [:label {:class (cs "label" label-class)}
     label]]
   (into
    [:div {:class (cs "field-body"
                      (when (> (count body) 1)
                        "has-grouped"))}]
    (for [el body]
      el))])


(defn input [type label id & {:keys [field-class label-class control-class
                                     input-class placeholder icon-class
                                     icon required]}]
  (let [path (session-path id)]
    [:div {:class (cs "field" field-class)}
     (when label
       [:label {:class (cs "label" label-class)} label])
     [:p {:class (cs "control" control-class)}
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
    [:a {:class (cs "button" button-class)
              :type "button"
              :on-click action}
     title]]])

(defn do-save [s]
  (session/assoc-in! (session-path (:session-id @s)) (:value @s))
  (swap! s assoc :editing false))

(defn do-reset [s]
  (swap! s assoc :value (session/get-in (session-path (:session-id @s))))
  (swap! s assoc :editing false))

(defn editable-field [label session-id type & {:keys [field-class label-class
                                               control-class input-class]}]
  (let [state (r/atom {:value (session/get-in (session-path session-id))
                       :session-id session-id
                       :editing false})]
    (fn []
      (println (str "editable-field state: " @state))
      (if (get @state :editing)
        [:div {:class (cs "field" "is-grouped" field-class)}
         (when label
           [:label {:class (cs "label" label-class)} label])
         [:p {:class (cs "control" control-class)}
          [:input {:type (name type)
                   :class (cs "input" "is-small" input-class)
                   :value (:value @state)
                   :id (name (:session-id @state))
                   :name (name (:session-id @state))
                   :on-change (fn [e]
                                (swap! state assoc :value (value-of e)))}]]
         [:p {:class (cs "control")}
          [button "Save" #(do-save state) :button-class "is-primary is-small"]]
         [:p {:class (cs "control")}
          [button "Reset" #(do-reset state) :button-class "is-small"]]]
        [:div {:class (cs "field" field-class)}
         (when label
           [:label {:class (cs "label" label-class)} label])
         [:p {:class (cs "control" control-class)}
          (str (:value @state) " ")
          [:span {:class (cs "icon")
                  :on-click #(swap! state assoc :editing true)}
           [:i {:class (cs "fa" "fa-pencil")}]]]]))))
