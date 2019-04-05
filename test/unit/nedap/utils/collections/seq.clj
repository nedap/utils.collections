(ns unit.nedap.utils.collections.seq
  (:require
   [clojure.test :refer :all]
   [nedap.utils.collections.seq :as sut]))

(deftest distribute-evenly-by
  (testing "Basic"
    (are [i n e] (= e
                    (sut/distribute-evenly-by {:n n} i))

      []                                1 []
      []                                2 []

      [1 1 2 2]                         1 [1 1 2 2]
      [1 1 2 2]                         2 [1 2, 1 2]
      [1 1 2 2]                         4 [1, 1, 2, 2]

      [1 1 1 1 2 2 2 2 3 3 3 3 4 4 4 4] 1 [1 1 1 1 2 2 2 2 3 3 3 3 4 4 4 4]
      [1 1 1 1 2 2 2 2 3 3 3 3 4 4 4 4] 2 [1 1 2 2 3 3 4 4, 1 1 2 2 3 3 4 4]
      [1 1 1 1 2 2 2 2 3 3 3 3 4 4 4 4] 4 [1 2 3 4, 1 2 3 4, 1 2 3 4, 1 2 3 4]

      [1 1 1 2 2 2 3 3 3]               3 '(1 2 3, 1 2 3, 1 2 3)))

  (testing "No items are ever lost or modified: they are only sorted"
    (doseq [input-index (range 100)
            n (range 1 101)
            :let [input (range input-index)
                  output (sut/distribute-evenly-by {:n n} input)]]
      (is (= (->> output sort)
             input)))))
