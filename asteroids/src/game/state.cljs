(ns game.state
  (:require [game.asteroid :as ad]
            [game.piece :as pc]
            [game.point :as pt]
            [game.util :as util]))

(def id (atom 0))

(defn move-piece
  "Moves an individual piece, updating the state with the new location."
  [state piece]
  (update state :pieces assoc (:id piece) (pc/move piece)))

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

(defn next-id
  "Returns the next generated id."
  []
  (let [fetched (atom nil)]
    (swap! id (fn [id]
                (reset! fetched id)
                (inc id)))
    @fetched))

(defn add-asteroid
  "Adds an asteroid to the pieces on the board, assigning it a new unique id."
  [state asteroid]
  (let [id (next-id)
        asteroid (assoc asteroid :id id)]
    (update state :pieces assoc id asteroid)))

(defmulti determine-changes-for-piece
  "Figures out what changes need to be made for the given piece."
  #(:type %2))

(deftype SplitAsteroidsError
  [state])

(defmethod determine-changes-for-piece :asteroid
  [state asteroid]
  (let [;; Signal that the previous location(s) owned by this asteroid may need to be cleared.
        state (reduce whiteout-point state (pc/old-points asteroid))]
    (try
      ;; Attempt to allocate points on the map to this asteroid.
      (reduce #(assign-point %1 %2 asteroid) state (pc/points asteroid))
      (catch CollisionError e
        (let [other-asteroid (get-in state [:pieces (.-other e)])
              ;; Split the asteroids in two (if possible).
              new-asteroids (concat (ad/split asteroid) (ad/split other-asteroid))
              ;; Remove the exploded asteroids.
              state (update state :pieces dissoc (:id asteroid))
              state (update state :pieces dissoc (.-other e))
              ;; Add any debris (new-asteroids)
              state (reduce add-asteroid state new-asteroids)]
          (throw (SplitAsteroidsError. state)))))))

(defn determine-changes
  "Given the pieces in the game, this figures out what changes need to be made to the board."
  [state]
  (try
    (let [initial-state (assoc state :changes {})
          pieces (vals (:pieces state))]
      ;; Attempt to give each piece the points on the board it wants.
      (reduce determine-changes-for-piece initial-state pieces))
    ;; We bubble up the fact that any asteroids collided and were made into debris, since that
    ;; changes the number of pieces in the game.  We now need to recalculate what changes need to
    ;; be made to the board.
    (catch SplitAsteroidsError e
      (determine-changes (.-state e)))))

(defrecord State
  [changes pieces])

(defn init
  "Gets the initial state for the game."
  []
  (let [new-asteroids (repeatedly 10 ad/random)
        state (State. {} {})
        state (reduce add-asteroid state new-asteroids)
        state (determine-changes state)]
    state))

(defn step
  "Moves pieces on the board, then figures out what changes to apply on the next render."
  [state]
  (let [;; Move each piece on the board to its new location.
        pieces (vals (:pieces state))
        state (reduce move-piece state pieces)
        ;; Figure out what changes need to be made on the next render.
        state (determine-changes state)]
    state))

(defn render
  ""
  [state drawing-context]
  (doseq [[point id] (:changes state)]
    (let [point (pt/string-> point)
          x (* (:x point) util/pixel-length)
          y (* (:y point) util/pixel-length)]
      (if id
        (do
          (set! (.-fillStyle drawing-context) "blue")
          (.fillRect drawing-context x y util/pixel-length util/pixel-length))
        (.clearRect drawing-context x y util/pixel-length util/pixel-length)))))
