(set-env! :dependencies '[[adzerk/boot-cljs "1.7.228-1"]
                          [org.clojure/clojure "1.8.0"]
                          [org.clojure/clojurescript "1.9.227"]]
          :source-paths #{"src"})

(require '[adzerk.boot-cljs :refer [cljs]])

(deftask build
  "Compile the app."
  []
  (comp
    (cljs ;; The :parallel-build compiler option causes issues with bootstrapped clojurescript, or I
          ;; would use it.
          :compiler-options {:asset-path "cljs-demo/target/main.out"
                             :static-fns true
                             :verbose true}
          ;; Bootstrapped CLJS only supports simple optimizations, unfortunately:
          ;; https://github.com/clojure/clojurescript/wiki/Compiler-Options#optimizations
          :optimizations :simple)
    (target)))
