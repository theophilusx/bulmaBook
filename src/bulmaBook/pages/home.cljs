(ns bulmaBook.pages.home
  (:require [bulmaBook.components.sidebar :refer [sidebar]]
            [bulmaBook.components.basic :refer [render-map]]
            [bulmaBook.pages.books :refer [books-page new-book-page]]
            [bulmaBook.pages.dashboard :refer [dashboard-page]]
            [bulmaBook.data :as data]
            [reagent.session :as session]))

(defn home-page []
  [:div
   [:div.columns
    [:div.column.is-4-tablet.is-3-desktop.is-2-widescreen
     [sidebar data/books-sidebar]]
    [:div.column
     (condp = (session/get-in [:ui :books :sidebar])
       :books (if (= (session/get-in [:ui :books :page]) :new-book)
                [new-book-page]
                [books-page])
       :dashboard [dashboard-page]
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
