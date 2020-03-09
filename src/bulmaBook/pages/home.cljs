(ns bulmaBook.pages.home
  (:require [bulmaBook.components.sidebar :refer [sidebar]]
            [bulmaBook.pages.books :refer [books-page]]
            [bulmaBook.pages.dashboard :refer [dashboard-page]]
            [bulmaBook.data :as data]
            [bulmaBook.store :as store]))

(defn home-page []
  [:<>
   [:div.columns
    [:div.column.is-4-tablet.is-3-desktop.is-2-widescreen
     [sidebar data/books-sidebar]]
    [:div.column
     (condp = (store/get-in store/global-state [:ui :books :sidebar])
       :books [books-page]
       :dashboard [dashboard-page]
       :customers [:div
                   [:h2.title.is-2
                    (str "Default Customers Page")]]
       :orders [:div
                [:h2.title.is-2
                 (str "Default Orders Page")]]
       [:div
        [:h2.title.is-2 (str "Unknown sub-page name: "
                             (store/get-in store/global-state
                                           [:books-sidebar :choice]))]])]]
   ])
