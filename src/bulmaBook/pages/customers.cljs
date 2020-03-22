(ns bulmaBook.pages.customers
  (:require [bulmaBook.components.toolbar :refer [deftoolbar-item toolbar]]
            [bulmaBook.components.inputs :as inputs]
            [reagent.core :as r]
            [bulmaBook.store :as store]
            [bulmaBook.utils :as utils]
            [bulmaBook.components.basic :refer [breadcrumbs]]
            [bulmaBook.components.tables :as tables]
            [bulmaBook.components.icons :as icons]
            [clojure.string :as string]
            [bulmaBook.models :as models]
            [bulmaBook.pages.ui :as ui]))

(def customer-list (r/atom {}))

(defn filter-customers [term]
  (store/reset! customer-list
                (filterv
                 (fn [cus]
                   (or (string/includes? (str (:title cus)) term)
                       (string/includes? (str (:first-name cus)) term)
                       (string/includes? (str (:last-name cus)) term)
                       (string/includes? (str (:first-name cus) " "
                                              (:last-name cus)) term)
                       (string/includes? (str (:address1 cus)) term)
                       (string/includes? (str (:address2 cus)) term)
                       (string/includes? (str (:pcode cus)) term)
                       (string/includes? (str (:city cus)) term)
                       (string/includes? (str (:country cus)) term)))
                 (models/customers->vec))))

(defn listing-component [type]
  (if (= type (ui/get-listing-type :customers))
    [:strong (utils/keyword->str type :initial-caps true)]
    [:a {:href "#"
         :on-click #(ui/set-listing-type :customers type)}
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
                  [inputs/button "New" #(ui/set-subpage :customers :new-customer)
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
      [(inputs/defoption "-- Choose a country --" :value "" :selected true)
       (inputs/defoption "Australia")
       (inputs/defoption "United Kiingdom")
       (inputs/defoption "United States")]
      :title "Country" :model doc]]]])

(defn customer-display []
  (let [customer (models/get-customer (ui/get-target :customers))]
    [:<>
     [:h4.title (str (:title customer) " " (:first-name customer) " "
                     (:last-name customer))]
     [:div.columns
      [:div.column.is-1
       [:p [:strong "Email"]]]
      [:div.column
       [:p (:email customer)]]]
     [:div.columns
      [:div.column.is-1
       [:p [:strong "Address"]]]
      [:div.column
       [:p (:address1 customer)]
       (when (:address2 customer)
         [:p (:address2 customer)])
       [:p (str (:city customer) " " (:pcode customer))]
       [:p (:country customer)]]]]))

(defn do-delete [cid]
  (ui/set-target :customers cid)
  (ui/set-subpage :customers :delete-customer))

(defn delete-customer []
  (models/delete-customer (ui/get-target :customers))
  (ui/set-target :customers nil)
  (ui/set-subpage :customers :customers))

(defn cancel-delete-customer []
  (ui/set-target :customers nil)
  (ui/set-subpage :customers :customers))

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
     [:div.box
      [customer-display]
      [inputs/field [[inputs/button "Delete" #(delete-customer)
                      :classes {:button "is-warning"}]
                     [inputs/button "Cancel" #(cancel-delete-customer)]]
       :classes {:field "has-addons"}]]]))

(defn do-edit [cid]
  (ui/set-target :customers cid)
  (ui/set-subpage :customers :edit-customer))

(defn save-edit-customer [customer]
  (models/add-customer @customer)
  (store/clear! customer)
  (ui/set-target :customers nil)
  (ui/set-subpage :customers :customers))

(defn cancel-edit-customer []
  (ui/set-target :customers nil)
  (ui/set-subpage :customers :customers))

(defn edit-customer-form []
  (let [doc (r/atom (models/get-customer (ui/get-target :customers)))]
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
  (let [new-id (models/get-new-id :customer)]
    (store/put! data :id new-id)
    (models/add-customer @data)
    (store/clear! data)
    (ui/set-subpage :customers :customers)))

(defn cancel-new-customer [data]
  (store/clear! data)
  (ui/set-subpage :customers :customers))

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
                           (tables/defcell
                             (str (count (models/get-customer-orders (:id c)))))
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
  (store/reset! customer-list (models/customers->vec))
  (fn []
    [:<>
     [:h1.title "Customers"]
     [toolbar (get-toolbar-data)]
     [customer-table @customer-list]]))

(defn customers-page []
  (case (ui/get-subpage :customers)
    :customers [customer-list-page]
    :new-customer [new-customer-page]
    :edit-customer [edit-customer-page]
    :delete-customer [delete-customer-page]
    [:<>
     [:h2.title "Page Not Found"]
     [:p "Bad page identifier " (str (ui/get-subpage :customers))]]))

(ui/set-subpage :customers :customers)
(ui/set-listing-type :customers :all)
