(ns bulmaBook.pages.home
  (:require [bulmaBook.components.sidebar :refer [sidebar]]
            [bulmaBook.components.basic :refer [render-map]]
            [bulmaBook.pages.books :refer [books-page]]
            [bulmaBook.data :as data]
            [reagent.session :as session]))

(defn home-page []
  [:div
   [:div.columns
    [:div.column.is-4-tablet.is-3-desktop.is-2-widescreen
     [sidebar data/books-sidebar]]
    [:div.column
     (condp = (session/get-in [:books-sidebar :choice])
       :books [books-page]
       :dashboard [:div
                   [:h2.title.is-2
                    (str "Default Dashboard Page")]]
       :customers [:div
                   [:h2.title.is-2
                    (str "Default Customers Page")]]
       :orders [:div
                [:h2.title.is-2
                 (str "Default Orders Page")]]
       [:div
        [:h2.title.is-2 (str "Unknown sub-page name: " (session/get-in [:books-sidebar :choice]))]]
       )]]
   [:div.columns
    [:div.column
     [:h4.title.is-4 "Global State"]
     [:div (render-map @session/state)]]]
   ]
  )
