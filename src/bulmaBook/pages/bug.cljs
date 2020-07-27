(ns bulmaBook.pages.bug
  (:require [theophilusx.yorick.store :as store]
            [theophilusx.yorick.utils :refer [spath]]
            [theophilusx.yorick.input :as inputs]
            [bulmaBook.data :as data]
            [reagent.core :as r]))

(defn do-report [place]
  (let [idx (count (store/get-in store/global-state [:data :bug-reports]))]
    (store/assoc-in! store/global-state
                     [:data :bug-reports (keyword (str "bug-" idx))] @place)
    (store/clear! place)
    (store/assoc-in! store/global-state (spath data/navbar-id) :home)))

(defn bug-form []
  (let [doc (r/atom {})]
    (fn []
      [:form.box
       [inputs/horizontal-field "Subject"
        [[inputs/input :text :subject :modal doc]]]
       [inputs/horizontal-field "Expected Behaviour"
        [[inputs/textarea nil :expected
          :placeholder "What did you expect to happen?" :modal doc]]]
       [inputs/horizontal-field "Actual Behaviour"
        [[inputs/textarea nil :actual :placeholder "What did you observe?"
          :modal doc]]]
       [inputs/button "Submit" #(do-report doc) :classes {:button "is-success"}]])))

(defn bug-report []
  [:section
   [:div.container
    [:div.columns.is-centered
     [:div.column.is-7-tablet.is-6-desktop.is-5-widescreen
      [:h2.title.is-2 "Bug Report"]
      [:p (str "When making a bug report, please include the following;")]
      [:div.content
       [:ul
        [:li "Platform and browser version"]
        [:li "Minimal set of steps to follow to reproduce the issue"]
        [:li "Any special or specific configuration used"]]]
      [bug-form]]]]])
