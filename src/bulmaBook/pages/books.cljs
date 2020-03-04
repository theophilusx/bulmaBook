(ns bulmaBook.pages.books
  (:require [bulmaBook.components.basic :refer [media breadcrumbs breadcrumbs]]
            [bulmaBook.components.paginate :refer [paginate]]
            [bulmaBook.components.toolbar :refer [deftoolbar-item toolbar]]
            [bulmaBook.components.form :as form]
            [reagent.session :as session]
            [reagent.core :as r]
            [clojure.string :as string]))

(def book-list (r/atom (session/get-in [:data :book-data])))
(def new-book-id :ui.books.new-book)

(defn save-new-book []
  (let [bk {:title (session/get-in! [:data :new-book :title])
            :image (session/get-in! [:data :new-book :image])
            :cost (session/get-in! [:data :new-book :cost])
            :pages (session/get-in! [:data :new-book :pages])
            :isbn (session/get-in! [:data :new-book :isbn])}]
    (session/update-in! [:data :book-data] conj bk)
    (reset! book-list (session/get-in [:data :book-data]))
    (session/assoc-in! [:ui :books :page] :books)))

(defn clear-new-book []
  (session/assoc-in! [:data :new-book] {})
  (session/assoc-in! [:ui :books :page] :books))

(defn new-book-page []
  [:div
   [breadcrumbs :ui.books.page
    [{:name "Books"
      :value :books
      :active false}
     {:name "New Book"
      :value :new-book
      :active true}]]
   [:form.box
    [form/horizontal-field "Title" [[form/input :text :data.new-book.title]]]
    [form/horizontal-field "Image" [[form/input :text :data.new-book.image]]]
    [form/horizontal-field "Cost" [[form/input :text :data.new-book.cost]]]
    [form/horizontal-field "Pages" [[form/input :text :data.new-book.pages]]]
    [form/horizontal-field "ISBN" [[form/input :text :data.new-book.isbn]]]
    [form/horizontal-field nil
     [[form/button "Save" save-new-book :button-class "is-success"]
      [form/button "Clear" clear-new-book]]]]])

;; (defn edit-book-page [bk]
;;   [:div
;;    [breadcrumbs :ui.books.page
;;     [{:name "Books"
;;       :value :books
;;       :active false}
;;      {:name "Edit Book"
;;       :value :edit
;;       :active true}]]
;;    [:form.box
;;     [form/horizontal-field "Title" [form/editable-field nil :title :text]]
;;     [form/horizontal-field "Image" [form/editable-field nil :image :text]]
;;     [form/horizontal-field "Cost" [form/editable-field nil :cost :text ]]
;;     [form/horizontal-field "Pages" [form/editable-field nil :pages :text]]
;;     [form/horizontal-field "ISBN" [form/editable-field nil :isbn :text]]]])

(defn filter-books [search-data]
  (println (str "searching for: " search-data))
  (reset! book-list (filterv
                     (fn [m]
                       (or (string/includes? (str (:title m)) search-data)
                           (string/includes? (str (:cost m)) search-data)
                           (string/includes? (str (:pages m)) search-data)
                           (string/includes? (str (:isbn m)) search-data)))
                     (session/get-in [:data :book-data]))))

(defn get-toolbar-data []
  {:left-items [(deftoolbar-item
                  :content [:p.subtitle.is-5
                            [:strong
                             (count (session/get-in [:data :book-data]))]
                            " books"])
                (deftoolbar-item
                  :type :div
                  :content [form/button "New" #(session/assoc-in!
                                                [:ui :books :page] :new-book)
                            :button-class "is-success"])
                (deftoolbar-item
                  :class "is-hidden-table-only"
                  :content [form/field
                            [[form/input :text :data.search
                              :placeholder "Book name, ISBN, author"]
                             [form/button "Search"
                              #(filter-books (session/get-in [:data :search]))]]
                            :field-class "has-addons"])]
   :right-items [(deftoolbar-item
                   :content "Order by")
                 (deftoolbar-item
                   :content [form/select-field :data.sort
                             [[form/option "Title" :title]
                              [form/option "Price" :cost]
                              [form/option "Page Count" :pages]
                              [form/option "ISBN" :isbn]]])]})

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
  (reset! book-list (session/get-in [:data :book-data]))
  (fn []
    (when (session/get-in [:data :sort])
      (reset! book-list (vec (sort-by (session/get-in [:data :sort])
                                      (session/get-in [:data :book-data])))))
    [:div
     [breadcrumbs :ui.books.page
      [{:name "Books"
        :value :books
        :active true}]]
     [toolbar (get-toolbar-data)]
     [paginate @book-list book-grid-component :page-size 2]]))
