(ns bulmaBook.pages.books
  (:require [bulmaBook.components.basic :refer [media breadcrumbs]]
            [bulmaBook.components.paginate :refer [paginate]]
            [bulmaBook.components.toolbar :refer [deftoolbar-item toolbar]]
            [bulmaBook.components.inputs :as inputs]
            [bulmaBook.store :as store]
            [bulmaBook.models :as models]
            [bulmaBook.pages.ui :as ui]
            [reagent.core :as r]
            [clojure.string :as string]))

(def book-list (r/atom {}))

(defn get-sort-field []
  (store/get-in store/global-state [:ui :books :sort]))

(defn filter-books [search-term]
  (store/reset! book-list (filterv
                           (fn [bk]
                             (or (string/includes? (str (:title bk)) search-term)
                                 (string/includes? (str (:cost bk)) search-term)
                                 (string/includes? (str (:pages bk)) search-term)
                                 (string/includes? (str (:isbn bk)) search-term)))
                           (models/books->vec))))

(defn book-fields [doc]
  [:<>
   [inputs/horizontal-field "Title" [[inputs/input :text :title :model doc]]]
   [inputs/horizontal-field "Image" [[inputs/input :text :image :model doc]]]
   [inputs/horizontal-field "Cost" [[inputs/input :text :cost :model doc]]]
   [inputs/horizontal-field "Pages" [[inputs/input :text :pages :model doc]]]
   [inputs/horizontal-field "ISBN" [[inputs/input :text :isbn :model doc]]]])

(defn save-new-book [book]
  (let [new-id (models/get-new-id :books)]
    (store/put! book :id new-id)
    (models/add-book @book)
    (store/clear! book)
    (ui/set-subpage :books :books)))

(defn clear-new-book [book]
  (store/clear! book)
  (ui/set-subpage :books :books))

(defn new-book-form []
  (let [doc (r/atom {})]
    (fn []
      [:form.box
       [book-fields doc]
       [inputs/field [[inputs/button "Save" #(save-new-book doc)
                       :classes {:button "is-success"}]
                      [inputs/button "Cancel" #(clear-new-book doc)]]
        :classes {:field "has-addons"}]])))

(defn new-book-page []
  [:<>
   [breadcrumbs :ui.books.page
    [{:name "Books"
      :value :books
      :active false}
     {:name "New Book"
      :value :new-book
      :active true}]]
   [new-book-form]])

(defn do-edit-book [bid]
  (ui/set-target :books bid)
  (ui/set-subpage :books :edit-book))

(defn save-edit-book [book]
  (models/add-book @book)
  (store/clear! book)
  (ui/set-target :books nil)
  (ui/set-subpage :books :books))

(defn cancel-edit-book [book]
  (store/clear! book)
  (ui/set-target :books nil)
  (ui/set-subpage :books :books))

(defn edit-book-form []
  (let [doc (r/atom (models/get-book (ui/get-target :books)))]
    (fn []
      [:form.box
       [book-fields doc]
       [inputs/field [[inputs/button "Save Changes" #(save-edit-book doc)
                       :classes {:button "is-success"}]
                      [inputs/button "Cancel" #(cancel-edit-book doc)]]
        :classes {:field "has-addons"}]])))

(defn edit-book-page [bid]
  [:<>
   [breadcrumbs :ui.books.page
    [{:name "Books"
      :value :books
      :active false}
     {:name "Edit Book"
      :value :edit-book
      :active true}]]
   [edit-book-form bid]])

(defn do-delete-book [bid]
  (ui/set-target :books bid)
  (ui/set-subpage :books :delete-book))

(defn delete-book-page []
  (let [book (models/get-book (ui/get-target :books))]
    [:<>
     [breadcrumbs :ui:books:page
      [{:name "Books"
        :value :books
        :active false}
       {:name "Delete Book"
        :value :delete-book
        :active true}]]
     [:table.table
      [:tbody
       [:tr [:th "Title"] [:td (:title book)]]
       [:tr [:th "Cover"] [:td [:img {:src (:image book) :width "80"}]]]
       [:tr [:th "Cost"] [:td (:cost book)]]
       [:tr [:th "Pages"] [:td (:pages book)]]
       [:tr [:th "ISBN"] [:td (:isbn book)]]]]
     [inputs/field [[inputs/button "Delete Book"
                     (fn []
                       (models/delete-book (ui/get-target :books))
                       (ui/set-target :books nil)
                       (ui/set-subpage :books :books))
                     :classes {:button "is-success"}]
                    [inputs/button "Cancel"
                     (fn []
                       (ui/set-target :books nil)
                       (ui/set-subpage :books :books))]]
      :classes {:field "has-addons"}]]))

(defn get-toolbar-data []
  {:left-items [(deftoolbar-item
                  :content [:p.subtitle.is-5
                            [:strong
                             (count @book-list)]
                            " books"])
                (deftoolbar-item
                  :type :div
                  :content
                  [inputs/button "New" #(ui/set-subpage :books :new-book)
                   :classes {:button "is-success"}])
                (deftoolbar-item
                  :class "is-hidden-table-only"
                  :content [inputs/search filter-books
                            :placeholder "Title, ISBN, etc"])]
   :right-items [(deftoolbar-item
                   :content "Order by")
                 (deftoolbar-item
                   :content [inputs/select-field :ui.books.sort
                             [(inputs/defoption "Title" :value "title")
                              (inputs/defoption "Price" :value "cost")
                              (inputs/defoption "Page Count" :value "pages")
                              (inputs/defoption "ISBN" :value "isbn")]
                             :model store/global-state :selected "title"])]})


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
                      [:a {:href "#"
                           :on-click #(do-edit-book (:id book))} "Edit"]
                      [:span "·"]
                      [:a {:href "#"
                           :on-click #(do-delete-book (:id book))} "Delete"]]]}
    :left {:content [[:img {:src (:image book) :width "80"}]]}]])

(defn book-grid-component [books]
  (into
   [:div.columns.is-multiline]
   (for [b books]
     [book-component b])))

(defn books-list-page []
  (store/reset! book-list (models/books->vec))
  (fn []
    (when (get-sort-field)
      (reset! book-list (vec (sort-by (keyword (get-sort-field)) (models/books->vec)))))
    [:<>
     [:h1.title "Books"]
     [toolbar (get-toolbar-data)]
     [paginate @book-list book-grid-component :page-size 2]]))

(defn books-page []
  (case (ui/get-subpage :books)
    :books [books-list-page]
    :new-book [new-book-page]
    :edit-book [edit-book-page]
    :delete-book [delete-book-page]
    [:<>
     [:h2.title "Page Not Found!"]
     [:p "Bad page identifier " (str (ui/get-subpage :books))]]))

(ui/set-subpage :books :books)
