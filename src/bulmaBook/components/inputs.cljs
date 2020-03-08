(ns bulmaBook.components.inputs
  (:require [bulmaBook.utils :refer [spath value-of value->keyword]]
            [bulmaBook.components.icons :as icons]
            [bulmaBook.store :as store]
            [reagent.core :as r]))

(defn field [body & {:keys [label classes]}]
  (into
   [:div.field {:class (:field classes)}
    (when label
      [:label.label {:class (:label classes)} label])]
   (for [el body]
     el)))

(defn horizontal-field [label body & {:keys [classes]}]
  [:div.field.is-horizontal {:class (:field classes)}
   [:div.field-label
    [:label.label {:class (:label classes)}
     label]]
   (into
    [:div.field-body {:class [(:body classes)
                              (when (> (count body) 1)
                                "has-grouped")]}]
    (for [el body]
      el))])

(defn input-helper [type id doc chg-fn & {:keys [class placeholder required]}]
  [:input.input {:type (name type)
                 :class class
                 :id (name id)
                 :name (name id)
                 :placeholder placeholder
                 :required required
                 :value (store/get-in doc (spath id))
                 :on-change chg-fn}])

(defn input [_ sid & {:keys [model change-fn]}]
  (let [doc (or model
                (r/atom {}))
        chg-fn (if (fn? change-fn)
                 change-fn
                 (fn [e]
                   (store/assoc-in! doc (spath sid) (value-of e))))]
    (fn [type sid & {:keys [classes placeholder required icon-data]}]
      (if icon-data
        (into
         [:div.control {:class [(:control classes)
                                (icons/icon-control-class icon-data)]}
          [input-helper type sid doc chg-fn :class (:input classes)
           :placeholder placeholder :required required]]
         (for [i (icons/icon icon-data)]
           i))
        [:div.control {:class (:control classes)}
         [input-helper type sid doc chg-fn :class (:input classes)
          :placeholder placeholder :required required]]))))

(defn input-field [label type id & {:keys [classes placeholder required icon-data
                                           model change-fn]}]
  [field [[input type id :classes classes :placeholder placeholder
           :required required :icon-data icon-data :model model
           :change-fn change-fn]]
   :label label :classes classes])

(defn checkbox [_ sid & {:keys [model change-fn]}]
  (let [doc (or model
                (r/atom {}))
        chg-fn (if (fn? change-fn)
                 change-fn
                 #(store/update-in! doc (spath sid) not))]
    (fn [label sid & {:keys [classes checked required]}]
      [:div.field {:class (:field classes)}
       [:div.control {:class (:control classes)}
        [:label.checkbox {:class (:label classes)}
         [:input {:type "checkbox"
                  :id (name sid)
                  :name (name sid)
                  :checked checked
                  :required required
                  :on-change chg-fn}]
         (str " " label)]]])))

(defn radio [id _ & {:keys [model click-fn]}]
  (let [doc (or model
                (r/atom {}))
        clk-fn (if (fn? click-fn)
                 click-fn
                 #(store/assoc-in! doc (spath id)
                                   (value->keyword (value-of %))))]
    (fn [id labels & {:keys [classes]}]
      (into
       [:div.control {:class (:control classes)}]
       (for [l labels]
         [:label.radio {:class (:label classes)
                        :name (name id)
                        :value (or (:value l)
                                   (value->keyword (:title l)))
                        :checked (:checked l)
                        :on-click clk-fn}
          (:title l)])))))

(defn button [title action & {:keys [classes]}]
  [:div.field {:class (:field classes)}
   [:div.control {:class (:control classes)}
    [:button.button {:class (:button classes)
                     :type "button"
                     :on-click action}
     title]]])

(defn editable-field [_ src sid & _]
  (let [doc (r/atom {:value (store/get-in src (spath sid))
                     :editing false})
        save-fn (fn []
                  (store/assoc-in! src (spath sid) (:value @doc))
                  (store/update! doc :editing not))
        cancel-fn (fn []
                    (store/put! doc :value (store/get-in src (spath sid)))
                    (store/update! doc :editing not))]
    (fn [type _ _ & {:keys [label classes]}]
      (if (:editing @doc)
        [horizontal-field label [[field [[input type :value :model doc]
                                         [button "Save" save-fn
                                          :classes (:save-button classes)]
                                         [button "Cancel" cancel-fn
                                          :classes (:cancel-button classes)]]
                                  :classes {:field "has-addons"}]]
         :classes (:field-edit classes)]
        [horizontal-field label [[field [(if (= type :password)
                                           "********"
                                           (str (:value @doc)))
                                         [:span.icon
                                          {:on-click #(store/update!
                                                       doc
                                                       :editing not)}
                                          [:i.fa.fa-pencil]]]]]
         :classes (:field-view classes)]))))

