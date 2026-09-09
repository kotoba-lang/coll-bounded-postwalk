(ns kotoba.coll.bounded-postwalk
  "bounded-postwalk -- one definition, addressed on its own.

  Split out of kotoba.lang.text on 2026-09-09. The unit here is the
  DEFINITION, not the library: this repo holds bounded-postwalk and names, in its
  deps.edn, exactly the definitions bounded-postwalk reaches. Nothing else."
  (:require [kotoba.coll.default-max-walk-depth :refer [default-max-walk-depth]]
            [kotoba.coll.walk :refer [walk]]
            [kotoba.coll.walk-children :refer [walk-children]]
            [kotoba.coll.walk-depth-exceeded :refer [walk-depth-exceeded!]]))

(defn bounded-postwalk
  "Like clojure.walk/postwalk: apply f to form's children first, then to
  form itself, bottom-up. Bounded by max-depth (default
  default-max-walk-depth); throws ex-info rather than recursing without
  limit once the ceiling is crossed."
  ([f form] (bounded-postwalk f default-max-walk-depth form))
  ([f max-depth form]
   (letfn [(walk [x depth]
             (when (> depth max-depth) (walk-depth-exceeded! max-depth))
             (f (walk-children walk x (inc depth))))]
     (walk form 0))))
