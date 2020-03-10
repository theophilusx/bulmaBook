(ns bulmaBook.pages.customers
  (:require [bulmaBook.components.toolbar :refer [deftoolbar-item toolbar]]
            [bulmaBook.components.inputs :as inputs]
            [reagent.core :as r]
            [bulmaBook.store :as store]
            [bulmaBook.utils :as utils]
            [bulmaBook.components.basic :refer [breadcrumbs]]
            [bulmaBook.components.tables :as tables]))

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

(defn set-edit-customer [cid]
  (store/assoc-in! store/global-state [:ui :customers :edit] cid))

(defn get-edit-customer []
  (store/get-in store/global-state [:ui :customers :edit]))

(defn set-delete-customer [cid]
  (store/assoc-in! store/global-state [:ui :customers :delete] cid))

(defn get-delete-customer []
  (store/get-in store/global-state [:ui :customers :delete]))

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

(defn do-edit [cid]
  (set-edit-customer cid)
  (set-subpage :edit-customer))

(defn do-delete [cid]
  (set-delete-customer cid)
  (set-subpage :delete-customer))

(defn customer-table []
  (let [customers (mapv (fn [c]
                          [(tables/defcell
                             [:a {:href "#"
                                  :on-click #(do-edit (:id c))}
                              (str (:first-name c) " " (:last-name c))])
                           (tables/defcell (str (:email c)))
                           (tables/defcell (str (:country c)))
                           (tables/defcell (str 0))
                           (tables/defcell
                             [inputs/field [[inputs/button "Edit"
                                             #(do-edit (:id c))
                                             :classes
                                             {:button "is-success is-small"}]
                                            [inputs/button "Delete"
                                             #(do-delete (:id c))
                                             :classes
                                             {:button "is-warning is-small"}]]
                              :classes {:field "has-addons"}])])
                        (customers->vec))
        header [(mapv (fn [h]
                        (tables/defcell h :type :th))
                      ["Name" "Email" "Country" "Orders ""Action"])]]
    [tables/table customers :header header :footer header :borded true
     :hover true :narrow true :fullwidth true :striped true]))

(defn customer-list-page []
  (store/reset! customer-list (customers->vec))
  (fn []
    [:<>
     [breadcrumbs :us.customers.page
      [{:name "Customers"
        :value :customers
        :active true}]]
     [toolbar (get-toolbar-data)]
     [customer-table]]))

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
