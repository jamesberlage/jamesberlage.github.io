(ns game.piece)

(defmulti move
  "Takes a piece and moves it."
  :type)

(defmulti old-points
  "Returns a list of the points previously owned by the piece."
  :type)

(defmulti points
  "Returns a list of the points currently owned by the piece."
  :type)
