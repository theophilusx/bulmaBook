(ns bulmaBook.pages.dashboard
  (:require [bulmaBook.components.basic :refer [breadcrumbs]]
            [bulmaBook.components.inputs :as inputs]))

(defn dashboard-page []
  [:<>
   [breadcrumbs :ui.dashboard.page
    [{:name "Dashboard"
      :value :dashboard
      :active true}]]
   [:h2 "Default dashboard page"]
   [inputs/file :data.file :is-boxed true :classes {:file "is-primary"}]])
