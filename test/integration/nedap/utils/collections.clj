(ns integration.nedap.utils.collections
  (:require
   [clojure.test :refer :all]
   [nedap.utils.collections.eager :refer :all]
   [nedap.utils.collections.seq :refer :all]))

(deftest workload-partitioning
  (are [i n e] (= e
                  (->> i
                       (distribute-evenly-by {:n n})
                       (divide-by n)))
    [] 4 []
    [1 1 1 1 2 2 2 2 3 3 3 3 4 4 4 4] 4 '[(1 2 3 4) (1 2 3 4) (1 2 3 4) (1 2 3 4)])
  
  (doseq [_ (range 100)]
    (= '[(1 2 3 4) (1 2 3 4) (1 2 3 4) (1 2 3 4)]
       (->> [1 1 1 1 2 2 2 2 3 3 3 3 4 4 4 4]
            (shuffle)
            (distribute-evenly-by {:n 4})
            (divide-by 4)))))
