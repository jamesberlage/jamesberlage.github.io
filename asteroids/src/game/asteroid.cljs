(ns game.asteroid
  (:require [game.piece :as piece]
            [game.point :as point]
            [game.util :as util]
            [game.vector :as vtr]))

(defrecord Asteroid
  [type id old-center center direction size])

(defn random
  "Returns a random asteroid anywhere on the screen, with a random directon of travel."
  []
  (let [pos-x (util/random-int 0 util/max-x)
        pos-y (util/random-int 0 util/max-y)]
    (Asteroid. :asteroid nil nil (point/Point. pos-x pos-y) (vtr/random) 2)))

(defn split
  "Splits a vector into \"debris\" (two smaller vectors), or removes the vector entirely, depending on its size."
  [asteroid]
  (case (:size asteroid)
    2 (let [[left-direction right-direction] (vtr/split (:direction asteroid))
            left-center (point/move (:center asteroid) (vtr/Vector. -1 0))
            right-center (point/move (:center asteroid) (vtr/Vector. 1 0))]
        [(Asteroid. :asteroid nil nil left-center left-direction 1)
         (Asteroid. :asteroid nil nil right-center right-direction 1)])
    1 []
    (throw (js/Error. "invalid size"))))

(defmethod piece/move :asteroid
  [asteroid]
  (let [old-center (:center asteroid)]
    (-> asteroid
        (assoc :old-center old-center)
        (assoc :center (point/move (:center asteroid) (:direction asteroid))))))

(defn points
  "Returns the points allocated to the asteroid.  If center-kw == :center, it will use the current center of the asteroid for reference; if center-kw == :old-center, it will use the old center."
  [asteroid center-kw]
  (let [center (get asteroid center-kw)]
    (case (:size asteroid)
      2 [center
         (point/move center (vtr/Vector. 1 0))
         (point/move center (vtr/Vector. -1 0))
         (point/move center (vtr/Vector. 0 1))
         (point/move center (vtr/Vector. 0 -1))]
      1 [center]
      (throw (js/Error. "invalid size")))))

(defmethod piece/old-points :asteroid
  [asteroid]
  (if (:old-center asteroid)
    (points asteroid :old-center)
    []))

(defmethod piece/points :asteroid
  [asteroid]
  (points asteroid :center))
