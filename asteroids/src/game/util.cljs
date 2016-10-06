(ns game.util)

(def max-x 50)
(def max-y 50)

(defn random-int
  ""
  [lower upper]
  (js/Math.floor (+ (* (js/Math.random) (- upper lower)) lower)))
