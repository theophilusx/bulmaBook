(ns bulmaBook.models
  (:require [bulmaBook.store :as store]))

(def state store/global-state)

(defn get-new-id [group]
  (case group
    :book (let [id (inc (store/get-in state [:data :book-counter]))]
            (store/update-in! state [:data :book-counter] inc)
            (keyword (str "bk" id)))
    :customer (let [id (inc (store/get-in state [:data :customer-counter]))]
                (store/update-in! state [:data :customer-counter] inc)
                (keyword (str "cs" id)))
    :order (let [id (inc (store/get-in state [:data :order-counter]))]
             (store/update-in! state [:data :order-counter] inc)
             (keyword (str "OR" id)))))

(defn book-data []
  (store/get-in store/global-state [:data :book-data]))

(defn books->vec []
  (let [books (book-data)]
    (mapv #(% books) (keys books))))

(defn get-book [bid]
  (bid (book-data)))

(defn add-book [book]
  (store/assoc-in! store/global-state [:data :book-data (:id book)] book))

(defn delete-book [bid]
  (store/update-in! store/global-state [:data :book-data] dissoc bid))

(defn customer-data []
  (store/get-in store/global-state [:data :customer-data]))


(defn customers->vec []
  (let [customers (customer-data)]
    (mapv #(% customers) (keys customers))))

(defn get-customer [cid]
  (cid (customer-data)))

(defn delete-customer [cid]
  (store/update-in! store/global-state [:data :customer-data] dissoc cid))

(defn add-customer [cus]
  (store/assoc-in! store/global-state [:data :customer-data (:id cus)] cus))

(defn order-data []
  (store/get-in store/global-state [:data :order-data]))

(defn orders->vec []
  (let [orders (order-data)]
    (mapv (fn [oid]
            (let [order (oid orders)
                  customer (get-customer (:cid order))]
              {:id oid
               :cid (:cid order)
               :customer (str (:first-name customer) " "
                              (:last-name customer))
               :date (:date order)
               :book-count (reduce (fn [acc bid]
                                     (+ acc (:quantity (bid (:books order)))))
                                   0 (keys (:books order)))
               :status (:status order)
               :total (reduce (fn [acc bid]
                                (+ acc (* (:quantity (bid (:books order)))
                                          (:cost (bid (:books order))))))
                              0 (keys (:books order)))}))
          (keys orders))))

(defn get-order [oid]
  (oid (order-data)))

(defn get-customer-orders [cid]
  (filterv #(= cid (:cid %)) (orders->vec)))