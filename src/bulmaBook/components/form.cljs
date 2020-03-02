(ns bulmaBook.components.form
  (:require [bulmaBook.utils :refer [cs session-path value-of value->keyword]]
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
   [:div {:class (cs "field" field-class)}
    (when label
      [:label {:class (cs "label" label-class)} label])]
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
  [:div {:class (cs "field" "is-horizontal" field-class)}
   [:div {:class (cs "field-label" field-class)}
    [:label {:class (cs "label" label-class)}
     label]]
   (into
    [:div {:class (cs "field-body" body-class
                      (when (> (count body) 1)
                        "has-grouped"))}]
    (for [el body]
      el))])

(defn input-helper [type id class placeholder required]
  [:input {:type (name type)
           :class (cs "input" class)
           :id (name id)
           :name (name id)
           :placeholder placeholder
           :required required
           :on-change #(session/assoc-in! (session-path id) (value-of %))}])

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
     [:div {:class (cs "control" control-class
                     (when icon
                       (basic/icon-control-class icon)))}
      (input-helper type id input-class placeholder required)]
     (for [i (basic/icon icon)]
       i))
    [:div {:class (cs "control" control-class)}
     (input-helper type id input-class placeholder required)]))

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
  (field [[input type id :control-class control-class :input-class input-class
           :placeholder placeholder :required required :icon icon]]
         :label label :field-class field-class :label-class label-class))

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
  (let [path (session-path id)]
    [:div {:class (cs "field" field-class)}
     [:div {:class (cs "control" control-class)}
      [:label {:class (cs "checkbox" label-class)}
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
   [:div {:class (cs "control" control-class)}]
   (for [l labels]
     [:label {:class (cs "radio" label-class)
              :name (name id)
              :value (or (:value l)
                         (value->keyword (:title l)))
              :checked (or (:checked l)
                           false)
              :on-click #(session/assoc-in! (session-path id)
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
  [:div {:class (cs "field" field-class)}
   [:div {:class (cs "control" control-class)}
    [:button {:class (cs "button" button-class)
              :type "button"
              :on-click action}
     title]]])

(defn do-save [s]
  (session/assoc-in! (session-path (:session-id @s)) (:value @s))
  (swap! s assoc :editing false))

(defn do-reset [s]
  (swap! s assoc :value (session/get-in (session-path (:session-id @s))))
  (swap! s assoc :editing false))

(defn editable-field
  "An editable field component. This component will display the value with a
  click handler event such that when the user clicks on the value, the field
  is replaced with an editable input field. The `label` argument will be used
  as the field label. The `session-id` argument is a keyword path specifier
  which identifies the path into the global session object where the value will
  be edited. The keyword uses a period `.` as a path separator e.g. `:a.b.c`
  will become `[:a :b :c]`. Additional optional keyword arguments include
  `field-class` - additional classes to add to the `field` element
  `label-class` - additional classes to add to the `label` element
  `control-class` - additional classes to add to the `control` element
  `input-class` - additional class elements to add to the `input` element."
  [label session-id type & {:keys [field-class label-class
                                   control-class input-class]}]
  (let [state (r/atom {:value (session/get-in (session-path session-id))
                       :session-id session-id
                       :editing false})]
    (fn []
      (println (str "editable-field state: " @state))
      (if (get @state :editing)
        [:div {:class (cs "field" "is-grouped" field-class)}
         (when label
           [:label {:class (cs "label" label-class)} label])
         [:p {:class (cs "control" control-class)}
          [:input {:type (name type)
                   :class (cs "input" "is-small" input-class)
                   :value (:value @state)
                   :id (name (:session-id @state))
                   :name (name (:session-id @state))
                   :on-change (fn [e]
                                (swap! state assoc :value (value-of e)))}]]
         [:p {:class (cs "control")}
          [button "Save" #(do-save state) :button-class "is-primary is-small"]]
         [:p {:class (cs "control")}
          [button "Reset" #(do-reset state) :button-class "is-small"]]]
        [:div {:class (cs "field" field-class)}
         (when label
           [:label {:class (cs "label" label-class)} label])
         [:p {:class (cs "control" control-class)}
          (str (:value @state) " ")
          [:span {:class (cs "icon")
                  :on-click #(swap! state assoc :editing true)}
           [:i {:class (cs "fa" "fa-pencil")}]]]]))))

(defn textarea
  "A textarea component. The `label` argument specifies the label to be
  associated with the textarea. The `session-id` argument is a keyword that
  specifies the path into the global state atom where the textarea data will
  be saved. The keyword uses a period `.` as a path separator e.g. `:a.b.c` will
  become `[:a :b :c]`. Additional optional keyword arguments include
  `field-class` - additional classes to add to the `field` element
  `control-class` - additional classes to add to the `control` element
  `label-class` - additional classes to add to the `label` element
  `textarea-class` - additional classes to add to the `textarea` element
  `placeholder` - placeholder text to add tot he `textarea` element."
  [label session-id & {:keys [field-class control-class label-class
                              textarea-class placeholder]}]
  [:div {:class (cs "field" field-class)}
   (when label
     [:label {:class (cs "label" label-class)} label])
   [:p {:class (cs "control" control-class)}
    [:textarea {:class (cs "textarea" textarea-class)
                :placeholder placeholder
                :id (name session-id)
                :name (name session-id)
                :on-change #(session/assoc-in!
                             (session-path session-id) (value-of %))}]]])

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
  [:div {:class (cs "select" select-class
                    (when rounded "is-rounded")
                    (when select-size
                      (condp = select-size
                        :small "is-small"
                        :medium "is-medium"
                        :large "is-large"
                        (println (str "Unknown select size: " select-size))))
                    (when multiple "is-multiple"))}
   (into
    [:select {:id (name id)
              :name (name id)
              :multiple (when multiple true false)
              :size multiple
              :on-change #(session/assoc-in! (session-path id)
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
  [:div {:class (cs "field" field-class)}
   (when title
     [:div {:class "label"} title])
   [select id options :select-class select-class :multiple multiple
    :rounded rounded :select-size select-size :icon icon]])

(defn file [id & {:keys [file-class label-class input-class cta-class
                         label action]}]
  [:div {:class (cs "file" "has-name" file-class)}
   [:label {:class (cs "file-label" label-class)}
    [:input {:class (cs "file-input" input-class)
             :id (name id)
             :name (name id)
             :type "file"
             :on-change (fn [e]
                          (let [fileList (-> e .-target .-files)]
                            (when (.-length fileList)
                              (let [f (first (array-seq fileList))]
                                (session/assoc-in! (session-path id) (.-name f))
                                (when (fn? action)
                                  (action e))))))}]
    [:span {:class (cs "file-cta" cta-class)}
     [:span {:class (cs "file-icon")}
      [:i {:class (cs "fa" "fa-upload")}]]
     [:span {:class (cs "file-label")}
      (or label
          "Choose a file")]
     [:span {:class (cs "file-name")}
      (if (session/get-in (session-path id))
        (str (session/get-in (session-path id)))
        "No file chosen")]]]])
