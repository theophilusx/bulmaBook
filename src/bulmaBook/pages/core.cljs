(ns bulmaBook.pages.core
  (:require [bulmaBook.pages.home :refer [home-page]]
            [bulmaBook.components.basic :refer [render-map]]
            [reagent.session :as session]))

(defn current-page []
  (condp = (session/get-in [:main-navbar :choice])
    :home [home-page]
    :profile [:div
              [:h2.h2.title "Profile page goes here"]
              [render-map @session/state]]
    :report-bug [:div
                 [:h2.h2.title "Report bug page goes here"]
                 [render-map @session/state]]
    :sign-out [:div
               [:h2.h2.title "Sign Out page goes here"]
               [render-map @session/state]]
    :login [:div
            [:h2.h2.title "Login page goes here"]
            [render-map @session/state]]
    [:div
     [:h2.h2.title "Bad page name: " (session/get-in [:main-navbar :choice])]
     [render-map @session/state]]))
