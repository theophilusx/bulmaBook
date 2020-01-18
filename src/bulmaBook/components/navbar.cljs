(ns bulmaBook.components.navbar
  (:require [reagent.core :refer [atom]]
            [reagent.session :as session]
            [bulmaBook.utils :refer [cs]]
            [bulmaBook.components.basic :refer [icon]]))

(defonce navbar-state (atom {}))



(defn defnavbar-item
  "Define navbar entry items map. Supported keys are
  `:type` - the entry type. Possible values `:a` = link, `:div` = arbitrary div
            `:dropdown` = a dropdown menu and `:raw` = just added 'as is'. 
            Defaults to `:a`
   `:title` - The item title to appear in the navbar 
   `:class` - additional classes to add to the element
   `:href` - hypertext url for `:a` items. Defaults to `#`
   `:id` - Add an id attribute to this item. Defaultw to `nav-<n>` 
           where `<n>` is a unique value
   `:contents` - the actual item contents
   `:selectable` - determines if the item has a click handler attached. 
                   Defaults to true.
   `:is-hoverable` - for dropdown menus determines if the menu will dropdown
                     when mouse hovers over it"
  [& {:keys [type title classes href id contents selectable
             icon-img is-hoverable]
      :or {type :a
           href "#"
           id (keyword (gensym "nav-"))
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

(defn navbar
  "Define an application navbar. The `data` argument is a map which can 
  have the following keys -
  `:session-key` - where in the session state the menu choice is recorded
  `:has-shadow` - true if the navbar has a shadow effect. Default true
  `:is-dark` - if true, navbar is a dark colour. Default is false
  `:has-burger` - true if navabar should include a 'burger' menu. Defaault true
  `:class` - additional class attributes to be added to main nav tag
  `:default-link` - the default (active) link set when navbar first loaded
  `:brand` - Brand definition (use defnavbar-item to define)
  `:menus` - the navbar menus = vector of defnavbar-item items
  `:end-menu` - vector of menus to be added after main menus i.e. to the right."
  [nb-def]
  (let [data (merge {:has-shadow true
                       :has-burger true
                       :is-dark false}
                      nb-def)
        state (atom {:burger-active false
                     :active-item (:default-link data)})]
    (session/assoc-in! (conj (:session-key data) :choice) (:default-link data))
    (fn []
      [:nav {:class      (cs "navbar" class
                             (when (:has-shadow data) "has-shadow"))
             :role       "navigation"
             :aria-label "main navigation"}
       (when (:brand data)
         (-brand state (:session-key data) (:brand data) (:has-burger data)))
       (-menu state (:session-key data) (:menus data) (:end-menu data))])))
