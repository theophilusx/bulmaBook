(ns ^:figwheel-hooks bulmaBook.core
  (:require [goog.dom :as gdom]
            [bulmaBook.data :as data]
            [bulmaBook.pages.core :refer [current-page]]
            [bulmaBook.pages.navbar :refer [top-navbar]]
            [theophilusx.yorick.store :as store]
            [reagent.core :as reagent]
            [reagent.dom :as rdom]))

(defn get-element [name]
  (gdom/getElement name))

(defn mount [el component]
  (rdom/render component el))

(defn mount-app []
  (when-let [nb (get-element "navbar")]
    (mount nb [top-navbar]))
  (when-let [el (get-element "app")]
    (mount el [current-page])))


;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(store/assoc-in! store/global-state [:data :book-data] data/book-data)
(store/assoc-in! store/global-state [:data :book-counter] data/book-counter)
(store/put! store/global-state :users data/user-data)
(store/assoc-in! store/global-state [:data :customer-data] data/customer-data)
(store/assoc-in! store/global-state [:data :customer-counter] data/customer-counter)
(store/assoc-in! store/global-state [:data :order-data] data/order-data)
(store/assoc-in! store/global-state [:data :order-counter] data/order-counter)
(store/assoc-in! store/global-state [:data :dashboard-data] data/dashboard-data)
(store/assoc-in! store/global-state [:data :book-sales] data/book-sales)
(store/assoc-in! store/global-state [:data :customer-history] data/customer-history)
(mount-app)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  (store/update-in! store/global-state [:__figwheel_counter] inc)
)
