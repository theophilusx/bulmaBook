(ns bulmaBook.pages.books
  (:require [bulmaBook.components.basic :refer [media]]
            [bulmaBook.components.paginate :refer [paginate]]
            [bulmaBook.components.toolbar :refer [deftoolbar-item toolbar]]
            [bulmaBook.components.form :as form]
            [reagent.session :as session]))

(defn get-toolbar-data []
  {:left-items [(deftoolbar-item
                  :content [:p.subtitle.is-5
                            [:strong
                             (count (session/get-in [:data :book-data]))]
                            " books"])
                (deftoolbar-item
                  :type :div
                  :content [form/button "New" #(println "Add new book")
                            :button-class "is-success"])
                (deftoolbar-item
                  :class "is-hidden-table-only"
                  :content [form/field
                            [[form/input :text :data.search
                              :placeholder "Book name, ISBN, author"]
                             [form/button "Search" #(println "do search")]]
                            :field-class "has-addons"])]
   :right-items [(deftoolbar-item
                   :content "Order by")
                 (deftoolbar-item
                   :content [:div.select
                             [:select
                              [:option "Publish date"]
                              [:option "Price"]
                              [:option "Page count"]]])]})

(defn book-component [book]
  [:article.box
   [media {:content [[:p.title.is-5.is-spaced.is-marginless
                      [:a {:href "#"} (:title book)]]
                     [:p.subtitle.is-marginless (:price book)]
                     [:div.content.is-small
                      (str (:pages book) " pages")
                      [:br]
                      (str "ISBN: " (:isbn book))
                      [:br]
                      [:a {:href "#"} "Edit"]
                      [:span "·"]
                      [:a {:href "#"} "Delete"]]]}
    :left {:content [[:img {:src (:image book) :width "80"}]]}]])

(defn book-grid-component [books]
  (into
   [:div.columns.is-multiline]
   (for [b books]
     [book-component b])))

(defn books-page []
  (let [books (session/get-in [:data :book-data])]
    [:div
     [:h2.title.is-2 (str "Page: " (session/get-in [:ui :books :sidebar]))]
     [toolbar (get-toolbar-data)]
     [paginate books book-grid-component :page-size 2]
     [:p "This is a default page. It will be replaced with real content later."]]))
