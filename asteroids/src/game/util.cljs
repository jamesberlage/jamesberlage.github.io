(ns game.util)

(def height 500)
(def width 500)
(def pixel-length 10)
(def max-x (/ width pixel-length))
(def max-y (/ height pixel-length))

(defn random-int
  "Returns a random integer in the range [lower, upper)."
  [lower upper]
  (js/Math.floor (+ (* (js/Math.random) (- upper lower)) lower)))
