(ns nedap.utils.collections.seq
  "Functions akin to the https://clojure.org/reference/sequences#_the_seq_library.

  All functions contained here should be lazy, else they should live in the `eager` ns."
  (:refer-clojure :exclude [flatten])
  (:require
   [nedap.utils.spec.predicates :refer :all]
   [nedap.utils.speced :as speced]))

(speced/defn ^sequential? distribute-evenly-by
  "Sorts `coll` by `f` in such a way that if partitioned by `n`, each partition will have items of similar cost.

  Refer to the tests for examples."
  [{:keys [^ifn? f, ^pos-integer? n]
    :or   {f identity
           n (-> (Runtime/getRuntime) .availableProcessors)}
    :as   ^{::speced/spec (fn [o]
                            (->> o keys (every? #{:f :n})))} options}
   ^coll? coll]
  (if-not (seq coll)
    coll
    (->> coll
         (sort-by f)
         (partition-all n)
         (map (fn [partition]
                (take n (concat partition (repeat ::padding)))))
         (apply map vector)
         (apply concat)
         (remove #{::padding}))))

(speced/defn ^sequential? flatten
  "Takes any nested combination of sequential things (lists, vectors, etc.)
  and returns their contents as a single, flat lazy sequence.

  (flatten nil) returns an empty sequence."
  [^::speced/nilable ^sequential? x]
  (clojure.core/flatten x))
