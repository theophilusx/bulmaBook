(ns ^:figwheel-hooks bulmaBook.core
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]))

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

(defn wip-component
  "WIP placeholder"
  []
  [:section
   [:div.container
    [:div.box
     [:div.message.has-icons-left
     [:span.icon.is-small.is-left
      [:i.fa.fa-hammer]]
      " Work in progress"]
     ]
    ]])

(defn navbar-component []
  [:nav.navbar.has-shadow
   [:div.navbar-brand
    [:a.navbar-item
     [:img {:src "images/logo.png"}]]
    [:div.navbar-burger
     [:span]
     [:span]
     [:span]]]
   [:div.navbar-menu
    [:div.navbar-start
     [:div.navbar-item
      [:small "Publishing at the speed of technology"]]]
    [:div.navbar-end
     [:div.navbar-item.has-dropdown.is-hoverable
      [:div.navbar-link "Alex Johnson"]
      [:div.navbar-dropdown
       [:a.navbar-item
        [:div
         [:span.icon.is-small
          [:i.fa.fa-user-circle-o]]
         " Profile"]]
       [:a.navbar-item
        [:div
         [:span.icon.is-small
          [:i.fa.fa-bug]]
         " Report Bug"]]
       [:a.navbar-item
        [:div
         [:span.icon.is-small
          [:i.fa.fa-sign-out]]
         " Sign Out"]]]]]]])

(defn homepage-component []
  [:div
   [navbar-component]
   [:section
    [:div.columns
     [:div.column.is-4-tablet.is-3-desktop.is-2-widescreen
      [:nav.menu
       [:p.menu-label "Menu"]
       [:ul.menu-list
        [:li [:a {:href "dashboard.html"}
              [:span.icon
               [:i.fa.fa-tachometer]]
              " Dashboard"]]
        [:li [:a.is-active {:href "books.html"}
              [:span.icon
               [:i.fa.fa-book]]
              " Books"]]
        [:li [:a {:href "customers.html"}
              [:span.icon
               [:i.fa.fa-address-book]]
              " Customers"]]
        [:li [:a {:href "orders.html"}
              [:span.icon
               [:i.fa.fa-file-text-o]]
              " Orders"]]]]]
     [:div.column
      ]]]])

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
