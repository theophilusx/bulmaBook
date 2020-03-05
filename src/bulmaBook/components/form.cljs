(ns bulmaBook.components.form
  (:require [bulmaBook.utils :refer [spath value-of value->keyword]]
            [bulmaBook.components.basic :as basic]
            [reagent.session :as session]
            [reagent.core :as r]))

(defn field
  "A generic field component. The `body` argument is a vector of form control
  components. Available optional keyword arguments include
  `label` - a field label
  `field-class` - additional classes to add to the `field` element
  `label-class` - additional classes to add to the label element"
  [body & {:keys [label field-class label-class]}]
  (into
   [:div.field {:class field-class}
    (when label
      [:label.label {:class label-class} label])]
   (for [el body]
     el)))

(defn horizontal-field
  "A component for the layout of horizontal fields in a form. The `label`
  argument becomes the label for the fields. The `body` argument is a vector
  of form control element components e.g. input element. Additional optional
  keyword arguments include
  `field-class` - additional classes to add to the field element
  `label-class` - additional classes to add to the label element
  `body-class` - additional classes to add to the `field-body` element"
  [label body & {:keys [field-class label-class
                        body-class]}]
  [:div.field.is-horizontal {:class field-class}
   [:div.field-label
    [:label.label {:class label-class}
     label]]
   (into
    [:div.field-body {:class [body-class
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
  `control-class` - additional classes to add to the `control` element
  `input-class` - additional classes to add to the `input` element
  `placeholder` - placeholder text to place in the `input` element
  `required` - if true, add the required attribute to the `input` element
  `icon` - either an `icon` map object (see `bulmaBook.components.basic`) or a
           vector of `icon` map objects"
  [type id & {:keys [control-class input-class placeholder required
                     icon]}]
  (if icon
    (into
     [:div.control {:class [control-class
                            (when icon
                              (basic/icon-control-class icon))]}
      [input-helper type id input-class placeholder required]]
     (for [i (basic/icon icon)]
       i))
    [:div.control {:class control-class}
     [input-helper type id input-class placeholder required]]))

(defn input-field
  "A basic input field component. The `label` argument will be the label
  associated with the field. The `type` argument is a keyword specifying the
  input filed type. The `id` is a keyword representing a path into the global
  session object. Any period `.` in the keyword is interpreted as a path
  separator e.g. :a.b.c will be translated into [:a :b :c]. Additional supported
  optional keyword arguments include
  `field-class` - additional classes to add tot he `field` element
  `label-class` - additional classes to be added to the `label` element
  `control-class` - additional classes to be added to the `control` element
  `input-class` - additional classes to be added tot he `input` element
  `placeholder` - placeholder text to be used in the `input` element
  `required` - if true, the `input` element has the required attribute added
  `icon` - either an `icon` data map (see `bulmaBook.components.basic`) or a
           vector of icon data maps"
  [label type id & {:keys [field-class label-class control-class
                           input-class placeholder required icon]}]
  [field [[input type id :control-class control-class :input-class input-class
           :placeholder placeholder :required required :icon icon]]
   :label label :field-class field-class :label-class label-class])

(defn checkbox
  "A basic checkbox component. The `label` argument represents the text label
  displayed after the checkbox. The `id` argument is a keyword representing
  the path into the global session object where the value will be stored.
  The `id` keyword uses a period `.` as a path separator e.g. :a.b.c becomes
  [:a :b :c]. Additional optional keyword arguments are
  `field-class` - additional classes to add to the `field` element
  `label-class` - additional classes to add to the `label` element
  `control-class` - additional classes to add to the `control` element"
  [label id & {:keys [field-class label-class control-class]}]
  (let [path (spath id)]
    [:div.field {:class field-class}
     [:div.control {:class control-class}
      [:label.checkbox {:class label-class}
       [:input {:type "checkbox"
                :id (name id)
                :name (name id)
                :on-change (fn []
                             (session/update-in! path not))}]
       (str " " label)]]]))

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
            'this item' will become `:this-item`."
  [id labels & {:keys [control-class label-class]}]
  (into
   [:div.control {:class control-class}]
   (for [l labels]
     [:label.radio {:class label-class
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
  `field-class` - additional classes to add to the `field` element
  `control-class` - additional classes to add to the `control` element
  `button-class` - additional classes to add to the `button` element"
  [title action & {:keys [field-class control-class button-class]}]
  [:div.field {:class field-class}
   [:div.control {:class control-class}
    [:button.button {:class button-class
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
  `field-class` - additional classes to add to the `field` element
  `label-class` - additional classes to add to the `label` element
  `control-class` - additional classes to add to the `control` element
  `input-class` - additional class elements to add to the `input` element."
  [label sid type & {:keys [field-class label-class
                                   control-class input-class]}]
  (let [state (r/atom {:value (session/get-in (spath sid))
                       :sid sid
                       :editing false})]
    (fn [label sid type & _]
      (if (get @state :editing)
        [:div.field.is-grouped {:class field-class}
         (when label
           [:label.label {:class label-class} label])
         [:p.control {:class control-class}
          [:input.input.is-small {:type (name type)
                                  :class input-class
                                  :value (:value @state)
                                  :id (name (:sid @state))
                                  :name (name (:sid @state))
                                  :on-change (fn [e]
                                               (swap! state assoc :value
                                                      (value-of e)))}]]
         [:p.control
          [button "Save" #(do-save state) :button-class "is-primary is-small"]]
         [:p.control
          [button "Reset" #(do-reset state) :button-class "is-small"]]]
        [:div.field {:class field-class}
         (when label
           [:label.label {:class label-class} label])
         [:p.control {:class control-class}
          (str (:value @state) " ")
          [:span.icon {:on-click #(swap! state assoc :editing true)}
           [:i.fa.fa-pencil]]]]))))

(defn textarea
  "A textarea component. The `label` argument specifies the label to be
  associated with the textarea. The `sid` argument is a keyword that
  specifies the path into the global state atom where the textarea data will
  be saved. The keyword uses a period `.` as a path separator e.g. `:a.b.c` will
  become `[:a :b :c]`. Additional optional keyword arguments include
  `field-class` - additional classes to add to the `field` element
  `control-class` - additional classes to add to the `control` element
  `label-class` - additional classes to add to the `label` element
  `textarea-class` - additional classes to add to the `textarea` element
  `placeholder` - placeholder text to add tot he `textarea` element."
  [label sid & {:keys [field-class control-class label-class
                              textarea-class placeholder]}]
  [:div.field {:class field-class}
   (when label
     [:label.label {:class label-class} label])
   [:p.control {:class control-class}
    [:textarea.textarea {:class textarea-class
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
  `icon` - either an icon data map (see `bulmaBook.components.basic`) or a
           vector of icon data maps which specify icons to add to the select
           component."
  [id options & {:keys [select-class multiple rounded select-size
                        icon]}]
  [:div.select {:class [select-class
                        (when rounded "is-rounded")
                        (when select-size
                          (case select-size
                            :small "is-small"
                            :medium "is-medium"
                            :large "is-large"
                            nil))
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
   [basic/icon-component icon]])

(defn select-field
  "A select field component. Wraps a `select` component inside a `field`
  and `control` component. The `id` argument is a keyword which specifies the
  path into the global session atom where the value for the `select` component
  will be stored. The keyword uses a period `.` as a path separator e.g. `:a.b.c`
  will become `[:a :b :c]`. The `options` argument is a vector of `option`
  components. Additional optional keyword arguments include
  `title` - a title label to be associated with the `select` components
  `field-class` - additional classes to add to the `field` components
  `select-class` - additional classes to add to the `select` components
  `multiple` - true if the `select` component should support multiple options
  `rounded` - if true, `select` component will be styled with rounded corners
  `select-size` - number of select `options` to display
  `icon` - either an icon data map (see `bulmaBook.components.basic`) or a
           vector of icon data maps"
  [id options & {:keys [title field-class select-class multiple
                        rounded select-size icon]}]
  [:div.field {:class field-class}
   (when title
     [:div.label title])
   [select id options :select-class select-class :multiple multiple
    :rounded rounded :select-size select-size :icon icon]])

(defn file [id & {:keys [field-class file-class label-class input-class cta-class
                         label action is-right is-fullwidth is-boxed size
                         position]}]
  [:div.field {:class field-class}
   [:div.file.has-name {:class [file-class
                                (when is-right "is-right")
                                (when is-fullwidth "is-fullwidth")
                                (when is-boxed "is-boxed")
                                (when size
                                  (case size
                                    :small "is-small"
                                    :medium "is-medium"
                                    :large "is-large"
                                    nil))
                                (when position
                                  (case position
                                    :center "is-centered"
                                    :right "is-right"
                                    nil))]}
    [:label.file-label {:class label-class}
     [:input.file-input {:class input-class
                         :id (name id)
                         :name (name id)
                         :type "file"
                         :on-change (fn [e]
                                      (let [fileList (-> e .-target .-files)]
                                        (when (.-length fileList)
                                          (let [f (first (array-seq fileList))]
                                            (session/assoc-in! (spath id) (.-name f))
                                            (when (fn? action)
                                              (action e))))))}]
     [:span.file-ctx {:class cta-class}
      [:span.file-icon
       [:i.fa.fa-upload]]
      [:span.file-label
       (or label
           "Choose a file")]
      [:span.file-name
       (if (session/get-in (spath id))
         (str (session/get-in (spath id)))
         "No file chosen")]]]]])
