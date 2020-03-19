(ns bulmaBook.pages.orders
  (:require [reagent.core :as r]
            [bulmaBook.models :as models]
            [bulmaBook.components.basic :refer [breadcrumbs]]
            [bulmaBook.components.toolbar :refer [toolbar deftoolbar-item]]
            [bulmaBook.components.inputs :as inputs]
            [bulmaBook.components.tables :as tables]
            [bulmaBook.pages.ui :as ui]
            [bulmaBook.utils :as utils]
            [bulmaBook.store :as store]
            [clojure.string :as string]))

(def orders-list (r/atom (models/orders->vec)))

(defn listing-component [type]
  (if (= type (ui/get-listing-type :orders))
    [:strong (utils/keyword->str type :initial-caps true)]
    [:a {:href "#"
         :on-click #(ui/set-listing-type :orders type)}
     (utils/keyword->str type :initial-caps true)]))

(defn filter-orders [term]
  (store/reset! orders-list
                (filterv
                 (fn [o]
                   (or (string/includes? (str (:id o)) term)
                       (string/includes? (str (:cid o)) term)
                       (string/includes? (str (:customer o)) term)
                       (string/includes? (str (:date o)) term)
                       (string/includes? (str (:status o)) term)
                       (string/includes? (str (:book-count o)) term)
                       (string/includes? (str (:total o)) term)))
                 (models/orders->vec))))

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

(defn order-button [type doc]
  [:button.button {:on-click #(store/put! doc :status type)
                   :class ["is-small"
                           (case type
                             :in-progress "is-warning"
                             :complete "is-success"
                             :failed "is-danger")
                           (when-not (= type (:status @doc))
                             "is-outlined")]}
   (utils/keyword->str type :initial-caps true)])

(defn order-buttons [doc]
  (into
   [:div.buttons]
   (for [b [:in-progress :complete :failed]]
     [order-button b doc])))

(defn books-table [doc]
  (let [body (mapv (fn [b]
                     (let [book (models/get-book (:id b))]
                       [(tables/defcell [:img {:src (:image book) :width "40"}])
                        (tables/defcell [:strong (:title book)])
                        (tables/defcell (str "$" (:cost book))
                          :class "has-text-right")
                        (tables/defcell "1" :class "has-text-right")
                        (tables/defcell (str "$" (:cost book))
                          :class "has-text-right")]))
                   (:books @doc))
        head [[(tables/defcell "Cover" :type :th :class "is-narrow")
               (tables/defcell "Title" :type :th)
               (tables/defcell "Price" :type :th
                 :class "has-text-right is-narrow")
               (tables/defcell "Quantity" :type :th
                 :class "has-text-right is-narrow")
               (tables/defcell "Total" :type :th
                 :class "has-text-right is-narrow")]]
        foot [[(tables/defcell (str "$" (reduce (fn [acc bk]
                                                  (+ acc
                                                     (*
                                                      (:cost bk) (:quantity bk))))
                                                0 (:books @doc))) :type :th
                 :colspan "5" :class "has-text-right")]]]
    [tables/table body :header head :footer foot
     :borded true :fullwidth true]))

(defn order-edit-form []
  (let [doc (r/atom (models/get-order (ui/get-target :orders)))
        customer (models/get-customer (:cid @doc))]
    (fn []
      [:div.columns.is-desktop
       [:div.column.is-4-desktop.is-3-widescreen
        [:p.heading [:strong "Date"]]
        [:p.content (:date @doc)]
        [:p.heading [:strong "Status"]]
        [order-buttons doc]
        [:p.heading [:strong "Customer"]]
        [:p.contenr
         [:strong (str (:first-name customer) " " (:last-name customer))]
         [:br] [:code (:email customer)]
         [:br] (str (:address1 customer) " " (:address2 customer))
         [:br] (str (:city customer) " " (:pcode customer))
         [:br] (:country customer)]]
       [:div.column
        [:p.heading [:strong "Books"]]
        [books-table doc]]])))

(defn order-edit-page []
  [:<>
   [breadcrumbs :ui.orders.page
    [{:name "Orders"
      :value :orders
      :active false}
     {:name "Edit Order"
      :value :edit-order
      :active true}]]
   [order-edit-form]])

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
