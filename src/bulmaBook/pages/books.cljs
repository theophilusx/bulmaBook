(ns bulmaBook.pages.books
  (:require [bulmaBook.components.basic :refer [media breadcrumbs breadcrumbs]]
            [bulmaBook.components.paginate :refer [paginate]]
            [bulmaBook.components.toolbar :refer [deftoolbar-item toolbar]]
            [bulmaBook.components.inputs :as inputs]
            [bulmaBook.store :as store]
            [reagent.core :as r]
            [clojure.string :as string]))

(defn books->vec []
  (let [books (store/get-in store/global-state [:data :book-data])]
    (mapv #(% books) (keys books))))

(defn gen-new-key []
  (keyword (str "bk" (inc (count (keys (store/get-in
                                        store/global-state
                                        [:data :book-data])))))))

(defn get-book [bid]
  (store/get-in store/global-state [:data :book-data bid]))

(def book-list (r/atom (books->vec)))

(defn save-new-book [book]
  (let [new-id (gen-new-key)]
    (store/put! book :id new-id)
    (store/update-in! store/global-state [:data :book-data new-id] conj @book)
    (store/clear! book)
    (store/assoc-in! store/global-state [:ui :books :page] :books)))

(defn clear-new-book [book]
  (store/clear! book)
  (store/assoc-in! store/global-state [:ui :books :page] :books))

(defn new-book-form []
  (let [doc (r/atom {})]
    (fn []
      [:form.box
       [inputs/horizontal-field "Title" [[inputs/input :text :title :modal doc]]]
       [inputs/horizontal-field "Image" [[inputs/input :text :image :modal doc]]]
       [inputs/horizontal-field "Cost" [[inputs/input :text :cost :modal doc]]]
       [inputs/horizontal-field "Pages" [[inputs/input :text :pages :modal doc]]]
       [inputs/horizontal-field "ISBN" [[inputs/input :text :isbn :modal doc]]]
       [inputs/button "Save" #(save-new-book doc)
        :classes {:button "is-success"}]])))

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
  (store/assoc-in! store/global-state [:ui :books :edit] bid)
  (store/assoc-in! store/global-state [:ui :books :page] :edit-book))

(defn save-edit-book [book]
  (store/assoc-in! store/global-state [:data :book-data (:id @book)] @book)
  (store/clear! book)
  (store/assoc-in! store/global-state [:ui :books :edit] nil)
  (store/assoc-in! store/global-state [:ui :books :page] :books))

(defn cancel-edit-book [book]
  (store/clear! book)
  (store/assoc-in! store/global-state [:ui :books :edit] nil)
  (store/assoc-in! store/global-state [:ui :books :page] :books))

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

(defn filter-books [search-term]
  (store/reset! book-list (filterv
                           (fn [bk]
                             (or (string/includes? (str (:title bk)) search-term)
                                 (string/includes? (str (:cost bk)) search-term)
                                 (string/includes? (str (:pages bk)) search-term)
                                 (string/includes? (str (:isbn bk)) search-term)))
                           (books->vec))))

(defn get-toolbar-data []
  {:left-items [(deftoolbar-item
                  :content [:p.subtitle.is-5
                            [:strong
                             (count @book-list)]
                            " books"])
                (deftoolbar-item
                  :type :div
                  :content
                  [inputs/button "New" #(store/assoc-in!
                                       store/global-state
                                       [:ui :books :page] :new-book)
                   :classes {:button "is-success"}])
                (deftoolbar-item
                  :class "is-hidden-table-only"
                  :content [inputs/search filter-books
                            :placeholder "Title, ISBN, etc"])]
   :right-items [(deftoolbar-item
                   :content "Order by")
                 (deftoolbar-item
                   :content [inputs/select-field :data.sort
                             [[inputs/option "Title" :title]
                              [inputs/option "Price" :cost]
                              [inputs/option "Page Count" :pages]
                              [inputs/option "ISBN" :isbn]]
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
                      [:a {:href "#"} "Delete"]]]}
    :left {:content [[:img {:src (:image book) :width "80"}]]}]])

(defn book-grid-component [books]
  (into
   [:div.columns.is-multiline]
   (for [b books]
     [book-component b])))

(defn books-page []
  (reset! book-list (books->vec))
  (fn []
    (when (store/get-in store/global-state [:data :sort])
      (reset! book-list (vec (sort-by (store/get-in store/global-state
                                                    [:data :sort])
                                      (books->vec)))))
    [:div
     [breadcrumbs :ui.books.page
      [{:name "Books"
        :value :books
        :active true}]]
     [toolbar (get-toolbar-data)]
     [paginate @book-list book-grid-component :page-size 2]]))
