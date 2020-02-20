(ns bulmaBook.pages.register
  (:require [bulmaBook.components.form :as form]
            [reagent.session :as session]
            [bulmaBook.data :as data]
            [bulmaBook.utils :refer [session-path value->keyword]]))

(defn do-registration []
  (let [reg (session/get :register)
        email-key (value->keyword (:email reg))]
    (if (not (contains? (session/get :users) email-key))
      (do
        (session/assoc-in! [:users email-key] reg)
        (session/remove! :register)
        (session/assoc-in! (session-path data/navbar-id) :login))
      (println "Registration failed: Email already exists"))))

(defn register []
  [:section
   [:div.container
    [:div.columns.is-centered
     [:div.column.is-6-tablet.is-7-desktop.is-5-widescreen
      [:form.box
       [:h2.title.is-2 "Register Account"]
       [form/horizontal-field "Email"
        [[form/input :email :register.email :control-class "has-icons-left"
          :icon {:name "fa-envelope" :position :left :icon-class "is-small"}
          :placeholder "e.g. alexjohnson@example.com" :required true]]]
       [form/horizontal-field "Password"
        [[form/input :password :register.password
          :icon {:name "fa-lock" :position :left :icon-class "is-small"}
          :placeholder "secret" :required true]]]
       [form/horizontal-field "Name"
        [[form/input :text :register.first-name :required true
          :placeholder "First name"]
         [form/input :text :register.last-name :required true
          :placeholder "Last name"]]]
       [form/button "Login" do-registration
        :button-class "is-success"]]]]]] )
