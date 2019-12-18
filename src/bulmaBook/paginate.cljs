(ns bulmaBook.paginate
  (:require [bulmaBook.utils :refer [cs]]
            [reagent.core :refer [atom]]
            [reagent.session :as session]))

(defn is-current? [state page]
  (if (= (get-in @state [:current]) page)
    true
    false))

(defn set-current [state page]
  (swap! state assoc-in [:current] page))

(defn get-current [state]
  (get-in @state [:current]))

(defn get-last [state]
  (get-in @state [:last]))

(defn get-page-count [state]
  (get-in @state [:total-pages] 0))

(defn get-page [state page]
  (get-in @state [:pages (keyword (str "page-" page))]))

(defn make-link [state page]
  [:li
   [:a {:class (cs "pagination-link"
                   (when (is-current? state page)
                     "is-current"))
        :on-click (fn []
                    (set-current state page))}
    (str page)]])

(defn paginate [records page-render-fn & {:keys [page-size]
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
                     :last (inc total)})]
    (fn []
      [:div
       [:nav.pagination
        (when (not (= (get-current state) 1))
          [:a.pagination-previous
           {:on-click (fn []
                        (set-current state (dec (get-current state))))}
           "Previous"])
        (when (not (= (get-current state) (get-last state)))
          [:a.pagination-next
           {:on-click (fn []
                        (set-current state (inc (get-current state))))}
           "Next"])
        (cond
          (<= (get-page-count state) 4)
          (into
           [:ul.pagination-list]
           (for [i (range 1 (inc (get-page-count state)))]
             [make-link state i]))
          (< (get-current state) 4)
          (into
           [:ul.pagination-list]
           (for [i [1 2 3 4 (get-last state)]]
             (if (= i 4)
               [:li [:a.pagination-ellipsis "\u2026"]]
               [make-link state i])))
          (> (get-current state) (- (get-last state) 4))
          (into
           [:ul.pagination-list
            [make-link state 1]]
           (for [i (range (- (get-last state) 4))]
             (if (= (- (get-last state) 4) i)
               [:li [:a.pagination-ellipsis "\u2026"]]
               [make-link state i])))
          :default
          (into
           [:ul.pagination-list
            [make-link state 1]]
           (for [i (range (- (get-current state) 2) (+ (get-current state) 3))]
             (if (or (= (- (get-current state) 2) i)
                     (= (+ (get-current state) 2) i))
               [:li [:a.pagination-ellipsis "\u2026"]]
               (if (= (+ (get-current state) 3) i)
                 [make-link state (get-last state)]
                 [make-link state i])))))]
       (page-render-fn (get-page state (get-current state)))])))
