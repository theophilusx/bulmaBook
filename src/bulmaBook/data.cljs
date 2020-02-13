(ns bulmaBook.data
  (:require [bulmaBook.components.navbar :refer [defnavbar-item]]
            [bulmaBook.components.toolbar :refer [deftoolbar-item]]
            [bulmaBook.components.basic :refer [button]]
            [bulmaBook.components.sidebar :refer [defsidebar-item]]
            [reagent.session :as session]))

(def book-data [{:title "TensorFlow For Machine Intelligence"
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
                 :isbn "9781939902092245"}])

(def navbar-id :ui.navbar)

(defn get-navabar-data []
  (let [session-name (session/get-in [:session :user :name])]
    (if session-name
      {:session-id navbar-id
       :has-shadow true
       :class "is-dark"
       :default-link :home
       :has-burger true
       :brand (defnavbar-item
                :contents [:img {:src "images/logo.png"}])
       :menus [(defnavbar-item
                 :type :div
                 :contents
                 [(defnavbar-item
                    :type :raw
                    :contents [:small "Publishing at the speed of technology"])])
               (defnavbar-item
                 :contents "Home"
                 :id :home)]
       :end-menu [(defnavbar-item
                    :type :dropdown
                    :title session-name
                    :is-hoverable true
                    :contents [(defnavbar-item :id :profile :contents "Profile"
                                 :icon-img "fa-user-circle-o")
                               (defnavbar-item :id :report-bug
                                 :contents "Report Bug" :icon-img "fa-bug")
                               (defnavbar-item :id :sign-out :contents "Sign Out"
                                 :icon-img "fa-sign-out")])]}
      {:session-id navbar-id
       :has-shadow true
       :class "is-dark"
       :default-link :register
       :has-burger true
       :brand (defnavbar-item
                :contents [:img {:src "images/logo.png"}])
       :menus [(defnavbar-item
                 :type :div
                 :contents
                 [(defnavbar-item
                    :type :raw
                    :contents
                    [:small "Publishing at the speed of technology"])])]
       :end-menu [(defnavbar-item
                    :contents "Login"
                    :id :login)]})))

(def books-toolbar
  {:left-items [(deftoolbar-item
                  :content [:p.subtitle.is-5 [:strong "6"]])
                (deftoolbar-item
                  :type :p
                  :content [button :title "New" :class "is-success"])
                (deftoolbar-item
                  :class "is-hidden-table-only"
                  :content [:div.field.has-addons
                            [:p.control
                             [:input.input {:type "text"
                                            :placeholder "Book name, ISBN"}]]
                            [:p.control
                             [:button.button "Search"]]])]
   :right-items [(deftoolbar-item
                   :content "Order by")
                 (deftoolbar-item
                   :content [:div.select
                             [:select
                              [:option "Publish date"]
                              [:option "Price"]
                              [:option "Page count"]]])]})

(def books-sidebar {:session-key [:books-sidebar]
                    :default-link :dashboard
                    :item (defsidebar-item
                            :type :menu
                            :title "Menu"
                            :items [(defsidebar-item
                                      :title "Dashboard"
                                      :icon-img "fa-tachometer"
                                      :id :dashboard)
                                    (defsidebar-item
                                      :title "Books"
                                      :icon-img "fa-book"
                                      :id :books)
                                    (defsidebar-item
                                      :title "Customers"
                                      :icon-img "fa-address-book"
                                      :id :customers)
                                    (defsidebar-item
                                      :title "Orders"
                                      :icon-img "fa-file-text-o"
                                      :id :orders)])})
