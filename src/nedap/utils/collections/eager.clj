(ns nedap.utils.collections.eager
  "ns for functions that compute their results eagerly.

  Accordingly, avoid processing inputs larger than memory."
  (:require
   [clojure.spec.alpha :as spec]
   [nedap.speced.def :as speced]
   [nedap.utils.collections.transients :as collections.transients]
   [nedap.utils.spec.predicates :refer :all]))

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

(def ^:dynamic *partitioning-pmap-runner*
  "The `clojure.core/map`-like function that `partitioning-pmap` will use.

  You may want to specify a different function for a finer-grained control of the underlying thread pool."
  pmap)

(speced/defn ^vector? partitioning-pmap
  "`clojure.core/pmap` replacement. Avoids creating more threads than necessary for CPU-bound tasks.

  e.g. for a `coll` of 20 items and a 6-core machine, 6 fixed threads are used at most, as opposed to 20 shifting threads."
  [^ifn? f
   ^::counted coll]
  (if-not (seq coll)
    (vec coll)
    (let [cpus (-> (Runtime/getRuntime) .availableProcessors)]
      (->> coll
           (divide-by cpus)
           (*partitioning-pmap-runner* (fn [work]
                                         (->> work
                                              (mapv f)
                                              transient)))
           (reduce collections.transients/into!)
           (persistent!)))))

(speced/defn first!
  "`clojure.core/first` which throws when a collection contains more than 1 item"
  [^seqable? coll]
  (if (<= (count coll) 1)
    (first coll)
    (throw (ex-info "Non unique collection" {:collection coll}))))
