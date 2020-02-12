(ns bulmaBook.pages.login
  (:require [bulmaBook.components.form :as form]
            [bulmaBook.data :as data]
            [bulmaBook.utils :refer [session-path]]
            [reagent.session :as session]))

(defn login
  "Initial go at the login component. Lots more to do!"
  []
  [:section.hero.is-primary.is-fullheight
   [:div.hero-body
    [:div.container
     [:div.columns.is-centered
      [:div.column.is-5-tablet.is-4-desktop.is-3-widescreen
       [:form.box
        [form/field [[:img {:src "images/logo-bis.png" :width "1627"}]]
         :field-class "has-text-centered"]
        [form/input :email "Email" :login.email :control-class "has-icons-left"
         :icon "fa fa-envelope" :placeholder "e.g. alexjohnson@example.com"
         :icon-class "is-small is-left" :required true]
        [form/input :password "Password" :login.password
         :control-class "has-icons-left" :icon-class "is-small is-left"
         :icon "fa fa-lock" :placeholder "secret" :required true]
        [form/checkbox "Remember me" :login.remember]
        [form/button "Login" (fn []
                               (println "Do login process")
                               (session/assoc-in! (session-path data/navbar-id) :home))
         :button-class "is-success"]
        ]]]]]])
