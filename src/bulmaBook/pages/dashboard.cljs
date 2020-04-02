(ns bulmaBook.pages.dashboard
  (:require [bulmaBook.components.inputs :as inputs]
            [bulmaBook.store :as store]
            [bulmaBook.components.basic :as basic]
            [bulmaBook.components.cards :as cards]
            [bulmaBook.components.media :as media]
            [bulmaBook.models :as models]
            [bulmaBook.utils :as utils]
            [bulmaBook.pages.ui :as ui]))

(defn do-edit-order [oid]
  (ui/set-target :orders oid)
  (ui/set-subpage :orders :edit-order)
  (ui/set-sidebar :orders))

(defn do-list-orders []
  (ui/set-subpage :orders :orders)
  (ui/set-sidebar :orders))

(defn order-item-component [order]
  [:div.level
   [:div.level-left
    [:div
     [:p.title.is-5.is-marginless
      [basic/a (:id order) :on-click #(do-edit-order (:id order))]]
     [:small (:date order) " "
      [basic/a (:customer order)]]]]
   [:div.level-right
    [:div.has-text-right
     [:p.title.is-5.is-marginless
      (str "$" (:total order))]
     [:span.tag {:class [(case (:status order)
                           :in-progress "is-warning"
                           :complete "is-success"
                           :failed "is-danger"
                           nil)]}
      (:status order)]]]])

(defn latest-orders-component []
  (let [orders (models/recent-orders)]
    [cards/card [:<>
                 [:h2.title.is-4 "Latest orders"]
                 (for [o orders]
                   ^{:key (:id o)} [order-item-component o])
                 [basic/a "View all orders"
                  :class "button is-link is-outlined"
                  :on-click do-list-orders]]]))

(defn do-edit-book [bid]
  (ui/set-target :books bid)
  (ui/set-subpage :books :edit-book)
  (ui/set-sidebar :books))

(defn do-list-books []
  (ui/set-subpage :books :books)
  (ui/set-sidebar :books))

(defn popular-book-item [rank bid]
  (let [book (models/get-book bid)]
    [media/media [:p.title.is-5.is-spaced.is-marginless
                  [basic/a (:title book) :on-click #(do-edit-book bid)]]
     :left [:<>
            [media/media-left [:p.number rank]]
            [media/media-left [basic/img (:image book) :width 40]]]
     :right [media/media-right (str (models/number-sold bid) " sold")]]))

(defn popular-books-component []
  (let [book-ranking (map-indexed (fn [idx val]
                                    [(inc idx) val])
                                  (models/popular-books))]
    [cards/card [:<>
                 [:h2.title.is-4 "Most popular books"]
                 (for [[rank bid] book-ranking]
                   ^{:key bid} [popular-book-item rank bid])
                 [basic/a "View all books"
                  :class "button is-link is-outlined" :on-click do-list-books]]]))

(defn do-edit-customer [cid]
  (ui/set-target :customers cid)
  (ui/set-subpage :customers :edit-customer)
  (ui/set-sidebar :customers))

(defn do-list-customers []
  (ui/set-subpage :customers :customers)
  (ui/set-sidebar :customers))

(defn loyal-customer-item [rank cid]
  (let [customer (models/get-customer cid)]
    [media/media [:<>
                  [:p.title.is-5.is-spaced.is-marginless
                   [basic/a (str (:first-name customer) " "
                                 (:last-name customer))
                    :on-click #(do-edit-customer cid)]]
                  [:p.subtitle.is-6 (:country customer)]]
     :left [media/media-left [:p.number rank] :class "is-marginless"]
     :right [media/media-right (str (models/customer-historical-orders cid)
                                    " orders")]]))

(defn loyal-customer-component []
  (let [customer-ranking (map-indexed (fn [idx val]
                                        [(inc idx) val])
                                      (models/loyal-customers))]
    [cards/card [:<>
                 [:h2.title.is-4 "Most loyal customers"]
                 (for [[rank cid] customer-ranking]
                   ^{:key cid} [loyal-customer-item rank cid])
                 [basic/a "View all customers"
                  :class "button is-link is-outlined"
                  :on-click do-list-customers]]]))

(defn dashboard-page []
  (let [stats (models/dashboard-period-data (models/dashboard-period))]
    [:<>
     [:h1.title "Dashboard"]
     [:div.level
      [:div.level-left
       [:h1.subtitle.is-3
        [:span.has-text-grey-light "Hello "]
        [:strong (models/session-user)]]]
      [:div.leve-right
       [inputs/select :ui.dashboard.period
        [(inputs/defoption "Today" :value "today")
         (inputs/defoption "Yesterday" :value "yesterday")
         (inputs/defoption "This Week" :value "week")
         (inputs/defoption "This Month" :value "month")
         (inputs/defoption "This Year" :value "year")
         (inputs/defoption "All Time" :value "all")]
        :selected "month" :model store/global-state]]]
     [:div.columns.is-multiline
      [:div.column.is-12-tablet.is-6-desktop.is-3-widescreen
       [basic/notification [:<>
                            [:p.title.is-1
                             (utils/number-thousands (str (:orders stats)))]
                            [:p.subtitle.is-4 "Orders"]]
        :class "is-link has-text"]]
      [:div.column.is-12-tablet.is-6-desktop.is-3-widescreen
       [basic/notification [:<>
                            [:p.title.is-1
                             (str "$" (utils/number-thousands
                                       (str (:revenue stats))))]
                            [:p.subtitle.is-4 "Revenue"]]
        :class "is-info has-text"]]
      [:div.column.is-12-tablet.is-6-desktop.is-3-widescreen
       [basic/notification [:<>
                            [:p.title.is-1
                             (utils/number-thousands (str (:visitors stats)))]
                            [:p.subtitle.is-4 "Visitors"]]
        :class "is-primary has-text"]]
      [:div.column.is-12-tablet.is-6-desktop.is-3-widescreen
       [basic/notification [:<>
                            [:p.title.is-1
                             (utils/number-thousands (str (:pages stats)))]
                            [:p.subtitle.is-4 "Pages"]]
        :class "is-success has-text"]]
      [:div.column.is-12-tablet.is-6-desktop.is-4-fullhd
       [latest-orders-component]]
      [:div.column.is-12-tablet.is-6-desktop.is-4-fullhd
       [popular-books-component]]
      [:div.column.is-12-tablet.is-6-desktop.is-4-fullhd
       [loyal-customer-component]]]]))
