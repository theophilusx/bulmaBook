(ns bulmaBook.pages.customers
  (:require [bulmaBook.components.toolbar :refer [deftoolbar-item toolbar]]
            [bulmaBook.components.inputs :as inputs]
            [reagent.core :as r]
            [bulmaBook.store :as store]
            [bulmaBook.utils :as utils]
            [bulmaBook.components.basic :refer [breadcrumbs]]))

(def customer-list (r/atom {}))

(defn customer-data []
  (store/get-in store/global-state [:data :customer-data]))

(defn customers->vec []
  (let [customers (customer-data)]
    (mapv #(% customers) (keys customers))))

(defn set-subpage [page]
  (store/assoc-in! store/global-state [:ui :customers :page] page))

(defn get-subpage []
  (store/get-in store/global-state [:ui :customers :page]))

(defn set-listing-type [type]
  (store/assoc-in! store/global-state [:ui :customers :listing] type))

(defn get-listing-type []
  (store/get-in store/global-state [:ui :customers :listing]))

(defn filter-customers []
  true)

(defn listing-component [type]
  (if (= type (get-listing-type))
    [:strong (utils/keyword->str type :initial-caps true)]
    [:a {:href "#"
         :on-click #(set-listing-type type)}
     (utils/keyword->str type :initial-caps true)]))


(defn get-toolbar-data []
  {:left-items [(deftoolbar-item
                  :content [:p.subtitle.is-5
                            [:strong
                             (count @customer-list)]
                            " customers"])
                (deftoolbar-item
                  :type :div
                  :content
                  [inputs/button "New" #(set-subpage :new-customer)
                   :classes {:button "is-success"}])
                (deftoolbar-item
                  :class "is-hidden-table-only"
                  :content [inputs/search filter-customers
                            :placeholder "Name, email"])]
   :right-items [(deftoolbar-item
                   :content [listing-component :all])
                 (deftoolbar-item
                   :content [listing-component :with-orders])
                 (deftoolbar-item
                   :content [listing-component :without-orders])]})



(defn delete-customer-page []
  (fn []
    [:<>
     [breadcrumbs :ui.customers.page
      [{:name "Customers"
        :value :customers
        :active false}
       {:name "Delete Customer"
        :value :delete-customer
        :active true}]]
     [:p "Customer delete page goes here"]]))

(defn edit-customer-page []
  (fn []
    [:<>
     [breadcrumbs :ui.customers.page
      [{:name "Customers"
        :value :customers
        :active false}
       {:name "Edit Customer"
        :value :edit-customer
        :active true}]]
     [:p "Customer edit page goes here"]]))

(defn new-customer-page []
  (fn []
    [:<>
     [breadcrumbs :ui.customers.page
      [{:name "Customers"
        :value :customers
        :active false}
       {:name "New Customer"
        :value :new-customer
        :active true}]]
     [:p "Customer add page goes here"]]))

(defn customer-list-page []
  (store/reset! customer-list (customers->vec))
  (fn []
    [:<>
     [breadcrumbs :us.customers.page
      [{:name "Customers"
        :value :customers
        :active true}]]
     [toolbar (get-toolbar-data)]
     [:p "Customer listing page goes here"]]))

(defn customers-page []
  (case (get-subpage)
    :customers [customer-list-page]
    :new-customer [new-customer-page]
    :edit-customer [edit-customer-page]
    :delete-customer [delete-customer-page]
    [:<>
     [:h2.title "Page Not Found"]
     [:p "Bad page identifier " (str (get-subpage))]]))

(set-subpage :customers)
(set-listing-type :all)
