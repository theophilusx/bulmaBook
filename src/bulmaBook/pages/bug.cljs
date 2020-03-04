(ns bulmaBook.pages.bug
  (:require [reagent.session :as session]
            [bulmaBook.utils :refer [spath]]
            [bulmaBook.components.form :as form]
            [bulmaBook.data :as data]))

(defn do-report []
  (let [idx (count (session/get-in [:data :bug-reports]))]
    (session/assoc-in! [:data :bug-reports (keyword (str "bug-" idx))]
                       {:subject (session/get-in [:bug :subject])
                        :expected (session/get-in [:bug :expected])
                        :actual (session/get-in [:bug :actual])
                        :reproduce (session/get-in [:bug :reproduce])})
    (session/remove! :bug)
    (session/assoc-in! (spath data/navbar-id) :home)))

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
      [form/horizontal-field "Subject"
       [[form/input :text :bug.subject]]]
      [form/horizontal-field "Expected Behaviour"
       [[form/textarea nil :bug.expected :placeholder "What did you expect to happen?"]]]
      [form/horizontal-field "Actual Behaviour"
       [[form/textarea nil :bug.actual :placeholder "What did you observe?"]]]
      [form/button "Submit" do-report :button-class "is-primary"]]]]])
