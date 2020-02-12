(ns bulmaBook.pages.login
  (:require [bulmaBook.components.form :as form]
            [bulmaBook.data :as data]
            [bulmaBook.utils :refer [session-path]]
            [reagent.session :as session]))


(defn do-login []
  (let [email (session/get-in [:login :email])
        user-profile (session/get-in [:users email])]
    (println (str "Email: " email))
    (println (str "User profile: " user-profile))
    (if (= (session/get-in [:login :password]) (:password user-profile))
      (do
        (session/assoc-in! [:session :user] {:name (str
                                                    (:first-name user-profile) " "
                                                    (:last-name user-profile))
                                             :email (:email user-profile)})
        (session/remove! :login)
        (session/assoc-in! (session-path data/navbar-id) :home))
      (println "Bad user or login password"))))

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
                               (do-login))
         :button-class "is-success"]]]]]]])
