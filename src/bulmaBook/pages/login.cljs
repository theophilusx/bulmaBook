(ns bulmaBook.pages.login
  (:require [bulmaBook.components.form :as form]))

(defn login-component
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
         :control-class "has-icon-left" :icon-class "is-small is-left"
         :icon "fa fa-lock" :placeholder "secret" :required true]
        [form/input :checkbox "Remember me" :login.remember]
        [form/field [[:button.button.is-success "Login"]]]]]]]]])