(defn textarea [_ sid & {:keys [model change-fn]}]
  (let [doc (or model
                (r/atom {}))
        chg-fn (if (fn? change-fn)
                 change-fn
                 #(store/assoc-in! doc (spath sid) (value-of %)))]
    (fn [label sid & {:keys [classes placeholder]}]
      [:div.field {:class (:field classes)}
       (when label
         [:label.label {:class (:label classes)} label])
       [:p.control {:class (:control classes)}
        [:textarea.textarea {:class (:textarea classes)
                             :placeholder placeholder
                             :id (name sid)
                             :name (name sid)
                             :on-change chg-fn}]]])))

(defn option [title val & {:keys [option-class disabled selected label]}]
  [:option {:class option-class
            :disabled disabled
            :selected selected
            :label label
            :value val}
   title])

(defn select [id _ & {:keys [model change-fn]}]
  (let [doc (or model
                (r/atom {}))
        chg-fn (if (fn? change-fn)
                 change-fn
                 #(store/assoc-in! doc (spath id) (value-of %)))]
    (fn [id options & {:keys [select-class multiple rounded select-size
                             icon-data]}]
      [:div.select {:class [select-class
                            (when rounded "is-rounded")
                            (case select-size
                              :small "is-small"
                              :medium "is-medium"
                              :large "is-large"
                              nil)
                            (when multiple "is-multiple")]}
       (into
        [:select {:id (name id)
                  :name (name id)
                  :multiple (when multiple true false)
                  :size multiple
                  :on-change chg-fn}]
        (for [o options]
          o))
       [icons/icon-component icon-data]])))

(defn select-field [id options & {:keys [title classes multiple rounded select-size icon-data
                        model change-fn]}]
  [:div.field {:class (:field classes)}
   (when title
     [:div.label title])
   [select id options :classes classes :multiple multiple
    :rounded rounded :select-size select-size :icon-data icon-data
    :model model :change-fn change-fn]])

(defn file [sid & {:keys [action model change-fn]}]
  (let [doc (or model
                (r/atom {}))
        chg-fn (if (fn? change-fn)
                 change-fn
                 (fn [e]
                   (let [file-list (-> e .-target .-files)]
                     (when (.-length file-list)
                       (let [file (first (array-seq file-list))]
                         (store/assoc-in! doc (spath sid) (.-name file))
                         (when (fn? action)
                           (action file)))))))]
    (fn [id & {:keys [classes label right fullwidth boxed size position]}]
      [:div.field {:class (:field classes)}
   [:div.file.has-name {:class [(:file classes)
                                (when right "is-right")
                                (when fullwidth "is-fullwidth")
                                (when boxed "is-boxed")
                                (case size
                                  :small "is-small"
                                  :medium "is-medium"
                                  :large "is-large"
                                  nil)
                                (case position
                                  :center "is-centered"
                                  :right "is-right"
                                  nil)]}
    [:label.file-label {:class (:label classes)}
     [:input.file-input {:class (:input classes)
                         :id (name id)
                         :name (name id)
                         :type "file"
                         :on-change chg-fn}]
     [:span.file-cta {:class (:file-cta classes)}
      [:span.file-icon
       [:i.fa.fa-upload]]
      [:span.file-label
       (or label
           "Choose a file")]
      [:span.file-name
       (if (store/get-in doc (spath id))
         (str (store/get-in doc (spath id)))
         "No file chosen")]]]]])))

(defn search [_ & _]
  (let [doc (r/atom {})]
    (fn [action & {:keys [placeholder]}]
      (println (str "Search doc: " @doc))
      [:<>
       [field [[input :text :search :placeholder placeholder :model doc]
               [button "Search" #(action (:search @doc))]]
        :classes {:field "has-addons"}]])))
