(ns bulmaBook.pages.dashboard
  (:require [bulmaBook.components.basic :refer [breadcrumbs render-map]]
            [bulmaBook.components.inputs :as inputs]
            [reagent.core :as r]))

(defn input-sample-form []
  (let [doc (r/atom {})]
    (fn []
      [:form.box
       [inputs/input-field "Text sample" :text :text-sample :model doc]
       [inputs/input-field "Email sample" :email :email-sample :model doc]
       [inputs/input-field "Colour sample" :color :color-sample :size 4 :model doc]
       [inputs/input-field "Date sample" :date :date-sample :size 12 :model doc]
       [inputs/input-field "Month sample" :month :month-sample :size "10" :model doc]
       [inputs/number-field  :number-sample :size 2 :maxlength 4 :max 99 :min 0
        :model doc :label "Number sample" :value 10]
       [inputs/range-field :range-field-sample 0 100 :step 10 :value 60
        :model doc :label "Sample range field"]
       [:hr]
       [render-map @doc]])))

(defn dashboard-page []
  [:<>
   [breadcrumbs :ui.dashboard.page
    [{:name "Dashboard"
      :value :dashboard
      :active true}]]
   [:h2 "Default dashboard page"]
   [inputs/file :data.file :is-boxed true :classes {:file "is-primary"}]
   [:hr]
   [input-sample-form]])
