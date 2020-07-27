(ns bulmaBook.pages.register
  (:require [theophilusx.yorick.input :as inputs]
            [theophilusx.yorick.icon :as icons]
            [bulmaBook.data :as data]
            [theophilusx.yorick.store :as store]
            [theophilusx.yorick.utils :refer [spath value->keyword]]
            [reagent.core :as r]))

(defn do-registration [place]
  (let [email-key (value->keyword (:email @place))]
    (if-not (contains? (store/get store/global-state :users) email-key)
      (do
        (store/assoc-in! store/global-state [:users email-key] @place)
        (store/clear! place)
        (store/assoc-in! store/global-state (spath data/navbar-id) :login))
      (println "Registration failed: Email already exists"))))

(defn register-form []
  (let [doc (r/atom {})]
    (fn []
      [:form.box
       [inputs/horizontal-field "Email"
        [inputs/input :email :email :classes {:control "has-icons-left"}
         :icon-data (icons/deficon "fa-envelope" :position :left
                      :icon-class "is-small")
         :placeholder "e.g. alexjohnson@example.com" :required true
         :model doc]]
       [inputs/horizontal-field "Password"
        [inputs/input :password :password
         :icon-data (icons/deficon "fa-lock" :position :left
                      :icon-class "is-small")
         :placeholder "secret" :required true :model doc]]
       [inputs/horizontal-field "Name"
        [:<>
         [inputs/input :text :first-name :required true
          :placeholder "First name" :model doc]
         [inputs/input :text :last-name :required true
          :placeholder "Last name" :model doc]]]
       [inputs/button "Register" #(do-registration doc)
        :classes {:button "is-success"}]])))

(defn register []
  [:section
   [:div.container
    [:div.columns.is-centered
     [:div.column.is-6-tablet.is-7-desktop.is-5-widescreen
      [register-form]]]]] )
