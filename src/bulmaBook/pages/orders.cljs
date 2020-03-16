(ns bulmaBook.pages.orders
  (:require [reagent.core :as r]
            [bulmaBook.models :as models]
            [bulmaBook.components.basic :refer [breadcrumbs]]
            [bulmaBook.components.toolbar :refer [toolbar deftoolbar-item]]
            [bulmaBook.components.inputs :as inputs]
            [bulmaBook.components.tables :as tables]
            [bulmaBook.pages.ui :as ui]
            [bulmaBook.utils :as utils]
            [bulmaBook.store :as store]))

(def orders-list (r/atom (models/orders->vec)))

(defn listing-component [type]
  (if (= type (ui/get-listing-type :orders))
    [:strong (utils/keyword->str type :initial-caps true)]
    [:a {:href "#"
         :on-click #(ui/set-listing-type :orders type)}
     (utils/keyword->str type :initial-caps true)]))

(defn filter-orders [term]
  true)

(defn get-toolbar-data []
  {:left-items [(deftoolbar-item
                  :content [:p.subtitle.is-5
                            [:strong
                             (count @orders-list)]
                            " orders"])
                (deftoolbar-item
                  :class "is-hidden-table-only"
                  :content [inputs/search filter-orders
                            :placeholder "Order #, customer"])]
   :right-items [(deftoolbar-item
                   :content [listing-component :all])
                 (deftoolbar-item
                   :content [listing-component :in-progress])
                 (deftoolbar-item
                   :content [listing-component :complete])
                 (deftoolbar-item
                   :content [listing-component :failed])]})
(defn order-edit-page []
  [:<>
   [breadcrumbs :ui.orders.page
    [{:name "Orders"
      :value :orders
      :active false}
     {:name "Edit Order"
      :value :edit-order
      :active true}]]
   [:p "Edit order page"]])

(defn do-edit-order [oid]
  (ui/set-target :orders oid)
  (ui/set-subpage :orders :edit-order))

(defn do-edit-customer [cid]
  (ui/set-target :customers cid)
  (ui/set-subpage :orders :edit-customer))

(defn order-table [order-data]
  (let [orders (mapv (fn [o]
                       [(tables/defcell
                          [:a {:href "#"
                               :on-click #(do-edit-order (:id o))}
                           (:id o)])
                        (tables/defcell
                          [:a {:href "#"
                               :on-click #(do-edit-customer (:cid o))}
                           (:customer o)])
                        (tables/defcell (str (:date o)))
                        (tables/defcell (str (:book-count o)))
                        (tables/defcell
                          (case (:status o)
                            :in-progress [:span.tag.is-warning "In progress"]
                            :complete [:span.tag.is-success "Complete"]
                            :failed [:span.tag.is-danger "failed"]))
                        (tables/defcell (str "$" (:total o)))])
                     order-data)
        header [(mapv (fn [h]
                        (tables/defcell h :type :th))
                      ["Order #" "Customer" "Date" "Books" "Status" "Total"])]]
    [tables/table orders :header header :footer header :borded true
     :hover true :narrow true :fullwidth true :striped true]))

(defn orders-list-page []
  (store/reset! orders-list (models/orders->vec))
  (fn []
    [:<>
     [:h1.title "Orders"]
     [toolbar (get-toolbar-data)]
     [order-table @orders-list]]))

(defn orders-page []
  (case (ui/get-subpage :orders)
    :orders [orders-list-page]
    :edit-order [order-edit-page]))

(ui/set-subpage :orders :orders)
(ui/set-listing-type :orders :all)
