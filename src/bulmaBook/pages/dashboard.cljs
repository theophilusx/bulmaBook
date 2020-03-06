(ns bulmaBook.pages.dashboard
  (:require [bulmaBook.components.basic :refer [breadcrumbs]]
            [bulmaBook.components.form :as form]))

(defn dashboard-page []
  [:div
   [breadcrumbs :ui.dashboard.page
    [{:name "Dashboard"
      :value :dashboard
      :active true}]]
   [:h2 "Default dashboard page"]
   [form/file :data.file :is-boxed true :classes {:file "is-primary"}]])
