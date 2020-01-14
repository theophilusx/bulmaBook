(ns bulmaBook.pages.navbar
  (:require [bulmaBook.components.navbar :as nb]))

(defn navbar-component []
  [nb/navbar
   [:main-navbar]
   :has-shadow true
   :class "is-dark"
   :default-link :home
   :brand (nb/defitem
            :contents [:img {:src "images/logo.png"}]) 
   :has-burger true
   :start-menu
   [(nb/defitem
      :type :div
      :contents [(nb/defitem
                   :type :raw
                   :contents [:small "Publishing at the speed of technology"])])
    (nb/defitem
      :contents "Home"
      :id :home)]
   :end-menu
   [(nb/defitem
      :type :dropdown
      :title "Alex Johnson"
      :is-hoverable true
      :contents [(nb/defitem :id :profile :contents "Profile"
                   :icon-img "fa-user-circle-o")
                 (nb/defitem :id :report-bug :contents "Report Bug"
                   :icon-img "fa-bug")
                 (nb/defitem :id :sign-out :contents "Sign Out"
                   :icon-img "fa-sign-out")])]])
