(ns bulmaBook.data.books
  (:require [reagent.session :as session]))

(defn init []
  (session/assoc-in! [:data :book-data] [{:title "TensorFlow For Machine Intelligence"
                                          :image "images/tensorflow.jpg"
                                          :cost "$22.99"
                                          :pages 270
                                          :isbn "9781939902351"}
                                         {:title "Docker in Production"
                                          :image "images/docker.jpg"
                                          :cost "$22.99"
                                          :pages 156
                                          :isbn "9781939902184"}
                                         {:title "Developing a Gulp.js Edge"
                                          :image "images/gulp.jpg"
                                          :cost "$22.99"
                                          :pages 134
                                          :isbn "9781939902146"}
                                         {:title "Learning Swift”"
                                          :image "images/swift.jpg"
                                          :cost "$22.99"
                                          :pages 342
                                          :isbn "9781939902115"}
                                         {:title "Choosing a JavaScript Framework"
                                          :image "images/js-framework.jpg"
                                          :cost "19.99"
                                          :pages 96
                                          :isbn "9781939902092"}
                                         {:title "Deconstructing Google Cardboard Apps"
                                          :image "images/google-cardboard.jpg"
                                          :cost "$22.99"
                                          :pages 179
                                          :isbn "9781939902092245"}]))
