(ns bulmaBook.components.navbar
  (:require [reagent.core :refer [atom]]
            [reagent.session :as session]
            [bulmaBook.utils :refer [cs session-path]]
            [bulmaBook.components.basic :refer [icon-component]]))

(defn active? [state id]
  (if (= (:active-item @state) id)
    true
    false))

(defn set-active [state & id]
  (swap! state assoc :active-item (first id)))

(defn dropdown-active? [state id]
  (if (= (:active-dropdown @state) id)
    true
    false))

(defn toggle-dropdown [state & id]
  (if (nil? id)
    (swap! state assoc :active-dropdown nil)
    (if (= (:active-dropdown @state) (first id))
      (swap! state assoc :active-dropdown nil)
      (swap! state assoc :active-dropdown (first id)))))

(defn set-choice [path & id]
  (session/assoc-in! (session-path path) (first id)))

(defn -item-a [a state]
  [:a {:class (cs "navbar-item" (:class a)
                  (when (active? state (:id a)) "is-active"))
       :href (:href a)
       :on-click (when (:selectable a)
                   (fn []
                     (toggle-dropdown state)
                     (set-active state (:id a))
                     (set-choice (:session-id @state) (:id a))))}
   (when (:icon a)
     [icon-component (:icon a)])
   (:contents a)])

(defn -item-raw [r _]
  [:div {:class (cs "content" (:class r))}
   (:contents r)])

(defn -item-div [d state]
  (into
   [:div {:class (cs "navbar-item" (:class d))}]
   (for [c (:contents d)]
     (condp = (:type c)
       :a (-item-a c state)
       :raw (-item-raw c state)
       (-item-div c state)))))

(defn -item-dropdown [d state]
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
        :a (-item-a i state)
        :dropdown (-item-dropdown i state)
        :divider [:hr.navbar-divider]
        (-item-div i state))))])

(defn -make-item [i state]
  (condp = (:type i)
    :a (-item-a i state)
    :raw (-item-raw i state)
    :dropdown (-item-dropdown i state)
    :divider [:hr.navbar-divider]
    (-item-div i state)))

(defn -burger [state]
  [:a {:class (cs "navbar-burger" "burger"
                        (when (get @state :burger-active)
                          "is-active"))
       :role "button"
       :aria-label "menu"
       :aria-expanded "false"
       :data-target (:session-id @state)
       :on-click (fn []
                   (swap! state update :burger-active not))}
   [:span {:aria-hidden true}]
   [:span {:aria-hidden true}]
   [:span {:aria-hidden true}]])

(defn -brand [state]
  [:div.navbar-brand
   (-make-item (:brand @state) state)
   (when (:has-burger @state)
     (-burger state))])

(defn -menu [state]
  [:div {:class (cs "navbar-menu"
                    (when (:burger-active @state)
                      "is-active"))
         :id (:session-id @state)}
   (into
    [:div.navbar-start]
    (for [s (:menus @state)]
      (-make-item s state)))
   (when (:end-menu @state)
     (into
      [:div.navbar-end]
      (for [e (:end-menu @state)]
        (-make-item e state))))])

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
   `:icon` - An icon data `map` (see `bulmaBook.components.basic`)
   `:selectable` - determines if the item has a click handler attached.
                   Defaults to true.
   `:is-hoverable` - for dropdown menus determines if the menu will dropdown
                     when mouse hovers over it"
  [& {:keys [type title classes href id contents selectable
             icon is-hoverable]
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
   :icon icon
   :is-hoverable is-hoverable})

(defn navbar
  "Define an application navbar. The `data` argument is a map which can
  have the following keys -
  `:session-id` - A keyword representing the session ID. e.g :topbar or :top.bar
  `:has-shadow` - true if the navbar has a shadow effect. Default true
  `:is-dark` - if true, navbar is a dark colour. Default is false
  `:has-burger` - true if navabar should include a 'burger' menu. Defaault true
  `:class` - additional class attributes to be added to main nav tag
  `:default-link` - the default (active) link set when navbar first loaded
  `:brand` - Brand definition (use defnavbar-item to define)
  `:menus` - the navbar menus = vector of defnavbar-item items
  `:end-menu` - vector of menus to be added after main menus i.e. to the right."
  [nb-def]
  (let [state (atom (merge {:has-shadow true
                            :has-burger true
                            :is-dark false
                            :burger-active false
                            :active-item (:default-link nb-def)
                            :dropdown-active false}
                           nb-def))]
    (set-choice (:session-id @state) (:default-link @state))
    (fn [nb-def]
      (swap! state assoc :menus (:menus nb-def)
             :end-menu (:end-menu nb-def))
      [:nav {:class      (cs "navbar" (:class @state)
                             (when (:has-shadow @state) "has-shadow"))
             :role       "navigation"
             :aria-label "main navigation"}
       (when (:brand @state)
         (-brand state))
       (-menu state)])))
