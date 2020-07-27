(ns bulmaBook.pages.profile
  (:require [theophilusx.yorick.input :as inputs]
            [theophilusx.yorick.basic :as basic]
            [theophilusx.yorick.utils :refer [value->keyword]]
            [theophilusx.yorick.store :as store]))

(defn profile-edit-form []
  (let [email-key (value->keyword (store/get-in
                                   store/global-state [:session :user :email]))
        fname-sid (keyword (str "users." (name email-key) ".first-name"))
        lname-sid (keyword (str "users." (name email-key) ".last-name"))
        pwd-sid (keyword (str "users." (name email-key) ".password"))
        classes {:save-button {:button "is-success"}}]
    [:form.box
     [inputs/horizontal-field "Email"
      [inputs/field [:div.content
                     (store/get-in store/global-state
                                   [:session :user :email])]]]
     [inputs/editable-field :text store/global-state fname-sid
      :label "First Name" :classes classes]
     [inputs/editable-field :text store/global-state lname-sid
      :label "Last Name" :classes classes]
     [inputs/editable-field :password store/global-state pwd-sid
      :label "Password" :classes classes]]))

(defn profile []
  [:div.columns
   [:div.column.is-4-tablet.is-3-desktop.is-2-widescreen]
   [:div.column
    [basic/breadcrumbs :ui.navbar [{:name "Profile"
                                    :value :profile
                                    :active true}]]
    [profile-edit-form]]])
