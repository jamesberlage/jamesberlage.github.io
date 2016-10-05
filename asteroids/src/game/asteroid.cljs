(ns game.asteroid
  (:require [game.piece :as piece]
            [game.point :as point]
            [game.vector :as vtr]))

(defrecord Asteroid
  [type id old-center center direction size])

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
    (case size
      2 [center
         (point/move center (vtr/Vector. 1 0))
         (point/move center (vtr/Vector. -1 0))
         (point/move center (vtr/Vector. 0 1))
         (point/move center (vtr/Vector. 0 -1))]
      1 [center]
      (throw (js/Error. "invalid size")))))

(defmethod piece/old-points :asteroid
  [asteroid]
  (points asteroid :old-center))

(defmethod piece/points :asteroid
  [asteroid]
  (points asteroid :center))
