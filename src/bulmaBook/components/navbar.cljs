(ns bulmaBook.components.navbar
  (:require [reagent.core :refer [atom]]
            [reagent.session :as session]
            [bulmaBook.utils :refer [cs]]
            [bulmaBook.components.basic :refer [icon]]))

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
                       id (keyword (gensym "item-"))
                       selectable true}}]
  {:type type
   :title title
   :class classes
   :href href
   :id id
   :contents contents
   :selectable selectable
   :icon-img icon-img
   :is-hoverable is-hoverable})

(defn active? [state id]
  (if (= (get @state :active-item) id)
    true
    false))

(defn set-active [state & id]
  (swap! state assoc :active-item (first id)))

(defn dropdown-active? [state id]
  (if (= (get @state :active-dropdown) id)
    true
    false))

(defn toggle-dropdown [state & id]
  (if (nil? id)
    (swap! state assoc :active-dropdown nil)
    (if (= (get @state :active-dropdown) (first id))
      (swap! state assoc :active-dropdown nil)
      (swap! state assoc :active-dropdown (first id)))))

(defn set-choice [model & id]
  (session/assoc-in! (conj model :choice) (first id)))

(defn -item-a [a state model]
  [:a {:class (cs "navbar-item" (:class a)
                  (when (active? state (:id a)) "is-active"))
       :href (:href a)
       :on-click (when (:selectable a)
                   (fn []
                     (toggle-dropdown state)
                     (set-active state (:id a))
                     (set-choice model (:id a))))}
   (if (:icon-img a)
     [icon (:icon-img a) :title (:contents a)]
     (:contents a))])

(defn -item-raw [r state model]
  [:div {:class (cs "content" (:class r))}
   (:contents r)])

(defn -item-div [d state model]
  (into
   [:div {:class (cs "navbar-item" (:class d))}]
   (for [c (:contents d)]
     (condp = (:type c)
       :a (-item-a c state model)
       :raw (-item-raw c state model)
       (-item-div c state model)))))

(defn -item-dropdown [d state model]
  [:div {:class (cs "navbar-item" "has-dropdown" (:class d)
                          (when (:is-hoverable d) "is-hoverable")
                          (when (dropdown-active? state (:id d)) 
                            "is-active"))}
   [:a {:class (cs "navbar-link")
        :id (:id d)
        :on-click (fn []
                    (toggle-dropdown state (:id d)))}
    (:title d)]
   (into
    [:div.navbar-dropdown]
    (for [i (:contents d)]
      (condp = (:type i)
        :a (-item-a i state model)
        :dropdown (-item-dropdown i state model)
        :divider [:hr.navbar-divider]
        (-item-div i state model))))])

(defn -make-item [i state model]
  (condp = (:type i)
    :a (-item-a i state model)
    :raw (-item-raw i state model)
    :dropdown (-item-dropdown i state model)
    :divider [:hr.navbar-divider]
    (-item-div i state model)))

(defn -burger [state model]
  [:a {:class (cs "navbar-burger" "burger"
                        (when (get @state :burger-active)
                          "is-active"))
       :role "button"
       :aria-label "menu"
       :aria-expanded "false"
       :data-target model
       :on-click (fn []
                   (swap! state update :burger-active not))}
   [:span {:aria-hidden true}]
   [:span {:aria-hidden true}]
   [:span {:aria-hidden true}]])

(defn -brand [state model item has-burger]
  [:div.navbar-brand
   (-make-item item state model)
   (when has-burger
     (-burger state model))])

(defn -menu [state model start end]
  [:div {:class (cs "navbar-menu"
                    (when (get @state :burger-active)
                      "is-active"))
         :id model}
   (into
    [:div.navbar-start]
    (for [s start]
      (-make-item s state model)))
   (when end
     (into
      [:div.navbar-end]
      (for [e end]
        (-make-item e state model))))])

(defn navbar [model & {:keys [brand has-shadow has-burger start-menu end-menu
                              class default-link]}]
  (let [state (atom {:burger-active false
                     :active-item default-link})]
    (session/assoc-in! (conj model :choice) default-link)
    (fn []
      [:nav {:class      (cs "navbar" class
                             (when has-shadow "has-shadow"))
             :role       "navigation"
             :aria-label "main navigation"}
       (when brand
         (-brand state model brand has-burger))
       (-menu state model start-menu end-menu)])))
