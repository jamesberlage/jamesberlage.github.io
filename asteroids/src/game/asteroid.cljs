(ns game.asteroid
  (:require [game.piece :as piece]
            [game.point :as point]
            [game.util :as util]
            [game.vector :as vtr]))

(defrecord Asteroid
  [type id old-center center direction size])

(defn random
  ""
  []
  (let [pos-x (util/random-int 0 util/max-x)
        pos-y (util/random-int 0 util/max-y)
        dir-x (util/random-int -5 5)
        dir-y (util/random-int -5 5)]
    (Asteroid. :asteroid nil nil (point/Point. pos-x pos-y) (vtr/Vector. dir-x dir-y) 2)))

(defn split
  ""
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
  ""
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
