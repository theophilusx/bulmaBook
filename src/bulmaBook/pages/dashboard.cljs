(ns bulmaBook.pages.dashboard
  (:require [bulmaBook.components.inputs :as inputs]
            [bulmaBook.store :as store]
            [bulmaBook.components.basic :as basic]))

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
   [:div.columns.is-multiline
    [:div.column.is-12-tablet.is-6-desktop.is-3-widescreen
     [basic/notification [:<>
                          [:p.title.is-1 "232"]
                          [:p.subtitle.is-4 "Orders"]]
      :class "is-link has-text"]]
    [:div.column.is-12-tablet.is-6-desktop.is-3-widescreen
     [basic/notification [:<>
                          [:p.title.is-1 "$7,648"]
                          [:p.subtitle.is-4 "Revenue"]]
      :class "is-info has-text"]]
    [:div.column.is-12-tablet.is-6-desktop.is-3-widescreen
     [basic/notification [:<>
                          [:p.title.is-1 "1,678"]
                          [:p.subtitle.is-4 "Visitors"]]
      :class "is-primary has-text"]]
    [:div.column.is-12-tablet.is-6-desktop.is-3-widescreen
     [basic/notification [:<>
                          [:p.title.is-1 "20,756"]
                          [:p.subtitle.is-4 "Pages"]]
      :class "is-success has-text"]]]])
