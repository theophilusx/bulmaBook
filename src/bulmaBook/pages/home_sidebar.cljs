(ns bulmaBook.pages.home-sidebar
  (:require [bulmaBook.components.vertical-menu :as vm]))

(defn sidebar-component []
  [vm/menu
   :sidebar-menu
   (vm/defitem
     :type :menu
     :title "Menu"
     :items
     [(vm/defitem :title "Dashboard" :icon-img "fa-tachometer" :id :dashboard)
      (vm/defitem :title "Books" :icon-img "fa-book" :id :books)
      (vm/defitem :title "Customers" :icon-img "fa-address-book" :id :customers)
      (vm/defitem :title "Orders" :icon-img "fa-file-text-o" :id :orders)])])
