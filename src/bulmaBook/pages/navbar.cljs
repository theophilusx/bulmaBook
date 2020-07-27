(ns bulmaBook.pages.navbar
  (:require [theophilusx.yorick.navbar :refer [navbar]]
            [bulmaBook.data :as data]))

(defn top-navbar []
  (let [data (data/get-navabar-data)]
    [navbar data]))
