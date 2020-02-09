(ns bulmaBook.pages.home
  (:require [bulmaBook.components.sidebar :refer [sidebar]]
            [bulmaBook.components.basic :refer [render-map]]
            [bulmaBook.pages.books :as bks]
            [bulmaBook.components.toolbar :as tb]
            [bulmaBook.data :as data]
            [reagent.session :as session]))

(defn home-page []
  [:div
   [:div.columns
    [:div.column.is-4-tablet.is-3-desktop.is-2-widescreen
     [sidebar data/books-sidebar]]
    [:div.column
     (condp = (session/get-in [:books-sidebar :choice])
       :books [:div
               [:h2.title.is-2 (str "Page: " (session/get-in [:books-sidebar :choice]))]
               [tb/toolbar data/books-toolbar]
               [bks/book-pages-component]
               [:p "This is a default page. It will be replaced with real content later."]
               ]
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
