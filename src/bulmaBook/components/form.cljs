(ns bulmaBook.components.form
  (:require [bulmaBook.utils :refer [spath value-of value->keyword]]
            [bulmaBook.components.icons :as icons]
            [reagent.session :as session]
            [reagent.core :as r]))

(defn field
  "A generic field component. The `body` argument is a vector of form control
  components. Available optional keyword arguments include
  `label` - a field label
  `classes` - a `map` of additional classes. Supported keys `:field` and `:label`
              element name."
  [body & {:keys [label classes]}]
  (into
   [:div.field {:class (:field classes)}
    (when label
      [:label.label {:class (:label classes)} label])]
   (for [el body]
     el)))

(defn horizontal-field
  "A component for the layout of horizontal fields in a form. The `label`
  argument becomes the label for the fields. The `body` argument is a vector
  of form control element components e.g. input element. Additional optional
  keyword arguments include
  `classes` - `map` of additional classes. Keys are `:field`, `:label` and `:body`"
  [label body & {:keys [classes]}]
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

(defn input-helper [type id class placeholder required]
  [:input.input {:type (name type)
                 :class class
                 :id (name id)
                 :name (name id)
                 :placeholder placeholder
                 :required required
                 :on-change #(session/assoc-in! (spath id) (value-of %))}])

(defn input
  "A basic `input` component. The `type` argument is a keyword representing the
  input type. Supported types are the HTML input types as keywords. The `id`
  argument is a keyword that defines the path into the session object used to
  store the value entered in the input component. The `id` keyword uses a
  period `.` as a path separator e.g. :person.first-name will be translated into
  `[:person :first-name]. Additional optional keyword arguments include
  `classes` - `map` of additional classes. Keys are `:control` and `:input`
  `placeholder` - placeholder text to place in the `input` element
  `required` - if true, add the required attribute to the `input` element
  `icon-data` - either an `icon` map (see `bulmaBook.components.icons`) or a
                vector of `icon` map objects"
  [type id & {:keys [classes placeholder required icon-data]}]
  (if icon-data
    (into
     [:div.control {:class [(:control classes)
                            (icons/icon-control-class icon-data)]}
      [input-helper type id (:input classes) placeholder required]]
     (for [i (icons/icon icon-data)]
       i))
    [:div.control {:class (:control classes)}
     [input-helper type id (:input classes) placeholder required]]))

(defn input-field
  "A basic input field component. The `label` argument will be the label
  associated with the field. The `type` argument is a keyword specifying the
  input filed type. The `id` is a keyword representing a path into the global
  session object. Any period `.` in the keyword is interpreted as a path
  separator e.g. :a.b.c will be translated into [:a :b :c]. Additional supported
  optional keyword arguments include
  `classes` - `map` of additional classes. Keys are `:field`, `:label`,
              `:control` and `input`.
  `placeholder` - placeholder text to be used in the `input` element
  `required` - if true, the `input` element has the required attribute added
  `icon-data` - either an `icon` data map (see `bulmaBook.components.icons`)
                or a vector of icon data maps"
  [label type id & {:keys [classes placeholder required icon-data]}]
  [field [[input type id :classes classes :placeholder placeholder
           :required required :icon-data icon-data]]
   :label label :classes classes])

(defn checkbox
  "A basic checkbox component. The `label` argument represents the text label
  displayed after the checkbox. The `id` argument is a keyword representing
  the path into the global session object where the value will be stored.
  The `id` keyword uses a period `.` as a path separator e.g. :a.b.c becomes
  [:a :b :c]. Additional optional keyword arguments are
  `classes` - `map` of additional classes. Keys are `:field`, `:label` and
              `:control`"
  [label id & {:keys [classes]}]
  [:div.field {:class (:field classes)}
   [:div.control {:class (:control classes)}
    [:label.checkbox {:class (:label classes)}
     [:input {:type "checkbox"
              :id (name id)
              :name (name id)
              :on-change (fn []
                           (session/update-in! (spath id) not))}]
     (str " " label)]]])

(defn radio
  "Basic radio button component. Selecting one of the radio button choices will
  store the value associated with the choice in the global session object in the
  path specified by `id`. The `id` argument is a keyword which specifies the
  path into the global session object where the value will be stored. The
  keyword uses a dot `.` as a path separator e.g. `:a.b.c` will become
  `[:a :b :c]`. The `labels` argument is a vector of maps where each map has at
  least the key `title`. Supported map keys are
  `title` - the text which will be used as the label for the radio button item
  `checked` - if true, this item will be checked
  `value` - if specified, represents the value that will be stored in the global
            session object when this radio button is selected. If not specified,
            the value defaults to the `title` converted to a keyword e.g.
            'this item' will become `:this-item`. The optional keyword argument
  `classes` is a map of additional classes to add. supported keys are `:control`
  and `:label`."
  [id labels & {:keys [classes]}]
  (into
   [:div.control {:class (:control classes)}]
   (for [l labels]
     [:label.radio {:class (:label classes)
                    :name (name id)
                    :value (or (:value l)
                               (value->keyword (:title l)))
                    :checked (or (:checked l)
                                 false)
                    :on-click #(session/assoc-in! (spath id)
                                                  (value->keyword (value-of %)))}
      (:title l)])))

(defn button
  "A basic button component. The `title` argument is the text that will be
  displayed on the button. The `action` argument is a function that will be
  executed when the buton is clicked on. Additional optional keyword args are
  `classes` - `map` of additional classes. Supported keys are `:field`,
  `:control` and `:button`."
  [title action & {:keys [classes]}]
  [:div.field {:class (:field classes)}
   [:div.control {:class (:control classes)}
    [:button.button {:class (:button classes)
                     :type "button"
                     :on-click action}
     title]]])

(defn do-save [s]
  (session/assoc-in! (spath (:sid @s)) (:value @s))
  (swap! s assoc :editing false))

(defn do-reset [s]
  (swap! s assoc :value (session/get-in (spath (:sid @s))))
  (swap! s assoc :editing false))

(defn editable-field
  "An editable field component. This component will display the value with a
  click handler event such that when the user clicks on the value, the field
  is replaced with an editable input field. The `label` argument will be used
  as the field label. The `sid` argument is a keyword path specifier
  which identifies the path into the global session object where the value will
  be edited. The keyword uses a period `.` as a path separator e.g. `:a.b.c`
  will become `[:a :b :c]`. Additional optional keyword arguments include
  `lasses` - `map` of additional class. Supported keys are `:field`, `:label`,
  `:control` and `:input`."
  [label sid type & {:keys [classes]}]
  (let [state (r/atom {:value (session/get-in (spath sid))
                       :sid sid
                       :editing false})]
    (fn [label sid type & _]
      (if (get @state :editing)
        [:div.field.is-grouped {:class (:field classes)}
         (when label
           [:label.label {:class (:label classes)} label])
         [:p.control {:class (:control classes)}
          [:input.input.is-small {:type (name type)
                                  :class (:input classes)
                                  :value (:value @state)
                                  :id (name (:sid @state))
                                  :name (name (:sid @state))
                                  :on-change (fn [e]
                                               (swap! state assoc :value
                                                      (value-of e)))}]]
         [:p.control
          [button "Save" #(do-save state)
           :classes {:button "is-primary is-small"}]]
         [:p.control
          [button "Reset" #(do-reset state)
           :classes {:button "is-small"}]]]
        [:div.field {:class (:field classes)}
         (when label
           [:label.label {:class (:label classes)} label])
         [:p.control {:class (:control classes)}
          (str (:value @state) " ")
          [:span.icon {:on-click #(swap! state assoc :editing true)}
           [:i.fa.fa-pencil]]]]))))

(defn textarea
  "A textarea component. The `label` argument specifies the label to be
  associated with the textarea. The `sid` argument is a keyword that
  specifies the path into the global state atom where the textarea data will
  be saved. The keyword uses a period `.` as a path separator e.g. `:a.b.c` will
  become `[:a :b :c]`. Additional optional keyword arguments include
  `classes` - `map` of additional classes to add. Supported keys are `:field`,
               `:control`, `:label` and `:textarea`.
  `placeholder` - placeholder text to add tot he `textarea` element."
  [label sid & {:keys [classes placeholder]}]
  [:div.field {:class (:field classes)}
   (when label
     [:label.label {:class (:label classes)} label])
   [:p.control {:class (:control classes)}
    [:textarea.textarea {:class (:textarea classes)
                         :placeholder placeholder
                         :id (name sid)
                         :name (name sid)
                         :on-change #(session/assoc-in!
                                      (spath sid) (value-of %))}]]])

(defn option
  "A basic `option` component. The `title` argument used as the label for the
  option. The `val` argument is the value associated with the option. Additional
  optional keyword arguments include
  `option-class` - additional classes added to the `option` element
  `disabled` - true if the option is disabled
  `selected` - true if the option is selected
  `label` - value for the `option` label attribute"
  [title val & {:keys [option-class disabled selected label]}]
  [:option {:class option-class
            :disabled disabled
            :selected selected
            :label label
            :value val}
   title])

(defn select
  "A `seledt` component. The `id` argument is a keyword specifying the path into
  the global session atom. The keyword uses a period `.` as a path separator.
  e.g. `:a.b.c` becomes `[:a :b :c]`. The `options` argument is a vector of
  `option` components representing the options presented by the `select`
  component. Additional optional keyword arguments include
  `select-class` - additional classes to add to the `select` element
  `multiple` - true if the `select` element allows multiple option selection
  `rounded` - true if the select styling should use rounded corners
  `select-size` - number of items to display in the select dropdown
  `icon-data` - either an icon data map (see `bulmaBook.components.icons`) or a
           vector of icon data maps which specify icons to add to the select
           component."
  [id options & {:keys [select-class multiple rounded select-size
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
              :on-change #(session/assoc-in! (spath id)
                                             (keyword (value-of %)))}]
    (for [o options]
      o))
   [icons/icon-component icon-data]])

(defn select-field
  "A select field component. Wraps a `select` component inside a `field`
  and `control` component. The `id` argument is a keyword which specifies the
  path into the global session atom where the value for the `select` component
  will be stored. The keyword uses a period `.` as a path separator e.g. `:a.b.c`
  will become `[:a :b :c]`. The `options` argument is a vector of `option`
  components. Additional optional keyword arguments include
  `title` - a title label to be associated with the `select` components
  `classes` - `map` of additional classes. Supported keys are `:field` and
              `:select`.
  `multiple` - true if the `select` component should support multiple options
  `rounded` - if true, `select` component will be styled with rounded corners
  `select-size` - number of select `options` to display
  `icon-data` - either an icon data map (see `bulmaBook.components.basic`) or a
           vector of icon data maps"
  [id options & {:keys [title classes multiple rounded select-size icon-data]}]
  [:div.field {:class (:field classes)}
   (when title
     [:div.label title])
   [select id options :classes classes :multiple multiple
    :rounded rounded :select-size select-size :icon-data icon-data]])

(defn file [id & {:keys [classes label action is-right is-fullwidth is-boxed
                         size position]}]
  [:div.field {:class (:field classes)}
   [:div.file.has-name {:class [(:file classes)
                                (when is-right "is-right")
                                (when is-fullwidth "is-fullwidth")
                                (when is-boxed "is-boxed")
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
                         :on-change (fn [e]
                                      (let [fileList (-> e .-target .-files)]
                                        (when (.-length fileList)
                                          (let [f (first (array-seq fileList))]
                                            (session/assoc-in! (spath id)
                                                               (.-name f))
                                            (when (fn? action)
                                              (action e))))))}]
     [:span.file-cta {:class (:file-cta classes)}
      [:span.file-icon
       [:i.fa.fa-upload]]
      [:span.file-label
       (or label
           "Choose a file")]
      [:span.file-name
       (if (session/get-in (spath id))
         (str (session/get-in (spath id)))
         "No file chosen")]]]]])
