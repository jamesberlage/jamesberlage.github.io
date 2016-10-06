(set-env! :dependencies '[[adzerk/boot-cljs "1.7.228-1"]
                          [org.clojure/clojure "1.8.0"]
                          [org.clojure/clojurescript "1.9.227"]]
          :source-paths #{"src"})

(require '[adzerk.boot-cljs :refer [cljs]])

(deftask compile-cljs
  "Compile the Clojurescript."
  []
  (comp
    (cljs :compiler-options {:asset-path "asteroids/target/main.out"})
    (target)))
