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
        [[form/input :email nil :register.email :control-class "has-icons-left"
          :icon "fa fa-envelope" :placeholder "e.g. alexjohnson@example.com"
          :icon-class "is-small is-left" :required true]]]
       [form/horizontal-field "Password"
        [[form/input :password nil :register.password
          :control-class "has-icons-left" :icon-class "is-small is-left"
          :icon "fa fa-lock" :placeholder "secret" :required true]]]
       [form/horizontal-field "Name"
        [[form/input :text nil :register.first-name :required true
          :placeholder "First name"]
         [form/input :text nil :register.last-name :required true
          :placeholder "Last name"]]]
       [form/button "Login" (fn []
                              (println "Do register process")
                              (do-registration))
        :button-class "is-success"]]]]]] )
