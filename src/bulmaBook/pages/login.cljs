(ns bulmaBook.pages.login
  (:require [bulmaBook.components.form :as form]
            [bulmaBook.data :as data]
            [bulmaBook.utils :refer [spath value->keyword]]
            [reagent.session :as session]
            [bulmaBook.components.icons :as icons]))


(defn do-login []
  (let [email-key (value->keyword (session/get-in [:login :email]))
        user-profile (session/get-in [:users email-key])]
    (if (= (session/get-in [:login :password]) (:password user-profile))
      (do
        (session/assoc-in! [:session :user] {:name (str
                                                    (:first-name user-profile) " "
                                                    (:last-name user-profile))
                                             :email (:email user-profile)})
        (session/remove! :login)
        (session/assoc-in! (spath data/navbar-id) :home))
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
         :classes {:field "has-text-centered"}]
        [form/input-field "Email" :email :login.email
         :icon-data (icons/deficon "fa-envelope" :position :left :size :small)
         :placeholder "e.g. alexjohnson@example.com"
         :required true]
        [form/input-field "Password" :password :login.password
         :icon-data (icons/deficon "fa-lock" :position :left :size :small)
         :placeholder "secret" :required true]
        [form/checkbox "Remember me" :login.remember]
        [form/button "Login" do-login :classes {:button "is-success"}]]]]]]])
