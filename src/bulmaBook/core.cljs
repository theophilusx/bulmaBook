(ns ^:figwheel-hooks bulmaBook.core
  (:require [goog.dom :as gdom]
            [bulmaBook.data :as data]
            [bulmaBook.pages.books :as bks]
            [bulmaBook.pages.navbar :as navbar]
            [bulmaBook.pages.home-sidebar :as home-sb]
            [bulmaBook.components.toolbar :as tb]
            [bulmaBook.components.basic :refer [media icon render-map]]
            [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            ;; [clojure.pprint :refer [pprint]]
            ))

(defn get-app-element []
  (gdom/getElement "app"))

(defn homepage-component []
  [:div
   [navbar/navbar-component data/navbar-data]
   [:section
    [:div.container
     [:div.columns
      [:div.column.is-4-tablet.is-3-desktop.is-2-widescreen
       [home-sb/sidebar-component]]
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
       [:div (render-map @session/state)]]]]]])


(defn mount [el]
  (reagent/render-component [homepage-component] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(session/assoc-in! [:data :book-data] data/book-data)
(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
