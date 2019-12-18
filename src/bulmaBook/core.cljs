(ns ^:figwheel-hooks bulmaBook.core
  (:require [goog.dom :as gdom]
            [bulmaBook.navbar :as nb]
            [bulmaBook.vertical-menu :as vm]
            [bulmaBook.toolbar :as tb]
            [bulmaBook.basic :refer [media icon button]]
            [bulmaBook.paginate :refer [paginate]]
            [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [clojure.pprint :refer [pprint]]))

(defn get-app-element []
  (gdom/getElement "app"))

(defn data-init []
  (session/assoc-in! [:book-data] [{:title "TensorFlow For Machine Intelligence"
                                    :image "images/tensorflow.jpg"
                                    :cost "$22.99"
                                    :pages 270
                                    :isbn "9781939902351"}
                                   {:title "Docker in Production"
                                    :image "images/docker.jpg"
                                    :cost "$22.99"
                                    :pages 156
                                    :isbn "9781939902184"}
                                   {:title "Developing a Gulp.js Edge"
                                    :image "images/gulp.jpg"
                                    :cost "$22.99"
                                    :pages 134
                                    :isbn "9781939902146"}
                                   {:title "Learning Swift”"
                                    :image "images/swift.jpg"
                                    :cost "$22.99"
                                    :pages 342
                                    :isbn "9781939902115"}
                                   {:title "Choosing a JavaScript Framework"
                                    :image "images/js-framework.jpg"
                                    :cost "19.99"
                                    :pages 96
                                    :isbn "9781939902092"}
                                   {:title "Deconstructing Google Cardboard Apps"
                                    :image "images/google-cardboard.jpg"
                                    :cost "$22.99"
                                    :pages 179
                                    :isbn "9781939902092245"}]))

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
   :end-menu
   [(nb/defitem
      :type :dropdown
      :title "Alex Johnson"
      :is-hoverable true
      :contents [(nb/defitem :id :profile :contents "Profile"
                   :icon-img "fa-user-circle-o" :selectable true)
                 (nb/defitem :id :report-bug :contents "Report Bug"
                   :icon-img "fa-bug" :selectable true)
                 (nb/defitem :id :sign-out :contents "Sign Out"
                   :icon-img "fa-sign-out" :selectable true)])]])

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

(defn book-component [book]
  [:article.box
   [media {:content [[:p.title.is-5.is-spaced.is-marginless
                      [:a {:href "#"} (:title book)]]
                     [:p.subtitle.is-marginless (:price book)]
                     [:div.content.is-small
                      (str (:pages book) " pages")
                      [:br]
                      (str "ISBN: " (:isbn book))
                      [:br]
                      [:a {:href "#"} "Edit"]
                      [:span "·"]
                      [:a {:href "#"} "Delete"]]]}
    :left {:content [[:img {:src (:image book) :width "80"}]]}]])

(defn book-grid-component [books]
  (into
   [:div.columns.is-multiline]
   (for [b books]
     [book-component b])))

(defn book-pages-component []
  (let [books (session/get-in [:book-data])]
    (paginate books book-grid-component :page-size 3)))

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
      [book-pages-component]
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
(data-init)
(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
