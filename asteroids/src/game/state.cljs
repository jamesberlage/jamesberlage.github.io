(ns game.state
  (:require [game.asteroid :as ad]
            [game.piece :as pc]
            [game.point :as pt]))

(def id (atom 0))

(defn next-id
  "Returns the next generated id."
  []
  (let [fetched (atom nil)]
    (swap! id (fn [id]
                (reset! fetched id)
                (inc id)))
    @fetched))

;; State looks like this:
;;
;; {:changes {"(24,7)" 4}
;;  :pieces {}
;;  :status :ongoing}

(defn whiteout-point
  "Signals that a point should be cleared on the next render.  If the point is already owned by another object, we don't clear it.

Examples:
(whiteout-point {:changes {\"(24,7)\" 5}} {:x 24 :y 7}) ;; returns {:changes {\"(24,7)\" 5}}
(whiteout-point {:changes {}} {:x 24 :y 7}) ;; returns {:changes {\"(24,7)\" nil}}
"
  [state point]
  (let [point (pt/->string point)]
    (if (contains? (:changes state) point)
      state
      (update state :changes assoc point nil))))

(defn whiteout-points
  "See whiteout-point above.  Operates on a collection."
  [state points]
  (if (empty? points)
    state
    (whiteout-points (whiteout-point state (first points)) (rest points))))

(deftype CollisionError
  [id other])

(defn assign-point
  "Signals that a point should be filled for the given object on the next render.  An error is thrown if the object has collided with another object.

Examples:
(assign-point {:changes {}} {:x 24 :y 7} {:id 5}) ;; returns {:changes {\"(24,7)\" 5}}
(assign-point {:changes {\"(24,7)\" 5}} {:x 24 :y 7} {:id 5}) ;; returns {:changes {\"(24,7)\" 5}}
(assign-point {:changes {\"(24,7)\" 8}} {:x 24 :y 7} {:id 5}) ;; throws an error
" 
  [state point piece]
  (let [point (pt/->string point)]
    (when-let [old-id (get-in state [:changes point])]
      (when-not (= old-id (:id piece))
        (throw (CollisionError. (:id piece) old-id))))
    (update state :changes assoc point (:id piece))))

(defn assign-points
  "See assign-point above.  Operates on a collection."
  [state points piece]
  (if (empty? points)
    state
    (assign-points (assign-point state (first points) piece) (rest points) piece)))

(defn add-asteroid
  ""
  [state asteroid]
  (let [id (next-id)
        asteroid (assoc asteroid :id id)]
    (update state :pieces assoc id asteroid)))

(defn add-asteroids
  ""
  [state asteroids]
  (if (empty? asteroids)
    state
    (add-asteroids (add-asteroid state (first asteroids)) (rest asteroids))))

(defmulti determine-changes-for-piece
  ""
  #(:type %2))

(deftype SplitAsteroidsError
  [state])

(defmethod determine-changes-for-piece :asteroid
  [state asteroid]
  (let [;; Signal that the previous location(s) owned by this asteroid may need to be cleared.
        state (whiteout-points state (pc/old-points asteroid))]
    (try
      ;; Attempt to allocate points on the map to this asteroid.
      (assign-points state (pc/points asteroid) asteroid)
      (catch CollisionError e
        (let [other-asteroid (get-in state [:pieces (.-other e)])
              ;; Split the asteroids in two (if possible).
              new-asteroids (concat (ad/split asteroid) (ad/split other-asteroid))
              ;; Remove the exploded asteroids.
              state (update state :pieces dissoc (:id asteroid))
              state (update state :pieces dissoc (.-other e))
              ;; Add any debris (new-asteroids)
              state (add-asteroids new-asteroids)]
          (throw (SplitAsteroidsError. state)))))))

(defn determine-changes
  "Given the pieces in the game, this figures out what changes need to be made to the board."
  [state]
  (try
    (let [initial-state (assoc state :changes {})
          pieces (:pieces state)]
      (reduce determine-changes-for-piece initial-state pieces))
    ;; We bubble up the fact that any asteroids collided and were made into debris, since that
    ;; changes the number of pieces in the game.  We now need to recalculate what changes need to
    ;; be made to the board.
    (catch SplitAsteroidsError e
      (determine-changes (.-state e)))))

(defn move-piece
  ""
  [state piece]
  (update state :pieces assoc (:id piece) (pc/move piece)))

(defn move-pieces
  ""
  [state]
  (reduce move-piece state (:pieces state)))
