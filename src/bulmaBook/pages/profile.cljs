(ns bulmaBook.pages.profile
  (:require [bulmaBook.components.form :as form]
            [bulmaBook.utils :refer [value->keyword]]
            [reagent.session :as session]))

(defn profile []
  (let [email-key (value->keyword (session/get-in [:session :user :email]))]
    [:section
     [:div.container
      [:div.columns.is-centered
       [:div.column.is-6-tablet.is-7-desktop.is-5-widescreen
        [:form.box
         [:h2.title.is-2 "User Profile"]
         [form/horizontal-field "Email"
          [[form/field [[:div.content
                         (session/get-in [:session :user :email])]]]]]
         [form/horizontal-field "First Name"
          [[form/editable-field nil
            (keyword (str "users." (name email-key) ".first-name")) :text]]]
         [form/horizontal-field "Last Name"
          [[form/editable-field nil
            (keyword (str "users." (name email-key) ".first-name")) :text]]]]]]]]))
