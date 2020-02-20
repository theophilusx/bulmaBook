(ns bulmaBook.components.form
  (:require [bulmaBook.utils :refer [cs session-path value-of]]
            [bulmaBook.components.basic :as basic]
            [reagent.session :as session]
            [reagent.core :as r]))

(defn field [body & {:keys [label field-class label-class]}]
  (into
   [:div {:class (cs "field" field-class)}
    (when label
      [:label {:class (cs "label" label-class)} label])]
   (for [el body]
     el)))

(defn horizontal-field [label body & {:keys [field-class label-class body-class]}]
  [:div {:class (cs "field" "is-horizontal" field-class)}
   [:div {:class (cs "field-label" field-class)}
    [:label {:class (cs "label" label-class)}
     label]]
   (into
    [:div {:class (cs "field-body" body-class
                      (when (> (count body) 1)
                        "has-grouped"))}]
    (for [el body]
      el))])

(defn input-helper [type id class placeholder required]
  [:input {:type (name type)
           :class (cs "input" class)
           :id (name id)
           :name (name id)
           :placeholder placeholder
           :required required
           :on-change #(session/assoc-in! (session-path id) (value-of %))}])

(defn input [type id & {:keys [control-class input-class placeholder required
                               icon]}]
  (if icon
    (into
     [:div {:class (cs "control" control-class
                     (when icon
                       (basic/icon-control-class icon)))}
      (input-helper type id input-class placeholder required)]
     (for [i (basic/icon icon)]
       i))
    [:div {:class (cs "control" control-class)}
     (input-helper type id input-class placeholder required)]))

(defn input-field [label type id & {:keys [field-class label-class control-class
                                           input-class placeholder required
                                           value icon]}]
  (field [[input type id :control-class control-class :input-class input-class
           :placeholder placeholder :required required :value value
           :icon icon]]
         :label label :field-class field-class :label-class label-class))

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

(defn textarea [label session-id & {:keys [field-class control-class label-class
                                          textarea-class placeholder]}]
  [:div {:class (cs "field" field-class)}
   (when label
     [:label {:class (cs "label" label-class)} label])
   [:p {:class (cs "control" control-class)}
    [:textarea {:class (cs "textarea" textarea-class)
                :placeholder placeholder
                :id (name session-id)
                :name (name session-id)
                :on-change #(session/assoc-in!
                             (session-path session-id) (value-of %))}]]])
