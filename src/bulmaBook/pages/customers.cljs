(ns bulmaBook.pages.customers
  (:require [bulmaBook.components.toolbar :refer [deftoolbar-item toolbar]]
            [bulmaBook.components.inputs :as inputs]
            [reagent.core :as r]
            [bulmaBook.store :as store]
            [bulmaBook.utils :as utils]
            [bulmaBook.components.basic :refer [breadcrumbs]]
            [bulmaBook.components.tables :as tables]
            [bulmaBook.components.icons :as icons]))

(def customer-list (r/atom {}))

(defn customer-data []
  (store/get-in store/global-state [:data :customer-data]))

(defn customers->vec []
  (let [customers (customer-data)]
    (mapv #(% customers) (keys customers))))

(defn get-new-customer-id []
  (let [id (inc (store/get-in store/global-state [:data :customer-counter]))]
    (store/update-in! store/global-state [:data :customer-counter] inc)
    (keyword (str "cs" id))))

(defn get-customer [cid]
  (cid (customer-data)))

(defn set-subpage [page]
  (store/assoc-in! store/global-state [:ui :customers :page] page))

(defn get-subpage []
  (store/get-in store/global-state [:ui :customers :page]))

(defn set-listing-type [type]
  (store/assoc-in! store/global-state [:ui :customers :listing] type))

(defn get-listing-type []
  (store/get-in store/global-state [:ui :customers :listing]))

(defn set-customer-target [cid]
  (store/assoc-in! store/global-state [:ui :customers :target] cid))

(defn get-customer-target []
  (store/get-in store/global-state [:ui :customers :target]))



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



(defn customer-form-fields [doc]
  [:<>
   [:div.columns
    [:div.column.is-one-quarter
     [inputs/input-field "Title" :text :title
      :placeholder "e.g. Ms, Mr, Dr" :model doc
      :classes {:input "is-large"}]]
    [:div.column
     [inputs/input-field "First Name" :text :first-name
      :placeholder "Given name" :required true :model doc
      :classes {:input "is-large"}]]
    [:div.column
     [inputs/input-field "Last Name" :text :last-name
      :placeholder "Family name" :required true :model doc
      :classes {:input "is-large"}]]]
   [inputs/input-field "Email" :email :email :model doc :required true
    :placeholder "Email address"
    :idon-data (icons/deficon "fa-envelope" :position :left :size :small )]
   [inputs/field [[inputs/input :text :address1 :model doc
                   :placeholder "Address line 1" :required true]
                  [inputs/input :text :address2 :model doc
                   :placeholder "Address line 2 (optional)"]]
    :label "Address"]
   [:div.columns
    [:div.column
     [inputs/input-field "Postcode/Zipcode" :text :pcode :model doc
      :placeholder "e.g. 2350" :required true]]
    [:div.column
     [inputs/input-field "City" :text :city :model doc :required true
      :placeholder "e.g. Armidale"]]
    [:div.column
     [inputs/select-field :country
      [[inputs/option "-- Choose a country --" :value ""]
       [inputs/option "Australia"]
       [inputs/option "United Kingdom"]
       [inputs/option "United States"]]
      :title "Country" :model doc]]]])

(defn do-delete [cid]
  (set-customer-target cid)
  (set-subpage :delete-customer))

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

(defn do-edit [cid]
  (set-customer-target cid)
  (set-subpage :edit-customer))

(defn save-edit-customer [customer]
  (store/assoc-in! store/global-state [:data :customer-data (:id @customer)]
                   @customer)
  (store/clear! customer)
  (set-customer-target nil)
  (set-subpage :customers))

(defn cancel-edit-customer []
  (set-customer-target nil)
  (set-subpage :customers))

(defn edit-customer-form []
  (let [doc (r/atom (get-customer (get-customer-target)))]
    (fn []
      [:form.box
       [customer-form-fields doc]
       [inputs/field [[inputs/button "Save" #(save-edit-customer doc)
                       :classes {:button "is-success"}]
                      [inputs/button "Cancel" #(cancel-edit-customer)]]
        :classes {:field "has-addons"}]])))

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
     [edit-customer-form]]))

(defn save-new-customer [data]
  (let [new-id (get-new-customer-id)]
    (store/put! data :id new-id)
    (store/assoc-in! store/global-state [:data :customer-data new-id] @data)
    (store/clear! data)
    (set-subpage :customers)))

(defn cancel-new-customer [data]
  (store/clear! data)
  (set-subpage :customers))

(defn new-customer-form []
  (let [doc (r/atom {})]
    (fn []
      [:form.box
       [customer-form-fields doc]
       [inputs/field [[inputs/button "Save" #(save-new-customer doc)
                       :classes {:button "is-success"}]
                      [inputs/button "Cancel" #(cancel-new-customer doc)]]
        :classes {:field "has-addons"}]])))

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
     [new-customer-form]]))

(defn customer-table [customer-data]
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
                        customer-data)
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
     [customer-table @customer-list]]))

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
