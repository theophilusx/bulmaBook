(ns bulmaBook.components.paginate
  (:require [bulmaBook.utils :refer [cs]]
            [reagent.core :refer [atom]]))

(defn -is-current?
  "Returns true if the supplied page numer is the current page. 
  False otherwise."
  [state page]
  (if (= (get-in @state [:current]) page)
    true
    false))

(defn -set-current
  "Update current page number in state to supplied page number."
  [state page]
  (swap! state assoc-in [:current] page))

(defn -get-current
  "Return current page number."
  [state]
  (get-in @state [:current]))

(defn -get-last
  "Return last page number."
  [state]
  (get-in @state [:last]))

(defn -get-page-count
  "Return total number of pages."
  [state]
  (get-in @state [:total-pages] 0))

(defn -get-page
  "Return the page associated with a page number."
  [state page]
  (get-in @state [:pages (keyword (str "page-" page))]))

(defn -make-link
  "Create a link item for specified page."
  [state page]
  [:li
   [:a {:class (cs "pagination-link"
                   (when (-is-current? state page)
                     "is-current"))
        :on-click (fn []
                    (-set-current state page))}
    (str page)]])

(defn paginate
  "Generate a paginated page of records. The `records` argument is a 
  sequence of records that will be partitioned into pages. Each record 
  in the sequence will be rendered using `page-render-fn`. The optional 
  key `:page-size` can be used to set how many records are shown per page 
  (defaults to 10). The `is-rounded` key can be set to true to have the 
  page navigation items rendered with rounded corners. The keys `is-small`
  `is-meidum` and `is-large` set the size of the navigation items."
  [records page-render-fn & {:keys [page-size is-rounded is-small
                                    is-medium is-large]
                             :or {page-size 10}}]
  (let [pages (reduce merge
                      (map-indexed
                       (fn [idx v]
                         {(keyword (str "page-" (inc idx)))
                          (vec v)})
                       (partition-all page-size records)))
        total (count (keys pages))
        state (atom {:pages pages
                     :total-pages total
                     :current 1
                     :first 1
                     :last total})]
    (fn []
      [:div
       [:nav {:class (cs "pagination"
                         (when is-rounded "is-rounded")
                         (when is-small "is-small")
                         (when is-medium "is-medium")
                         (when is-large "is-large"))
              :role "navigation"
              :aria-label "pagination"}
        (when (not (= (-get-current state) 1))
          [:a.pagination-previous
           {:on-click (fn []
                        (-set-current state (dec (-get-current state))))}
           "Previous"])
        (when (not (= (-get-current state) (-get-last state)))
          [:a.pagination-next
           {:on-click (fn []
                        (-set-current state (inc (-get-current state))))}
           "Next"])
        (cond
          (<= (-get-page-count state) 4)
          (into
           [:ul.pagination-list]
           (for [i (range 1 (inc (-get-page-count state)))]
             [-make-link state i]))
          (< (-get-current state) 4)
          (into
           [:ul.pagination-list]
           (for [i [1 2 3 4 (-get-last state)]]
             (if (= i 4)
               [:li [:a.pagination-ellipsis "\u2026"]]
               [-make-link state i])))
          (> (-get-current state) (- (-get-last state) 4))
          (into
           [:ul.pagination-list
            [-make-link state 1]]
           (for [i (range (- (-get-last state) 4))]
             (if (= (- (-get-last state) 4) i)
               [:li [:a.pagination-ellipsis "\u2026"]]
               [-make-link state i])))
          :default
          (into
           [:ul.pagination-list
            [-make-link state 1]]
           (for [i (range (- (-get-current state) 2) (+ (-get-current state) 3))]
             (if (or (= (- (-get-current state) 2) i)
                     (= (+ (-get-current state) 2) i))
               [:li [:a.pagination-ellipsis "\u2026"]]
               (if (= (+ (-get-current state) 3) i)
                 [-make-link state (-get-last state)]
                 [-make-link state i])))))]
       (page-render-fn (-get-page state (-get-current state)))])))
