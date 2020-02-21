(ns bulmaBook.pages.books
  (:require [bulmaBook.components.basic :refer [media]]
            [bulmaBook.components.paginate :refer [paginate]]
            [bulmaBook.components.toolbar :refer [deftoolbar-item toolbar]]
            [bulmaBook.components.modal :refer [modal-card]]
            [bulmaBook.components.form :as form]
            [bulmaBook.utils :refer [session-path]]
            [reagent.session :as session]))

(def new-book-id :ui.books.new-book)

(defn new-book []
  [modal-card [[:p "The body of the card goes here"]] new-book-id
   :header [[:p {:class "modal-card-title"} "Add Book"]
            [:button {:class "delete"
                      :aria-label "close"
                      :on-click #(session/assoc-in! (session-path new-book-id) false)}]]
   :footer [[:p "This is the footer section"]]])

(defn get-toolbar-data []
  {:left-items [(deftoolbar-item
                  :content [:p.subtitle.is-5
                            [:strong
                             (count (session/get-in [:data :book-data]))]
                            " books"])
                (deftoolbar-item
                  :type :div
                  :content [form/button "New" #(session/assoc-in! (session-path new-book-id) true)
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
     [new-book]
     [toolbar (get-toolbar-data)]
     [paginate books book-grid-component :page-size 2]]))
