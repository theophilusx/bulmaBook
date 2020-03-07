(ns bulmaBook.pages.core
  (:require [bulmaBook.pages.home :refer [home-page]]
            [bulmaBook.utils :refer [spath]]
            [bulmaBook.pages.login :refer [login]]
            [bulmaBook.pages.register :refer [register]]
            [bulmaBook.pages.profile :refer [profile]]
            [bulmaBook.pages.bug :refer [bug-report]]
            [bulmaBook.components.basic :refer [render-map]]
            [bulmaBook.store :as store]
            [bulmaBook.data :as data]))

(defn current-page []
  (case (store/get-in store/global-state (spath data/navbar-id))
    :home [home-page]
    :profile [:<>
              [profile]
              [render-map @store/global-state]]
    :report-bug [:<>
                 [bug-report]
                 [render-map @store/global-state]]
    :sign-out [:<>
               [:h2.h2.title "Sign Out page goes here"]
               [render-map @store/global-state]]
    :login [:<>
            [login]
            [render-map @store/global-state]]
    :register [:<>
               [register]
               [render-map @store/global-state]]
    [:<>
     [:h2.h2.title "Bad page name: " (store/get-in store/global-state
                                      (spath data/navbar-id) "nil")]
     [render-map @store/global-state]]))
