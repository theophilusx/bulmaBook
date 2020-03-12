(ns bulmaBook.models
  (:require [bulmaBook.store :as store]))

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
