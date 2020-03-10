(ns bulmaBook.pages.books
  (:require [bulmaBook.components.basic :refer [media breadcrumbs]]
            [bulmaBook.components.paginate :refer [paginate]]
            [bulmaBook.components.toolbar :refer [deftoolbar-item toolbar]]
            [bulmaBook.components.inputs :as inputs]
            [bulmaBook.store :as store]
            [reagent.core :as r]
            [clojure.string :as string]))

(def book-list (r/atom {}))

(defn get-subpage []
  (store/get-in store/global-state [:ui :books :page]))

(defn set-subpage [page]
  (store/assoc-in! store/global-state [:ui :books :page] page))

(defn set-edit-id [bid]
  (store/assoc-in! store/global-state [:ui :books :edit] bid))

(defn get-edit-id []
  (store/get-in store/global-state [:ui :books :edit]))

(defn set-delete-id [bid]
  (store/assoc-in! store/global-state [:ui :books :delete] bid))

(defn get-delete-id []
  (store/get-in store/global-state [:ui :books :delete]))

(defn get-sort-field []
  (store/get-in store/global-state [:ui :books :sort]))

(defn book-data []
  (store/get-in store/global-state [:data :book-data]))

(defn books->vec []
  (let [books (book-data)]
    (mapv #(% books) (keys books))))

(defn gen-new-key []
  (keyword (str "bk" (inc (count (keys (book-data)))))))

(defn get-book [bid]
  (bid (book-data)))

(defn add-book [book]
  (store/assoc-in! store/global-state [:data :book-data (:id book)] book))

(defn delete-book [bid]
  (store/update-in! store/global-state [:data :book-data] dissoc bid))


(defn filter-books [search-term]
  (store/reset! book-list (filterv
                           (fn [bk]
                             (or (string/includes? (str (:title bk)) search-term)
                                 (string/includes? (str (:cost bk)) search-term)
                                 (string/includes? (str (:pages bk)) search-term)
                                 (string/includes? (str (:isbn bk)) search-term)))
                           (books->vec))))


(defn save-new-book [book]
  (let [new-id (gen-new-key)]
    (println (str "New Book: " @book))
    (store/put! book :id new-id)
    (println (str "New book w/ ID: " @book))
    (add-book @book)
    (store/clear! book)
    (set-subpage :books)))

(defn clear-new-book [book]
  (store/clear! book)
  (set-subpage :books))

(defn new-book-form []
  (let [doc (r/atom {})]
    (fn []
      [:form.box
       [inputs/horizontal-field "Title" [[inputs/input :text :title :model doc]]]
       [inputs/horizontal-field "Image" [[inputs/input :text :image :model doc]]]
       [inputs/horizontal-field "Cost" [[inputs/input :text :cost :model doc]]]
       [inputs/horizontal-field "Pages" [[inputs/input :text :pages :model doc]]]
       [inputs/horizontal-field "ISBN" [[inputs/input :text :isbn :model doc]]]
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
  (set-edit-id bid)
  (set-subpage :edit-book))

(defn save-edit-book [book]
  (add-book @book)
  (store/clear! book)
  (set-edit-id nil)
  (set-subpage :books))

(defn cancel-edit-book [book]
  (store/clear! book)
  (set-edit-id nil)
  (set-subpage :books))

(defn edit-book-form []
  (let [doc (r/atom (get-book (store/get-in store/global-state
                                            [:ui :books :edit])))]
    (fn []
      [:form.box
       [inputs/horizontal-field "Id" [[inputs/field [(str (:id @doc))]]]]
       [inputs/horizontal-field "Title" [[inputs/input :text :title :model doc]]]
       [inputs/horizontal-field "Image" [[inputs/input :text :image :model doc]]]
       [inputs/horizontal-field "Cost" [[inputs/input :text :cost :model doc]]]
       [inputs/horizontal-field "Pages" [[inputs/input :text :pages :model doc]]]
       [inputs/horizontal-field "ISBN" [[inputs/input :text :isbn :model doc]]]
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
  (set-delete-id bid)
  (set-subpage :delete-book))

(defn delete-book-page []
  (let [book (get-book (get-delete-id))]
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
                       (delete-book (get-delete-id))
                       (set-delete-id nil)
                       (set-subpage :books))
                     :classes {:button "is-success"}]
                    [inputs/button "Cancel"
                     (fn []
                       (set-delete-id nil)
                       (set-subpage :books))]]
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
                  [inputs/button "New" #(set-subpage :new-book)
                   :classes {:button "is-success"}])
                (deftoolbar-item
                  :class "is-hidden-table-only"
                  :content [inputs/search filter-books
                            :placeholder "Title, ISBN, etc"])]
   :right-items [(deftoolbar-item
                   :content "Order by")
                 (deftoolbar-item
                   :content [inputs/select-field :ui.books.sort
                             [[inputs/option "Title" :value :title]
                              [inputs/option "Price" :value :cost]
                              [inputs/option "Page Count" :value :pages]
                              [inputs/option "ISBN" :value :isbn]]
                             :model store/global-state])]})

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
  (store/reset! book-list (books->vec))
  (fn []
    (when (get-sort-field)
      (reset! book-list (vec (sort-by (get-sort-field) (books->vec)))))
    [:<>
     [breadcrumbs :ui.books.page
      [{:name "Books"
        :value :books
        :active true}]]
     [toolbar (get-toolbar-data)]
     [paginate @book-list book-grid-component :page-size 2]]))

(defn books-page []
  (case (get-subpage)
    :books [books-list-page]
    :new-book [new-book-page]
    :edit-book [edit-book-page]
    :delete-book [delete-book-page]
    [:<>
     [:h2.title "Page Not Found!"]
     [:p "Bad page identifier " (str (get-subpage))]]))

(set-subpage :books)
