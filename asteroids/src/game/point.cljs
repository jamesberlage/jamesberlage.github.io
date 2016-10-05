(ns game.point
  (:require [game.globals :refer [max-x max-y]]))

(defrecord Point
  ""
  [x y])

(defn id
  ""
  [point]
  (str "(" (:x point) "," (:y point) ")"))

(defn move
  ""
  [point vector]
  (let [x (mod (+ (:x point) (:x vector)) max-x)
        y (mod (+ (:y point) (:y vector)) max-y)]
    (Point. x y)))
