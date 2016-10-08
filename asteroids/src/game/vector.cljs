(ns game.vector
  (:require [game.util :as util]))

(defrecord Vector
  [x y])

(defn random
  ""
  []
  (let [x (util/random-int -2 2)
        y (util/random-int -2 2)]
    (if (and (= x 0) (= y 0))
      (random)
      (Vector. x y))))

(defn split
  "Splits a vector by cutting its length in half, then rotating it 90 degrees right and left, respectively.  Does not take decimal parts into account."
  [vector]
  (let [x (js/Math.floor (/ (:x vector) 2))
        y (js/Math.floor (/ (:y vector) 2))]
    [(Vector. (- y) x)
     (Vector. y (- x))]))
