(ns bulmaBook.pages.register
  (:require [bulmaBook.components.form :as form]
            [reagent.session :as session]
            [bulmaBook.data :as data]
            [bulmaBook.utils :refer [session-path]]))

(defn register []
  [:section
   [:div.container
    [:h2.title.is-2 "Register Account"]
    [:div.columns
     [:div.column.is-5-tablet.is-4-desktop.is-3-widescreen
      [:form.box
       [form/horizontal-field "Email"
        [[form/input :email "" :register.email :control-class "has-icons-left"
          :icon "fa fa-envelope" :placeholder "e.g. alexjohnson@example.com"
          :icon-class "is-small is-left" :required true]]]
       [form/horizontal-field "Password"
        [[form/input :password "" :register.password
          :control-class "has-icons-left" :icon-class "is-small is-left"
          :icon "fa fa-lock" :placeholder "secret" :required true]]]
       [form/horizontal-field "Name"
        [[form/input :text "" :register.fname :required true]
         [form/input :text "" :register.lname :required true]]]
       [form/button "Login" (fn []
                              (println "Do login process")
                              (session/assoc-in! (session-path data/navbar-id) :home))
        :button-class "is-success"]]]]]] )
