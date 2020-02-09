(ns bulmaBook.pages.core
  (:require [bulmaBook.pages.home :refer [home-page]]
            [bulmaBook.components.basic :refer [render-map]]
            [reagent.session :as session]))

(defn current-page []
  (condp = (session/get-in [:main-navbar :choice])
    :home [home-page]
    :profile [:div
              [:h3.h3 "Profile page goes here"]
              [render-map @session/state]]
    :report-bug [:div
                 [:h3.h3 "Report bug page goes here"]
                 [render-map @session/state]]
    :sign-out [:div
               [:h3.h3 "Sign Out page goes here"]
               [render-map @session/state]]
    :login [:div
            [:h3.h3 "Login page goes here"]
            [render-map @session/state]]
    [:div
     [:h3.h3 "Bad page name: " (session/get-in [:main-navbar :choice])]
     [render-map @session/state]]))
