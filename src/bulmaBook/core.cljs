(ns ^:figwheel-hooks bulmaBook.core
  (:require [goog.dom :as gdom]
            [bulmaBook.data.books :as book-data]
            [bulmaBook.pages.books :as bks]
            [bulmaBook.components.navbar :as nb]
            [bulmaBook.components.vertical-menu :as vm]
            [bulmaBook.components.toolbar :as tb]
            [bulmaBook.components.basic :refer [media icon button]]
            [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [clojure.pprint :refer [pprint]]))

(defn get-app-element []
  (gdom/getElement "app"))

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

(defn sidebar-component []
  [vm/menu
   :sidebar-menu
   (vm/defitem
     :type :menu
     :title "Menu"
     :items
     [(vm/defitem :title "Dashboard" :icon-img "fa-tachometer" :id :dashboard)
      (vm/defitem :title "Books" :icon-img "fa-book" :id :books)
      (vm/defitem :title "Customers" :icon-img "fa-address-book" :id :customers)
      (vm/defitem :title "Orders" :icon-img "fa-file-text-o" :id :orders)])])

(defn toolbar-component []
  [tb/toolbar
   :left-items
   [(tb/defitem :content [:p.subtitle.is-5 [:strong "6"]])
    (tb/defitem :type :p :content [button :title "New" :class "is-success"])
    (tb/defitem
      :class "is-hidden-table-only"
      :content [:div.field.has-addons
                [:p.control
                 [:input.input {:type "text"
                                :placeholder "Book name, ISBN"}]]
                [:p.control
                 [:button.button "Search"]]])]
   :right-items [(tb/defitem :content "Order by")
                 (tb/defitem
                   :content [:div.select
                             [:select
                              [:option "Publish date"]
                              [:option "Price"]
                              [:option "Page count"]]])]])


(defn homepage-component []
  [:div
   [navbar-component]
   [:section
    [:div.columns
     [:div.column.is-4-tablet.is-3-desktop.is-2-widescreen
      [sidebar-component]]
     [:div.column
      [:h2.title.is-2 (str (name (or (session/get-in [:main-navbar :choice])
                                     "Unknown")) " / "
                           (name (or (session/get-in [:sidebar-menu :choice])
                                     "Unknown")))]
      [toolbar-component]
      [bks/book-pages-component]
      [:p "This is a default page. It will be replaced with real content later."]]]
    [:div.columns
     [:div.column
      [:h4.title.is-4 "Global State"]
      [:p (str "Session: " @session/state)]]
     [:div.column
      [:h4.title.is-4 "Menu State"]
      [:p (str "Menu: " @vm/menu-state)]]]]])


(defn mount [el]
  (reagent/render-component [homepage-component] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(book-data/init)
(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
