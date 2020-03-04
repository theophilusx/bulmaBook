(ns bulmaBook.components.basic
  (:require [bulmaBook.utils :refer [session-path]]
            [clojure.string :as string]
            [reagent.session :as session]))

(defn icon-component
  "Generate an icon component. `icon-data` is a map containing the keys
  `:position` - place icon to the `:left` or `:right`
  `:span-class` - additional class attributes to add to `span` element
  `:name` - name of the font awesome icon to add
  `:icon-class` - additional class attributes to add to the `icon` element"
  [icon-data]
  [:span.icon {:class [(:span-class icon-data)
                       (when (contains? icon-data :position)
                         (case (:position icon-data)
                           :left "is-left"
                           :right "is-right"
                           nil))]}
   [:i.fa {:class [(:name icon-data)
                   (:icon-class icon-data)]}]])

(defn icon
  "Generates a `vector` of `icon` components from icon data. The `icon-data`
  can be either a `map` or a `vector` of icon data `maps`"
  [icon-data]
  (if (map? icon-data)
    [[icon-component icon-data]]
    (vec (for [i icon-data]
           [icon-component i]))))

(defn icon-control-class [icon-data]
  (if (map? icon-data)
    (when (contains? icon-data :position)
      (case (:position icon-data)
        :left "has-icons-left"
        :right "has-icons-right"
        ""))
    (string/join (map (fn [i]
                        (when (contains? i :position)
                          (case (:position i)
                            :left "has-icon-left"
                            :right "has-icon-right"
                            "")))
                      icon-data) " ")))

(defn media [body & {:keys [left right id class]}]
  [:article.media {:class [class]
                   :id id}
   (when left
     (into
      [:aside.media-left {:class [(:class left)]
                          :id (:id left)}]
      (for [c (:content left)]
        c)))
   (into
    [:div.media-content {:class [(:class body)]
                         :id (:id body)}]
    (for [c (:content body)]
      c))
   (when right
     (into
      [:aside.media-right {:class [(:class right)]
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

(defn breadcrumbs [id crumbs & {:keys [nav-class position separator size]}]
  [:nav.breadcrumb {:class [nav-class
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
              :on-click #(session/assoc-in! (session-path id) (:value c))}
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
              :on-click #(session/assoc-in! (session-path id) (:value c))}
          (:name c)]])))])
