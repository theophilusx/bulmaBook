(ns bulmaBook.components.modal
  (:require [bulmaBook.utils :refer [cs session-path]]
            [reagent.session :as session]))

(defn modal [body id & {:keys [modal-class background-class content-class close-class]}]
  [:div {:class (cs "modal" modal-class
                    (when (session/get-in (session-path id))
                      "is-active"))}
   [:div {:class (cs "modal-background" background-class)
          :on-click #(session/assoc-in! (session-path id) false)}]
   (into
    [:div {:class (cs "modal-content" content-class)}]
    (for [c body]
      c))
   [:button {:class (cs "modal-close" "is-large" close-class)
             :aria-label "close"
             :on-click #(session/assoc-in! (session-path id) false)}]])

(defn modal-card [body id & {:keys [modal-class background-class card-class
                                    header header-class body-class
                                    footer footer-class close-class]}]
  [:div {:class (cs "modal" modal-class
                    (when (session/get-in (session-path id))
                      "is-active"))}
   [:div {:class (cs "modal-background" background-class)
          :on-click #(session/assoc-in! (session-path id) false)}]
   [:div {:class (cs "modal-card" card-class)}
    (when header
      (into
       [:header {:class (cs "modal-card-head" header-class)}]
       (for [h header]
         h)))
    (into
     [:section {:class (cs "modal-card-body" body-class)}]
     (for [b body]
       b))
    (when footer
      (into
       [:footer {:class (cs "modal-card-foot" footer-class)}]
       (for [f footer]
         f)))]
   [:button {:class (cs "modal-close" "is-large" close-class)
             :aria-label "close"
             :on-click #(session/assoc-in! (session-path id) false)}]])
