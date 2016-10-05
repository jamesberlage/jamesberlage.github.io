(ns game.vector)

(defrecord Vector
  ""
  [x y])

(defn split
  ""
  [vector]
  (let [x (js/Math.floor (/ (:x vector) 2))
        y (js/Math.floor (/ (:y vector) 2))]
    [(Vector. (- y) x)
     (Vector. y (- x))]))
