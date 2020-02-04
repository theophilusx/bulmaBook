(ns ^:figwheel-hooks bulmaBook.core
  (:require [goog.dom :as gdom]
            [bulmaBook.data :as data]
            [bulmaBook.pages.home :refer [home-page]]
            [bulmaBook.pages.navbar :as navbar]
            [reagent.core :as reagent ]
            [reagent.session :as session]
            ;; [clojure.pprint :refer [pprint]]
            ))

(session/assoc-in! [:page-mapping] {:home home-page})

(defn get-element [name]
  (gdom/getElement name))

(defn mount [el component]
  (reagent/render-component component el))

(defn mount-app []
  (when-let [nb (get-element "navbar")]
    (mount nb [navbar/navbar-component data/navbar-data]))
  (when-let [el (get-element "app")]
    (mount el [home-page])))


;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(session/assoc-in! [:data :book-data] data/book-data)
(mount-app)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
