(ns game.point
  (:require [clojure.string :refer [split]]
            [game.util :refer [max-x max-y]]))

(defrecord Point
  [x y])

(defn ->string
  ""
  [point]
  (str (:x point) "," (:y point)))

(defn <-string
  ""
  [point]
  (let [[x y] (split point #"," 2)]
    (Point. x y)))

(defn move
  ""
  [point vector]
  (let [x (mod (+ (:x point) (:x vector)) max-x)
        y (mod (+ (:y point) (:y vector)) max-y)]
    (Point. x y)))
