(ns bulmaBook.pages.dashboard
  (:require [bulmaBook.components.inputs :as inputs]
            [bulmaBook.store :as store]))

(defn dashboard-page []
  [:<>
   [:h1.title "Dashboard"]
   [:div.level
    [:div.level-left
     [:h1.subtitle.is-3
      [:span.has-text-grey-light "Hello "]
      [:strong (store/get-in store/global-state [:session :user :name])]]]
    [:div.leve-right
     [inputs/select :ui.dashboard.period
      [(inputs/defoption "Today")
       (inputs/defoption "Yesterday")
       (inputs/defoption "This Week" :value "week")
       (inputs/defoption "This Month" :value "month")
       (inputs/defoption "This Year" :value "year")
       (inputs/defoption "All Time" :value "all")]
      :selected "month" :model store/global-state]]]
   ])
