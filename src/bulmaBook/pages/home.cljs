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
     [:h2.title.is-2 (str (name (or (session/get-in [:main-navbar :choice])
                                    "Unknown")) " / "
                          (name (or (session/get-in [:sidebar-menu :choice])
                                    "Unknown")))]
     [tb/toolbar data/books-toolbar]
     [bks/book-pages-component]
     [:p "This is a default page. It will be replaced with real content later."]]]
   [:div.columns
    [:div.column
     [:h4.title.is-4 "Global State"]
     [:div (render-map @session/state)]]]
   ]
  )
