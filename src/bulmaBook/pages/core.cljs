(ns bulmaBook.pages.core
  (:require [bulmaBook.pages.home :refer [home-page]]
            [theophilusx.yorick.utils :refer [spath]]
            [bulmaBook.pages.login :refer [login]]
            [bulmaBook.pages.register :refer [register]]
            [bulmaBook.pages.profile :refer [profile]]
            [bulmaBook.pages.bug :refer [bug-report]]
            [theophilusx.yorick.basic :refer [render-map]]
            [theophilusx.yorick.store :as store]
            [bulmaBook.data :as data]))

(defn current-page []
  (case (store/get-in store/global-state (spath data/navbar-id))
    :home [:<>
           [home-page]
           [:div.columns
            [:div.column
             [:h4.title.is-4 "Global State"]
             [:div (render-map @store/global-state)]]]]
    :profile [:<>
              [profile]
              [:div.columns
               [:div.column
                [:h4.title.is-4 "Global State"]
                [:div (render-map @store/global-state)]]]]
    :report-bug [:<>
                 [bug-report]
                 [:div.columns
                  [:div.column
                   [:h4.title.is-4 "Global State"]
                   [:div (render-map @store/global-state)]]]]
    :sign-out [:<>
               [:h2.h2.title "Sign Out page goes here"]
               [:div.columns
                [:div.column
                 [:h4.title.is-4 "Global State"]
                 [:div (render-map @store/global-state)]]]]
    :login [:<>
            [login]
            [:div.columns
             [:div.column
              [:h4.title.is-4 "Global State"]
              [:div (render-map @store/global-state)]]]]
    :register [:<>
               [register]
               [:div.columns
                [:div.column
                 [:h4.title.is-4 "Global State"]
                 [:div (render-map @store/global-state)]]]]
    [:<>
     [:h2.h2.title "Bad page name: " (store/get-in store/global-state
                                                   (spath data/navbar-id) "nil")]
     [:div.columns
      [:div.column
       [:h4.title.is-4 "Global State"]
       [:div (render-map @store/global-state)]]]]))
