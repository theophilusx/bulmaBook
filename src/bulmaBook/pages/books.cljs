(ns bulmaBook.pages.books
  (:require [bulmaBook.components.basic :refer [media breadcrumbs breadcrumbs]]
            [bulmaBook.components.paginate :refer [paginate]]
            [bulmaBook.components.toolbar :refer [deftoolbar-item toolbar]]
            [bulmaBook.components.inputs :as inputs]
            [bulmaBook.store :as store]
            [reagent.core :as r]
            [clojure.string :as string]))

(def book-list (r/atom (store/get-in store/global-state [:data :book-data])))

(defn save-new-book [place]
  (store/update-in! store/global-state [:data :book-data] conj @place)
  (store/clear! place)
  (store/assoc-in! store/global-state [:ui :books :page] :books))

(defn clear-new-book [place]
  (store/clear! place)
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

(defn filter-books [search-term]
  (println (str "search term: " search-term))
  (store/reset! book-list (filterv
                           (fn [m]
                             (or (string/includes? (str (:title m)) search-term)
                                 (string/includes? (str (:cost m)) search-term)
                                 (string/includes? (str (:pages m)) search-term)
                                 (string/includes? (str (:isbn m)) search-term)))
                           (store/get-in store/global-state [:data :book-data]))))

(defn get-toolbar-data []
  {:left-items [(deftoolbar-item
                  :content [:p.subtitle.is-5
                            [:strong
                             (count (store/get-in store/global-state
                                                  [:data :book-data]))]
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
  (reset! book-list (store/get-in store/global-state [:data :book-data]))
  (fn []
    (when (store/get-in store/global-state [:data :sort])
      (reset! book-list (vec (sort-by (store/get-in store/global-state
                                                    [:data :sort])
                                      (store/get-in store/global-state
                                                    [:data :book-data])))))
    [:div
     [breadcrumbs :ui.books.page
      [{:name "Books"
        :value :books
        :active true}]]
     [toolbar (get-toolbar-data)]
     [paginate @book-list book-grid-component :page-size 2]]))
