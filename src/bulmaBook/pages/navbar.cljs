(ns bulmaBook.pages.navbar
  (:require [bulmaBook.components.navbar :refer [navbar]]
            [bulmaBook.data :as data]))

(defn top-navbar []
  (let [data (data/get-navabar-data)]
    [navbar data]))
