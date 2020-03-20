(ns bulmaBook.pages.home
  (:require [bulmaBook.components.sidebar :refer [sidebar]]
            [bulmaBook.pages.books :refer [books-page]]
            [bulmaBook.pages.dashboard :refer [dashboard-page]]
            [bulmaBook.pages.customers :refer [customers-page]]
            [bulmaBook.pages.orders :refer [orders-page]]
            [bulmaBook.data :as data]
            [bulmaBook.store :as store]
            [bulmaBook.pages.ui :as ui]))

(defn home-page []
  [:<>
   [:div.columns
    [:div.column.is-4-tablet.is-3-desktop.is-2-widescreen
     [sidebar data/books-sidebar]]
    [:div.column
     (condp = (ui/get-sidebar)
       :books [books-page]
       :dashboard [dashboard-page]
       :customers [customers-page]
       :orders [orders-page]
       [:div
        [:h2.title.is-2 (str "Unknown sub-page name: "
                             (store/get-in store/global-state
                                           [:books-sidebar :choice]))]])]]])
