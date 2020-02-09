(ns bulmaBook.components.sidebar
  (:require [bulmaBook.utils :refer [cs ensure-vector]]
            [bulmaBook.components.basic :refer [icon]]
            [reagent.session :as session]))

(defn is-active? [model id]
  (if (= (session/get-in [model :choice]) id)
    true
    false))

(defn set-active [model id]
  (session/assoc-in! [model :choice] id))

(defn -make-item [i model]
  [:li 
   [:a {:class (cs (:class i)
                   (when (is-active? model (:id i)) "is-active"))
        :href  (:href i)
        :id    (:id i)
        :on-click (fn []
                    (set-active model (:id i)))}
    (if (:icon-img i)
      [icon (:icon-img i) :title (:title i)]
      (:title i))]])

(defn -make-menu [m model]
  [:div
   [:p.menu-label (:title m)]
   (into
    [:ul.menu-list]
    (for [i (:items m)]
      (if (= (:type i) :item)
        (-make-item i model)
        (-make-menu i model))))])

(defn defsidebar-item
  "Define an item for the sidebar. The item can either be a simple link item
  or it can be a sub-menu item. Items are defined by a `map` which can have
  the following keys -
  `:type` Type of the item. Either :item for a simple link item or :menu for 
          a nested sub-menu item. Defaults to `:item`.
  `:title` The title for sub-menus.
  `:href` The href for the link. Defaults to `#`.
  `::id` An id attribute for the item. If not provided, defaults to a unique
         value with the prefix `side-bar-`. 
  `:icon-image` Name of an icon image to include in the menu.
  `:class` Any additional class attributes to add to the element.
  `:items` For items of type `:menu`, this key is a vector of `defsidebar-item`
           maps representing the sub-menu items."
  [& {:keys [type title href icon-img id items class]
      :or {type :item
           href "#"
           id (keyword (gensym "side-bar-"))}}]
  {:type type
   :title title
   :href href
   :icon-img icon-img
   :id id
   :items items
   :class class})

(defn sidebar
  "Defines a sidebar menu. A sidebar is defined by a `map` with the keys
  `:sessin-key` Path into the global state atom where this component will
                store its state. 
  `:default-link` The default menu id to be set as active when sidebar is first 
                   loaded
  `:item` A `defsidebar-item` map defining the parent menu with sub-menus as a 
          vector of `defsidebar-tiem` items in the `:items` key"
  [data]
  (update-in data [:session-key] ensure-vector)
  (session/assoc-in! (conj (:session-key data) :choice) (:default-link data))
  (fn []
    [:nav {:class (cs "menu" (:class (:item data)))}
     (-make-menu (:item data) (:session-key data))]))