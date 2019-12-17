(ns bulmaBook.navbar
  (:require [reagent.core :refer [atom]]
            [reagent.session :as session]
            [bulmaBook.utils :as utils]
            [bulmaBook.basic :refer [icon]]))

(defonce navbar-state (atom {}))

;; item hash
;; {:type :a | :div | :raw | :dropdown
;;  :classes string of classes
;;  :href link target (defaults to "#"
;;  :id unique id for item (will default to gensym keyword)
;;  :icon-img an icon to include
;;  :is-hoverable for dropdown allow hoverable CSS
;;  :contents what will go into the :a or :div. Can be vector of item hashes
;;            for a div
;;  :selectable if true, add click handler to set model and active state


(defn defitem [& {:keys [type title classes href id contents selectable
                         icon-img is-hoverable]
                  :or {type :a
                       href "#"
                       id (keyword (gensym "item-"))}}]
  {:type type
   :title title
   :class classes
   :href href
   :id id
   :contents contents
   :selectable selectable
   :icon-img icon-img
   :is-hoverable is-hoverable})

(defn is-active [model id]
  (if (= (get-in @navbar-state [model :activve-item]) id)
    true
    false))

(defn is-dropdown-active [model id]
  (if (= (get-in @navbar-state [model :active-dropdown]) id)
    true
    false))

(defn toggle-dropdown [model id]
  (if (nil? id)
    (swap! navbar-state assoc-in [model :active-dropdown] nil)
    (if (= (get-in @navbar-state [model :active-dropdown]) id)
      (swap! navbar-state assoc-in [model :active-dropdown] nil)
      (swap! navbar-state assoc-in [model :active-dropdown] id))))

(defn set-active [model id]
  (swap! navbar-state assoc-in [model :active-item] id))

(defn -item-a [a model]
  [:a {:class (utils/cs "navbar-item" (:class a)
                        (when (is-active model (:id a)) "is-active"))
       :href (:href a "#")
       :on-click (when (:selectable a)
                   (fn []
                     (toggle-dropdown model nil)
                     (set-active model (:id a))
                     (session/assoc-in! [model :choice] (:id a))))}
   (if (:icon-img a)
     [icon (:icon-img a) :title (:contents a)]
     (:contents a))])

(defn -item-raw [r model]
  [:div {:class (utils/cs "content" (:class r))}
   (:contents r)])

(defn -item-div [d model]
  (into
   [:div {:class (utils/cs "navbar-item" (:class d))}]
   (for [c (:contents d)]
     (condp = (:type c)
       :a (-item-a c model)
       :raw (-item-raw c model)
       (-item-div c model)))))

(defn -item-dropdown [d model]
  [:div {:class (utils/cs "navbar-item" "has-dropdown" (:class d)
                          (when (:is-hoverable d)
                            "is-hoverable")
                          (when (and (not (:is-hoverable d))
                                     (is-dropdown-active model (:id d))) 
                            "is-active"))}
   [:a {:class (utils/cs "navbar-link")
        :id (:id d)
        :on-click (fn []
                    (toggle-dropdown model (:id d)))}
    (:title d)]
   (into
    [:div.navbar-dropdown]
    (for [i (:contents d)]
      (condp = (:type i)
        :a (-item-a i model)
        :dropdown (-item-dropdown i model)
        :divider [:hr.navbar-divider]
        (-item-div i model))))])

(defn -make-item [i model]
  (condp = (:type i)
    :a (-item-a i model)
    :raw (-item-raw i model)
    :dropdown (-item-dropdown i model)
    :divider [:hr.navbar-divider]
    (-item-div i model)))

(defn -burger [model]
  [:a {:class (utils/cs "navbar-burger" "burger"
                        (when (get-in @navbar-state [model :burger-active])
                          "is-active"))
       :role "button"
       :aria-label "menu"
       :aria-expanded "false"
       :data-target model
       :on-click (fn []
                   (swap! navbar-state update-in [model :burger-active] not))}
   [:span {:aria-hidden true}]
   [:span {:aria-hidden true}]
   [:span {:aria-hidden true}]])

(defn -brand [model item has-burger]
  [:div.navbar-brand
   (-make-item item model)
   (when has-burger
     (-burger model))])

(defn -menu [model start end]
  [:div {:class (utils/cs "navbar-menu"
                          (when (get-in @navbar-state [model :burger-active])
                            "is-active"))
         :id model}
   (into
    [:div.navbar-start]
    (for [s start]
      (-make-item s model)))
   (when end
     (into
      [:div.navbar-end]
      (for [e end]
        (-make-item e model))))])

(defn navbar [model & {:keys [brand has-shadow has-burger start-menu end-menu
                              classes]}]
  (swap! navbar-state assoc model {:burger-active false
                                   :active-item nil})
  (fn []
    [:nav {:class      (utils/cs "navbar" classes
                                 (when has-shadow "has-shadow"))
           :role       "navigation"
           :aria-label "main navigation"}
     (when brand
       (-brand model brand has-burger))
     (-menu model start-menu end-menu)]))
