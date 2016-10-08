(set-env! :dependencies '[[adzerk/boot-cljs "1.7.228-1"]
                          [org.clojure/clojure "1.8.0"]
                          [org.clojure/clojurescript "1.9.227"]
                          [reagent "0.6.0"]]
          :source-paths #{"src"})

(require '[adzerk.boot-cljs :refer [cljs]])

(deftask build
  "Compile the app."
  []
  (comp
    (cljs :compiler-options {:asset-path "asteroids/target/main.out"})
    (target)))
