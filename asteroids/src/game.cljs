(ns game
  (:require [game.state :as state]))

(defn main
  ""
  []
  (let [canvas (js/document.getElementById "mnt")
        drawing-context (.getContext canvas "2d")
        s (state/init)
        _ (js/console.log (pr-str s))
        _ (state/render s drawing-context)
        s (state/step s)
        _ (js/console.log (pr-str s))
        _ (state/render s drawing-context)]))

(main)
