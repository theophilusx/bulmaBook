(ns bulmaBook.components.basic
  (:require [bulmaBook.utils :refer [cs]]
            [clojure.string :as string]))

(defn icon-component
  "Generate an icon component. `icon-data` is a map containing the keys
  `:position` - place icon to the `:left` or `:right`
  `:span-class` - additional class attributes to add to `span` element
  `:name` - name of the font awesome icon to add
  `:icon-class` - additional class attributes to add to the `icon` element"
  [icon-data]
  [:span {:class (cs "icon" (:span-class icon-data)
                     (when (contains? icon-data :position)
                       (condp = (:position icon-data)
                         :left "is-left"
                         :right "is-right"
                         (println (str "Unsupported value for position in icon "
                                       (:position icon-data))))))}
   [:i {:class (cs "fa" (:name icon-data) (:icon-class icon-data))}]])

(defn icon
  "Generates a `vector` of `icon` components from icon data. The `icon-data`
  can be either a `map` or a `vector` of icon data `maps`"
  [icon-data]
  (if (map? icon-data)
    [(icon-component icon-data)]
    (into
     []
     (for [i icon-data]
       (icon-component i)))))

(defn icon-control-class [icon-data]
  (if (map? icon-data)
    (when (contains? icon-data :position)
      (condp = (:position icon-data)
        :left "has-icons-left"
        :right "has-icons-right"
        (println (str "Unsupported value for position in icon "
                      (:position icon-data)))))
    (string/join (map (fn [i]
                        (when (contains? i :position)
                          (condp = (:position i)
                            :left "has-icon-left"
                            :right "has-icon-right"
                            (println (str "Unsupported value for position in "
                                          "icon: " (:position i))))))
                      icon-data) " ")))

(defn media [body & {:keys [left right id class]}]
  [:article {:class (cs "media" class)
             :id id}
   (when left
     (into
      [:aside {:class (cs "media-left" (:class left))
               :id (:id left)}]
      (for [c (:content left)]
        c)))
   (into
    [:div {:class (cs "media-content" (:class body))
           :id (:id body)}]
    (for [c (:content body)]
      c))
   (when right
     (into
      [:aside {:class (cs "media-right" (:class right))
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
