(ns bulmaBook.utils
  (:require [clojure.string :as string]))

(defn cs [& names]
  (string/join " " (filter identity names)))

(defn value-of [e]
  (-> e .-target .-value))

(defn vector-of-vectors [v]
  (every? vector? v))

(defn ensure-vector [v]
  (if (not (vector? v))
    [v]
    v))


