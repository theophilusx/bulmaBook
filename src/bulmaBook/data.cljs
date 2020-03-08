(ns bulmaBook.data
  (:require [bulmaBook.components.navbar :refer [defnavbar-item]]
            [bulmaBook.components.sidebar :refer [defsidebar-item]]
            [bulmaBook.store :as store]
            [bulmaBook.components.icons :as icons]))

(def user-data {:fred-example-com {:email "fred@example.com"
                                   :password "secret"
                                   :first-name "Fred"
                                   :last-name "Flintstone"}
                :barney-email-com {:email "barney@email.com"
                                   :password "secret"
                                   :first-name "Barney"
                                   :last-name "Rubble"}})

(def book-data {:bk1 {:id :bk1
                      :title "TensorFlow For Machine Intelligence"
                      :image "images/tensorflow.jpg"
                      :cost "$22.99"
                      :pages 270
                      :isbn "9781939902351"}
                :bk2 {:id :bk2
                      :title "Docker in Production"
                      :image "images/docker.jpg"
                      :cost "$22.99"
                      :pages 156
                      :isbn "9781939902184"}
                :bk3 {:id :bk3
                      :title "Developing a Gulp.js Edge"
                      :image "images/gulp.jpg"
                      :cost "$22.99"
                      :pages 134
                      :isbn "9781939902146"}
                :bk4 {:id :bk4
                      :title "Learning Swift"
                      :image "images/swift.jpg"
                      :cost "$22.99"
                      :pages 342
                      :isbn "9781939902115"}
                :bk5 {:id :bk5
                      :title "Choosing a JavaScript Framework"
                      :image "images/js-framework.jpg"
                      :cost "19.99"
                      :pages 96
                      :isbn "9781939902092"}
                :bk6 {:id :bk6
                      :title "Deconstructing Google Cardboard Apps"
                      :image "images/google-cardboard.jpg"
                      :cost "$22.99"
                      :pages 179
                      :isbn "9781939902092245"}})

(def navbar-id :ui.navbar)

(defn get-navabar-data []
  (let [session-name (store/get-in store/global-state [:session :user :name])]
    (if session-name
      {:sid navbar-id
       :has-shadow true
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
                                 :icon-data (icons/deficon "fa-user-circle-o"))
                               (defnavbar-item :id :report-bug
                                 :contents "Report Bug"
                                 :icon-data (icons/deficon "fa-bug"))
                               (defnavbar-item :id :sign-out :contents "Sign Out"
                                 :icon-data (icons/deficon "fa-sign-out"))])]}
      {:sid navbar-id
       :has-hadow true
       :default-link :login
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
                    :contents "Register"
                    :id :register)]})))



(def books-sidebar {:sid :ui.books.sidebar
                    :default-link :dashboard
                    :item (defsidebar-item
                            :type :menu
                            :title "Menu"
                            :items [(defsidebar-item
                                      :title "Dashboard"
                                      :icon-data (icons/deficon "fa-tachometer")
                                      :id :dashboard)
                                    (defsidebar-item
                                      :title "Books"
                                      :icon-data (icons/deficon "fa-book")
                                      :id :books)
                                    (defsidebar-item
                                      :title "Customers"
                                      :icon-data (icons/deficon "fa-address-book")
                                      :id :customers)
                                    (defsidebar-item
                                      :title "Orders"
                                      :icon-data (icons/deficon "fa-file-text-o")
                                      :id :orders)])})
