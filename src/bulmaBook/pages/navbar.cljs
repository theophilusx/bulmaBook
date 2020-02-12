(ns bulmaBook.pages.navbar
  (:require [bulmaBook.components.navbar :refer [navbar]]
            [bulmaBook.data :as data]
            [reagent.session :as session]))

(defn top-navbar []
  (let [email (session/get-in [:session :user :email])
        data (if email data/navbar-data data/login-navbar)]
    [navbar data]))
