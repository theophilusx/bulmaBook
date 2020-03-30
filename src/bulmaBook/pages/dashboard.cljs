(ns bulmaBook.pages.dashboard
  (:require [bulmaBook.components.inputs :as inputs]
            [bulmaBook.store :as store]
            [bulmaBook.components.basic :as basic]
            [bulmaBook.models :as models]
            [bulmaBook.utils :as utils]))

(defn dashboard-page []
  (let [stats (models/dashboard-period-data (models/dashboard-period))]
    [:<>
     [:h1.title "Dashboard"]
     [:div.level
      [:div.level-left
       [:h1.subtitle.is-3
        [:span.has-text-grey-light "Hello "]
        [:strong (models/session-user)]]]
      [:div.leve-right
       [inputs/select :ui.dashboard.period
        [(inputs/defoption "Today" :value "today")
         (inputs/defoption "Yesterday" :value "yesterday")
         (inputs/defoption "This Week" :value "week")
         (inputs/defoption "This Month" :value "month")
         (inputs/defoption "This Year" :value "year")
         (inputs/defoption "All Time" :value "all")]
        :selected "month" :model store/global-state]]]
     [:div.columns.is-multiline
      [:div.column.is-12-tablet.is-6-desktop.is-3-widescreen
       [basic/notification [:<>
                            [:p.title.is-1
                             (utils/number-thousands (str (:orders stats)))]
                            [:p.subtitle.is-4 "Orders"]]
        :class "is-link has-text"]]
      [:div.column.is-12-tablet.is-6-desktop.is-3-widescreen
       [basic/notification [:<>
                            [:p.title.is-1
                             (str "$" (utils/number-thousands
                                       (str (:revenue stats))))]
                            [:p.subtitle.is-4 "Revenue"]]
        :class "is-info has-text"]]
      [:div.column.is-12-tablet.is-6-desktop.is-3-widescreen
       [basic/notification [:<>
                            [:p.title.is-1
                             (utils/number-thousands (str (:visitors stats)))]
                            [:p.subtitle.is-4 "Visitors"]]
        :class "is-primary has-text"]]
      [:div.column.is-12-tablet.is-6-desktop.is-3-widescreen
       [basic/notification [:<>
                            [:p.title.is-1
                             (utils/number-thousands (str (:pages stats)))]
                            [:p.subtitle.is-4 "Pages"]]
        :class "is-success has-text"]]]]))
