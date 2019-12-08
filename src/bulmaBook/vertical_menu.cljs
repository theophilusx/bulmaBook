(ns bulmaBook.vertical-menu)

(defn v-menu-item [title & {:keys [href icon id extra-styles]
                            :or {href "#"
                                 extra-styles ""}}]
  (let [opts (if id
               {:href href :id id :class extra-styles}
               {:href href :class extra-styles})]
    [:li (if icon
           [:a opts
            [:span.icon [:i {:class (str "fa " icon)}]]
            (str " " title)]
           [:a opts title])]))

(defn v-menu [title items & {:keys [extra-styles]
                             :or {extra-styles ""}}]
  [:nav {:class (str "menu " extra-styles)}
   [:p.menu-label title]
   (into
    [:ul.menu-list]
    (for [i items]
      i))])
