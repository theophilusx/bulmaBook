(ns bulmaBook.pages.profile
  (:require [bulmaBook.components.inputs :as inputs]
            [bulmaBook.utils :refer [value->keyword]]
            [bulmaBook.store :as store]
            [reagent.core :as r]))

(defn profile-edit-form []
  (let [doc (r/atom {})
        email-key (value->keyword (store/get-in
                                   store/global-state [:session :user :email]))]
    (fn []
      [:form.box
       [inputs/horizontal-field "Email"
        [[inputs/field [[:div.content
                       (store/get-in store/global-state
                                     [:session :user :email])]]]]]
       [inputs/horizontal-field "First Name"
        [[inputs/editable-field nil
          (keyword (str "users." (name email-key) ".first-name")) :text
          :model doc]]]
       [inputs/horizontal-field "Last Name"
        [[inputs/editable-field nil
          (keyword (str "users." (name email-key) ".first-name")) :text
          :model doc]]]])))

(defn profile []
  [:section
   [:div.container
    [:div.columns.is-centered
     [:div.column.is-6-tablet.is-7-desktop.is-5-widescreen
      [profile-edit-form]]]]])
