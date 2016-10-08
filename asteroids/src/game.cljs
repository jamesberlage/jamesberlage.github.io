(ns game
  (:require [game.state :as state]
            [game.util :as util]
            [reagent.core :as reagent]))

(defn invalid-viewport-component
  "A simple component that displays a message to the user telling them if their screen is too small to play the game effectively."
  []
  [:p "Sorry, to play this game you need a device with a larger screen."])

(defn end-game
  ""
  [meta-state]
  (when-let [id (:interval-id meta-state)]
    (js/window.clearInterval id))
  (assoc meta-state :interval-id nil))

(defn start-game-loop
  ""
  [st drawing-context]
  (state/render @st drawing-context)
  (js/window.setInterval (fn []
                           (swap! st state/step)
                           (state/render @st drawing-context))
                         1000))

(defn start-game
  ""
  [meta-state]
  (let [canvas (js/document.getElementById "asteroids")
        drawing-context (.getContext canvas "2d")
        st (atom (state/init))]
    ;; Clear the previous game, if any.
    (.clearRect drawing-context 0 0 util/width util/height)
    (assoc meta-state :interval-id (start-game-loop st drawing-context))))

(defn asteroids-component
  ""
  []
  (let [meta-state (reagent/atom {:interval-id nil, :game-ended? true})]
    (fn []
      [:div
       [:button {:on-click #(swap! meta-state start-game)} "Start game"]
       [:button {:on-click #(swap! meta-state end-game)} "End game"]
       [:canvas {:id "asteroids"
                 :style {:height util/height
                         :width util/width}}]])))

(defn game-component
  ""
  []
  ;; We don't support screens less than 500px.
  ;; Realistically, we don't support devices without a typical keyboard, but this is a good
  ;; heuristic.  It will exclude mobile devices.
  (if (< (.-innerWidth js/window) util/width)
    [invalid-viewport-component]
    [asteroids-component]))

(defn main
  "Entrypoint for the app."
  []
  (reagent/render [game-component] (js/document.getElementById "mnt")))

(main)
