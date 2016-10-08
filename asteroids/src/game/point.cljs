(ns game.point
  (:require [clojure.string :refer [split]]
            [game.util :refer [max-x max-y]]))

(defrecord Point
  [x y])

(defn ->string
  "Turns a point into a uniquely identifiable string."
  [point]
  (str (:x point) "," (:y point)))

(defn string->
  "Turns a string of the form \"x,y\" into a Point."
  [point]
  (let [[x y] (split point #"," 2)]
    (Point. x y)))

(defn move
  "Moves a point along a vector."
  [point vector]
  (let [x (mod (+ (:x point) (:x vector)) max-x)
        y (mod (+ (:y point) (:y vector)) max-y)]
    (Point. x y)))
