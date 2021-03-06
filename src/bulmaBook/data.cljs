(ns bulmaBook.data
  (:require [theophilusx.yorick.navbar :refer [defnavbar-item]]
            [theophilusx.yorick.sidebar :refer [defsidebar-item]]
            [theophilusx.yorick.store :refer [get-in global-state]]
            [theophilusx.yorick.icon :as icons]))

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
                      :cost 22.99
                      :pages 270
                      :isbn "9781939902351"}
                :bk2 {:id :bk2
                      :title "Docker in Production"
                      :image "images/docker.jpg"
                      :cost 22.99
                      :pages 156
                      :isbn "9781939902184"}
                :bk3 {:id :bk3
                      :title "Developing a Gulp.js Edge"
                      :image "images/gulp.jpg"
                      :cost 22.99
                      :pages 134
                      :isbn "9781939902146"}
                :bk4 {:id :bk4
                      :title "Learning Swift"
                      :image "images/swift.jpg"
                      :cost 22.99
                      :pages 342
                      :isbn "9781939902115"}
                :bk5 {:id :bk5
                      :title "Choosing a JavaScript Framework"
                      :image "images/js-framework.jpg"
                      :cost 19.99
                      :pages 96
                      :isbn "9781939902092"}
                :bk6 {:id :bk6
                      :title "Deconstructing Google Cardboard Apps"
                      :image "images/google-cardboard.jpg"
                      :cost 22.99
                      :pages 179
                      :isbn "9781939902092245"}})

(def book-counter 6)

(def book-sales {:bk1 160
                 :bk2 154
                 :bk3 123
                 :bk4 110
                 :bk5 158
                 :bk6 162})

(def customer-data {:cs1 {:id :cs1
                          :title "Mr"
                          :first-name "John"
                          :last-name "Miller"
                          :email "johnmiller@example.com"
                          :address1 "100 Donald Av."
                          :address2 nil
                          :city "Smallville"
                          :pcode "4432"
                          :country "United States"}
                    :cs2 {:id :cs2
                          :title "Ms"
                          :first-name "Samantha"
                          :last-name "Rogers"
                          :email "samrogers@example.com"
                          :address1 "44 Victoria St."
                          :address2 nil
                          :city "Oxford"
                          :pcode "WO3"
                          :country "United Kingdom"}
                    :cs3 {:id :cs3
                          :title "Dr"
                          :first-name "Paul"
                          :last-name "Jacques"
                          :email "paulj@example.com"
                          :address1 "The Big Book Store"
                          :address2 "114 Broadway St."
                          :city "Newtown"
                          :pcode "2320"
                          :country "Australia"}})

(def customer-history {:cs1 7
                       :cs2 5
                       :cs3 11})

(def customer-counter 3)

(def order-data {:OR1000 {:id :OR1000
                          :date "03 JAN 2020 11:35"
                          :cid :cs1
                          :books {:bk1{:id :bk1
                                       :cost 22.99
                                       :quantity 1}}
                          :status :in-progress}
                 :OR1001 {:id :OR1001
                          :date "09 JAN 2020 12:20"
                          :cid :cs3
                          :books {:bk2 {:id :bk2
                                        :cost 22.99
                                        :quantity 1}
                                  :bk3 {:id :bk3
                                        :cost 22.99
                                        :quantity 1}}
                          :status :complete}
                 :OR1002 {:id :OR1002
                          :date "20 JAN 2020 16:20"
                          :cid :cs3
                          :books {:bk4 {:id :bk4
                                        :cost 22.99
                                        :quantity 1}}
                          :status :failed}
                 :OR1003 {:id :OR1003
                          :date "02 FEB 2020 09:30"
                          :cid :cs1
                          :books {:bk6 {:id :bk6
                                        :cost 22.99
                                        :quantity 1}}
                          :status :complete}})

(def order-counter 1003)

(def dashboard-data {:yesterday {:orders 10
                                 :revenue 950
                                 :visitors 350
                                 :pages 910}
                     :today {:orders 5
                             :revenue 144
                             :visitors 120
                             :pages 830}
                     :week {:orders 30
                            :revenue 750
                            :visitors 600
                            :pages 1000}
                     :month {:orders 120
                             :revenue 435
                             :visitors 3200
                             :pages 1120}
                     :year {:orders 2300
                            :revenue 5450
                            :visitors 39125
                            :pages 5000}
                     :all {:orders 67995
                           :revenue 47330
                           :visitors 120605
                           :pages 5100}})


(def navbar-id :ui.navbar)

(defn get-navabar-data []
  (let [session-name (get-in global-state [:session :user :name])]
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
                                 :icon-data (icons/deficon "fas fa-user"))
                               (defnavbar-item :id :report-bug
                                 :contents "Report Bug"
                                 :icon-data (icons/deficon "fas fa-bug"))
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



(def books-sidebar {:sid :ui.sidebar
                    :default-link :dashboard
                    :data (defsidebar-item
                            :type :menu
                            :title "Menu"
                            :items [(defsidebar-item
                                      :title "Dashboard"
                                      :icon-data (icons/deficon "fas fa-tachometer-alt")
                                      :id :dashboard)
                                    (defsidebar-item
                                      :title "Books"
                                      :icon-data (icons/deficon "fas fa-book")
                                      :id :books)
                                    (defsidebar-item
                                      :title "Customers"
                                      :icon-data (icons/deficon "fas fa-address-book")
                                      :id :customers)
                                    (defsidebar-item
                                      :title "Orders"
                                      :icon-data (icons/deficon "fas fa-file-alt")
                                      :id :orders)])})

