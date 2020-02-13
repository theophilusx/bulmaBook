(ns bulmaBook.pages.profile
  (:require [bulmaBook.components.form :as form]
            [reagent.session :as session]))

(defn profile []
  (let [email (session/get-in [:session :user :email])
        user-profile (session/get-in [:users email])]
    [:section
     [:div.container
      [:div.columns.is-centered
       [:div.column.is-6-tablet.is-7-desktop.is-5-widescreen
        [:form.box
         [:h2.title.is-2 "User Profile"]
         [form/horizontal-field "Email"
          [[form/field [[:div.content email]]]]]
         [form/horizontal-field "Name"
          [[form/field [[:div.content (:first-name user-profile)]]]
           [form/field [[:div.content (:last-name user-profile)]]]]]]]]]]))
