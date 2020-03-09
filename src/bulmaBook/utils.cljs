(ns bulmaBook.utils
  (:require [clojure.string :as string]))

(defn cs [& names]
  (string/join " " (filter identity names)))

(defn value-of [e]
  (-> e .-target .-value))

(defn vector-of-vectors [v]
  (every? vector? v))

(defn ensure-vector [v]
  (if-not (vector? v)
    [v]
    v))

(defn spath [kw & {:keys [prefix]}]
  (let [init (if prefix
               [prefix]
               [])]
    (reduce (fn [acc v]
              (conj acc (keyword v)))
            init (string/split (name kw) #"\."))))

(defn value->keyword [v]
  (keyword (string/replace v #"\.|:|@|\ " "-")))

(defn initcaps [s]
  (string/join " " (map string/capitalize (string/split s #" "))))

(defn keyword->str [kw & {:keys [initial-caps]}]
  (let [s (string/replace (name kw) #"-" " ")]
    (if initial-caps
      (initcaps s)
      s)))
