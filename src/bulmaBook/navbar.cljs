(ns bulmaBook.navbar)

(defn navbar-brand
  "Add basic navbar brand component."
  [body & {:keys [burger]}]
  (if burger
    [:div.navbar-brand
     body
     [:div.navbar-burger
      [:span]
      [:span]
      [:span]]]
    [:div.navbar-brand body]))

(defn navbar-item-link [title & {:keys [expanded tab href icon extra-styles id]
                                 :or [expanded false
                                      tab false
                                      href "#"
                                      extra-styles ""]}]
  (let [opts (cond
               (and expanded tab)
               {:class (str "navbar-item is-expanded is-tab" extra-styles)
                :href href} 
               expanded {:class (str "navbar-item is-expanded" extra-styles)
                         :href href} 
               tab {:class (str "navbar-item is-tab" extra-styles)
                    :href href} 
               :default {:class "navbar-item"
                         :href href})]
    [:a (if id
          (assoc opts :id id)
          opts)
     (if icon
       [:div
        [:span.icon.is-small
         [:i {:class (str "fa " icon)}]]
        (str " " title)])]))

(defn navbar-item-div [items & {:keys [expanded tab extra-styles id]
                                :or {expanded false
                                     tab false
                                     extra-styles ""}}]
  (let [opts (cond
                (and expanded tab)
                {:class  (str "navbar-item is-expanded is-tab" extra-styles)}
                expanded {:class (str "navbar-item is-expanded" extra-styles)}
                tab {:class (str "navbar-item is-tab" extra-styles)}
                :default {:class (str "navbar-item" extra-styles)})]
    (into
     [:div (if id
             (assoc opts :id id)
             opts)]
     (for [i items]
       i))))


(defn navbar-dropdown-item [title & {:keys [icon id extra-styles]
                                     :or {extra-styles ""}}]
  [navbar-item-link title :icon icon :id id :extra-styles extra-styles])

(defn navbar-dropdown-menu [title items & {:keys [hoverable extra-styles id]
                                           :or [extra-styles ""]}]
  (let [opts (if hoverable
               {:class (str "navbar-item has-dropdown is-hoverable" extra-styles)}
               {:class (str "navbar-item has-dropdown" extra-styles)})
        d-menu (into
                [:div.navbar-dropdown]
                (for [i items]
                  i))]
    [:div (if id
            (assoc opts :id id)
            opts)
     [:div.navbar-link title]
     d-menu]))


(defn navbar-menu [items & {:keys [end]}]
  (let [start (into
               [:div.navbar-start]
               (for [i items]
                 i))
        m-end (if end
                (into
                 [:div.navbar-end]
                 (for [i end]
                   i)))]
    (if end
      [:div.navbar-menu start m-end]
      [:div.navbar-menu start])))

(defn navbar [menu & {:keys [shadow brand extra-styles]
                      :or {extra-styles ""}}]
  (let [opts (if shadow
               {:class (str "navbar has-shadow " extra-styles)}
               {:class (str "navbar " extra-styles)})]
    (if brand
      [:nav opts
       brand
       menu]
      [:nav opts menu])))
