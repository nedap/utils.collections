(ns nedap.utils.collections.transients
  "Functions concerned with https://clojure.org/reference/transients"
  (:require
   [nedap.utils.speced :as speced])
  (:import
   (clojure.lang ITransientCollection)))

(speced/defn ^ITransientCollection into!
  "Analog to `clojure.core/into`."
  [^ITransientCollection a
   ^ITransientCollection b]
  (reduce conj! a (persistent! b)))
