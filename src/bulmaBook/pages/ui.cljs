(ns bulmaBook.pages.ui
  (:require [theophilusx.yorick.store :as store]))

(defn set-subpage [group page]
  (store/assoc-in! store/global-state [:ui group :page] page))

(defn get-subpage [group]
  (store/get-in store/global-state [:ui group :page]))

(defn set-target [group target]
  (store/assoc-in! store/global-state [:ui group :target] target))

(defn get-target [group]
  (store/get-in store/global-state [:ui group :target]))

(defn set-listing-type [group type]
  (store/assoc-in! store/global-state [:ui group :listing] type))

(defn get-listing-type [group]
  (store/get-in store/global-state [:ui group :listing]))

(defn set-sidebar [item]
  (store/assoc-in! store/global-state [:ui :sidebar] item))

(defn get-sidebar []
  (store/get-in store/global-state [:ui :sidebar]))
