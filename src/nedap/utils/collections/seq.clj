(ns nedap.utils.collections.seq
  "Functions akin to the https://clojure.org/reference/sequences#_the_seq_library.

  Functions contained here should be lazy, else they should live in the `eager` ns."
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
