(ns bulmaBook.pages.core
  (:require [bulmaBook.pages.home :refer [home-page]]
            [bulmaBook.utils :refer [session-path]]
            [bulmaBook.pages.login :refer [login]]
            [bulmaBook.pages.register :refer [register]]
            [bulmaBook.pages.profile :refer [profile]]
            [bulmaBook.components.basic :refer [render-map]]
            [reagent.session :as session]
            [bulmaBook.data :as data]))

(defn current-page []
  (condp = (session/get-in (session-path data/navbar-id))
    :home [home-page]
    :profile [:div
              [profile]
              [render-map @session/state]]
    :report-bug [:div
                 [:h2.h2.title "Report bug page goes here"]
                 [render-map @session/state]]
    :sign-out [:div
               [:h2.h2.title "Sign Out page goes here"]
               [render-map @session/state]]
    :login [:div
            [login]
            [render-map @session/state]]
    :register [:div
               [register]
               [render-map @session/state]]
    [:div
     [:h2.h2.title "Bad page name: " (session/get-in
                                      (session-path data/navbar-id))]
     [render-map @session/state]]))
