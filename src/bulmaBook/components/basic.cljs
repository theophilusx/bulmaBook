(ns bulmaBook.components.basic
  (:require [bulmaBook.utils :refer [spath]]
            [clojure.string :as string]
            [reagent.session :as session]))

(defn media [body & {:keys [left right id class]}]
  [:article.media {:class class
                   :id id}
   (when left
     (into
      [:aside.media-left {:class (:class left)
                          :id (:id left)}]
      (for [c (:content left)]
        c)))
   (into
    [:div.media-content {:class (:class body)
                         :id (:id body)}]
    (for [c (:content body)]
      c))
   (when right
     (into
      [:aside.media-right {:class (:class right)
                           :id (:id right)}]
      (for [c (:content right)]
        c)))])

(declare render-map)
(declare render-set)

(defn render-vec
  "Vector display component. Will render vector as an un-ordered list"
  [v]
  (into
   [:ul]
   (for [i v]
     (cond
       (vector? i) [:li (render-vec i)]
       (map? i)    [:li (render-map  i)]
       (set? i)    [:li (render-set i)]
       :else [:li (str i)]))))

(defn render-set [s]
  [:div.box
   (str "(" (string/join ", " s) ")")])

(defn render-map [m]
  [:table.table
   (into
    [:tbody]
    (for [k (keys m)]
      (cond
        (map? (get m k)) [:tr
                          [:td [:strong (str k)]]
                          [:td (render-map (get m k))]]
        (set? (get m k)) [:tr
                          [:td [:strong (str k)]]
                          [:td (str (get m k))]]
        (vector? (get m k)) [:tr
                             [:td [:strong (str k)]]
                             [:td (render-vec (get m k))]]
        :else [:tr
                  [:td [:strong (str k)]]
                  [:td (str (get m k))]])))])

(defn breadcrumbs [id crumbs & {:keys [class position separator size]}]
  [:nav.breadcrumb {:class [class
                            (when position
                              (case position
                                :center "is-centered"
                                :right "is-right"
                                nil))
                            (when separator
                              (case separator
                                :arrow "has-arrow-separator"
                                :bullet "has-bullet-separator"
                                :dot "has-dot-separator"
                                :succeeds "has-succeeds-separator"
                                nil))
                            (when size
                              (case size
                                :small "is-small"
                                :medium "is-medium"
                                :large "is-large"
                                :normal ""
                                nil))]
                    :aria-label "breadcrumbs"}
   (into
    [:ul]
    (for [c crumbs]
      (if (:icon c)
        [:li {:class (when (:active c)
                       "is-active")}
         [:a {:href "#"
              :on-click #(session/assoc-in! (spath id) (:value c))}
          [:span.icon {:class (when (contains? (:icon c) :size)
                                (case (:size (:icon c))
                                  :small "is-small"
                                  :medium "is-medium"
                                  :large "is-large"
                                  nil))}
           [:i.fa {:class (:name (:icon c))
                   :aria-hidden "true"}]]
          [:span (:name c)]]]
        [:li {:class (when (:active c)
                       "is-active")}
         [:a {:href "#"
              :on-click #(session/assoc-in! (spath id) (:value c))}
          (:name c)]])))])
