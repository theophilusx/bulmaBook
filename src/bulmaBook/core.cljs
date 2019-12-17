(ns ^:figwheel-hooks bulmaBook.core
  (:require [goog.dom :as gdom]
            [bulmaBook.navbar :as nb]
            [bulmaBook.vertical-menu :as vm]
            [bulmaBook.toolbar :as tb]
            [bulmaBook.basic :as b]
            [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [clojure.pprint :refer [pprint]]))

(defn multiply [a b] (* a b))


;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:text "Hello world!"}))

(defn get-app-element []
  (gdom/getElement "app"))

(defn login-component
  "Initial go at the login component. Lots more to do!"
  []
  [:section.hero.is-primary.is-fullheight
   [:div.hero-body
    [:div.container
     [:div.columns.is-centered
      [:div.column.is-5-tablet.is-4-desktop.is-3-widescreen
       [:form.box
        [:div.field.has-text-centered
         [:img {:src "images/logo-bis.png" :width "162"}]]
        [:div.field
         [:label.label "Email"]
         [:div.control.has-icons-left
          [:span.icon.is-small.is-left
            [:i.fa.fa-envelope]]
          [:input.input {:type "email"
                         :placeholder "e.g. alexjohnson@gmail.com"
                         :required true}]]]
        [:div.field
         [:label.label "Password"]
         [:div.control.has-icons-left
          [:input.input {:type "password"
                         :placeholder "********"
                         :required true}]
          [:span.icon.is-small.is-left
           [:i.fa.fa-lock]]]]
        [:div.field
         [:label.checkbox
          [:input {:type "checkbox"}]
          " Remember me"]]
        [:div.field
         [:button.button.is-success "Login"]]
        ]]]]]])

(defn navbar-component []
  [nb/navbar
   :main-navbar
   :has-shadow true
   :brand (nb/defitem
            :contents [:img {:src "images/logo.png"}]) 
   :has-burger true
   :start-menu
   [(nb/defitem
      :type :div
      :contents [(nb/defitem
                   :type :raw
                   :contents [:small "Publishing at the speed of technology"])])]
   :end-menu [(nb/defitem
                :type :dropdown
                :title "Alex Johnson"
                :is-hoverable true
                :contents [(nb/defitem
                             :id :profile
                             :contents "Profile"
                             :icon-img "fa-user-circle-o"
                             :selectable true)
                           (nb/defitem
                             :id :report-bug
                             :contents "Report Bug"
                             :icon-img "fa-bug"
                             :selectable true)
                           (nb/defitem
                             :id :sign-out
                             :contents "Sign Out"
                             :icon-img "fa-sign-out"
                             :selectable true)])]])

(defn homepage-component []
  [:div
   [navbar-component]
   [:section
    [:div.columns
     [:div.column.is-4-tablet.is-3-desktop.is-2-widescreen
      [vm/menu
       :sidebar-menu
       (vm/defitem
         :type :menu
         :title "Menu"
         :items
         [(vm/defitem :title "Dashboard" :icon-img "fa-tachometer" :id :dashboard)
          (vm/defitem :title "Books" :icon-img "fa-book" :id :books)
          (vm/defitem :title "Customers" :icon-img "fa-address-book" :id :customers)
          (vm/defitem :title "Orders" :icon-img "fa-file-text-o" :id :orders)])]]
     [:div.column
      [:h2.title.is-2 (str (name (or (session/get-in [:main-navbar :choice])
                                     "Unknown")) " / "
                           (name (or (session/get-in [:sidebar-menu :choice])
                                     "Unknown")))]
      [tb/toolbar
       :left-items [(tb/defitem :content [:p.subtitle.is-5 [:strong "6"]])
                    (tb/defitem
                      :type :p
                      :content [b/button :title "New" :class "is-success"])
                    (tb/defitem
                      :class "is-hidden-table-only"
                      :content [:div.field.has-addons
                                [:p.control
                                 [:input.input {:type "text"
                                                :placeholder "Book name, ISBN"}]]
                                [:p.control
                                 [:button.button "Search"]]])
                    ]
       :right-items [(tb/defitem :content "Order by")
                     (tb/defitem
                       :content [:div.select
                                 [:select
                                  [:option "Publish date"]
                                  [:option "Price"]
                                  [:option "Page count"]]])]
       ]
      [:p "This is a default page. It will be replaced with real content later."]]]
    [:div.columns
     [:div.column
      [:h4.title.is-4 "Global State"]
      [:p (str "Session: " @session/state)]]
     [:div.column
      [:h4.title.is-4 "Navbar State"]
      [:p (str "Navbars: " @nb/navbar-state)]]
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
(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
