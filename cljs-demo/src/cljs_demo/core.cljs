(ns cljs-demo.core
  (:require [cljs.js :as cljs]))

;; cljs.user must be set so that we can use defn.
(set! (-> js/window (.-cljs) (.-user)) #js{})

;; You may be thinking, "eval-user-input ? Really?  What a terrible thing to do."  You're not wrong, but
;; in this case we are only taking clojure and compiling it to javascript for execution.  Anything the
;; user can run here, they can already run by opening Chrome/Firefox/Edge/Safari developer tools and
;; typing in the appropriate javascript.
(defn eval-user-input
  "Takes some clojurescript as a string and evaluates it."
  [code-element result-element]
  (let [code (.-innerText code-element)]
    (cljs/eval-str (cljs/empty-state)
                   code
                   'eval-user-input.core
                   {:eval cljs/js-eval}
                   (fn [{:keys [error value]}]
                     (if error
                       (set! (.-innerText result-element) (pr-str error))
                       (set! (.-innerText result-element) (pr-str value)))))))

(defn main
  "Allows the user to compile clojurescript to javascript and run it in the browser."
  []
  (let [button-element (js/document.getElementById "eval")
        code-element (js/document.getElementById "code")
        result-element (js/document.getElementById "result")]
    (set! (.-onclick button-element) #(eval-user-input code-element result-element))))

(main)
