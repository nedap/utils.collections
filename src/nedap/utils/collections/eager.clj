(ns nedap.utils.collections.eager
  "ns for functions that compute their results eagerly.

  Accordingly, avoid processing inputs larger than memory."
  (:require
   [clojure.spec.alpha :as spec]
   [nedap.utils.collections.transients :as collections.transients]
   [nedap.utils.spec.predicates :refer :all]
   [nedap.utils.speced :as speced])
  (:import
   (clojure.lang Repeat)))

(spec/def ::counted (fn [x]
                      (try
                        (count x)
                        (catch Throwable _ ;; Throwable: catch OOMs too (e.g. trying to count an infinite sequence)
                          false))))

(spec/def ::divisions (spec/coll-of sequential? :kind vector?))

(speced/defn ^::divisions divide-by
  "Divides `coll` in `n` parts. The parts can have disparate sizes if the division isn't exact."
  [^pos-integer? n
   ^::counted coll]
  (let [the-count (count coll)
        seed [(-> the-count double (/ n) Math/floor)
              (rem the-count n)
              []
              coll]
        recipe (iterate (fn [[quotient remainder output input]]
                          (let [chunk-size (+ quotient (if (pos? remainder)
                                                         1
                                                         0))
                                addition (take chunk-size input)
                                result (cond-> output
                                         (seq addition) (conj addition))]
                            [quotient
                             (dec remainder)
                             result
                             (drop chunk-size input)]))
                        seed)
        index (inc n)]
    (-> recipe
        (nth index)
        (nth 2))))

(speced/defn ^vector? partitioning-pmap
  "`clojure.core/pmap` replacement. Avoids creating more threads than necessary for CPU-bound tasks.

  e.g. for a `coll` of 20 items and a 6-core machine, 6 fixed threads are used at most, as opposed to 20 shifting threads."
  [^ifn? f
   ^::counted coll]
  (if-not (seq coll)
    coll
    (let [cpus (-> (Runtime/getRuntime) .availableProcessors)]
      (->> coll
           (divide-by cpus)
           (pmap (fn [work]
                   (->> work
                        (mapv f)
                        transient)))
           (reduce collections.transients/into!)
           (persistent!)))))