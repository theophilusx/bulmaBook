(ns bulmaBook.pages.login
  (:require [theophilusx.yorick.input :as inputs]
            [bulmaBook.data :as data]
            [theophilusx.yorick.utils :refer [spath value->keyword]]
            [theophilusx.yorick.store :as store]
            [theophilusx.yorick.icon :as icons]
            [reagent.core :as r]))


(defn do-login [place]
  (let [email-key (value->keyword (store/get-in place [:email]))
        user-profile (store/get-in store/global-state [:users email-key])]
    (if (= (store/get-in place [:password]) (:password user-profile))
      (do
        (store/assoc-in! store/global-state [:session :user]
                         {:name (str
                                 (:first-name user-profile) " "
                                 (:last-name user-profile))
                          :email (:email user-profile)})
        (store/clear! place)
        (store/assoc-in! store/global-state (spath data/navbar-id) :home))
      (println "Bad user or login password"))))

(defn login-form []
  (let [doc (r/atom {})]
    (fn []
      [:form.box
       [inputs/field [:img {:src "images/logo-bis.png" :width "1627"}]
        :classes {:field "has-text-centered"}]
       [inputs/input-field "Email" :email :email
        :icon-data (icons/deficon "fas fa-envelope" :position :left :size :small)
        :attrs {:placeholder "e.g. alexjohnson@example.com"
                :required true}
        :model doc]
       [inputs/input-field "Password" :password :password
        :icon-data (icons/deficon "fas fa-lock" :position :left :size :small)
        :attrs {:placeholder "secret"
                :required true}
        :model doc]
       [inputs/checkbox "Remember me" :remember :model doc]
       [inputs/button "Login" #(do-login doc)
        :classes {:button "is-success"}]])))

(defn login []
  [:section.hero.is-primary.is-fullheight
   [:div.hero-body
    [:div.container
     [:div.columns.is-centered
      [:div.column.is-5-tablet.is-4-desktop.is-3-widescreen
       [login-form]]]]]])
