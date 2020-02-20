(ns bulmaBook.components.sidebar
  (:require [bulmaBook.utils :refer [cs session-path]]
            [bulmaBook.components.basic :refer [icon-component]]
            [reagent.session :as session]))

(defn is-active? [session-id id]
  (if (= (session/get-in (session-path session-id)) id)
    true
    false))

(defn set-active [session-id id]
  (session/assoc-in! (session-path session-id) id))

(defn -make-item [i session-id]
  [:li
   [:a {:class (cs (:class i)
                   (when (is-active? session-id (:id i)) "is-active"))
        :href  (:href i)
        :id    (:id i)
        :on-click (fn []
                    (set-active session-id (:id i)))}
    (when (:icon i)
      [icon-component (:icon i)])
    (:title i)]])

(defn -make-menu [m session-id]
  [:aside.menu
   [:h3.is-3.menu-label (:title m)]
   (into
    [:ul.menu-list]
    (for [i (:items m)]
      (if (= (:type i) :item)
        (-make-item i session-id)
        (-make-menu i session-id))))])

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
  `:icon` An icon data `map` (see `bulmaBook.components.basic`)
  `:class` Any additional class attributes to add to the element.
  `:items` For items of type `:menu`, this key is a vector of `defsidebar-item`
           maps representing the sub-menu items."
  [& {:keys [type title href icon id items class]
      :or {type :item
           href "#"
           id (keyword (gensym "side-bar-"))}}]
  {:type type
   :title title
   :href href
   :icon icon
   :id id
   :items items
   :class class})

(defn sidebar
  "Defines a sidebar menu. A sidebar is defined by a `map` with the keys
  `:sessin-id` A keyword representing a path into the global state atom e.g.
               :place. If keyword has a `.` it is interpreted as a delimiter for
               another path level e.g. :place.sub-place = [:place :sub-place].
  `:default-link` The default menu id to be set as active when sidebar is first
                   loaded
  `:item` A `defsidebar-item` map defining the parent menu with sub-menus as a
          vector of `defsidebar-tiem` items in the `:items` key"
  [data]
  (session/assoc-in! (session-path (:session-id data)) (:default-link data))
  (fn []
    [:nav {:class (cs "menu" (:class (:item data)))}
     (-make-menu (:item data) (:session-id data))]))
