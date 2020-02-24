(ns unit.nedap.utils.collections.eager
  (:require
   [clojure.test :refer :all]
   [nedap.utils.collections.eager :as sut])
  (:import
   (clojure.lang ExceptionInfo)))

(deftest divide-by
  (testing "Basic behavior"
    (are [input n expected] (= expected
                               (sut/divide-by n input))
      []                           1 '()
      []                           3 '()
      [1]                          1 '((1))
      [1]                          3 '((1))
      [1 2]                        3 '((1) (2))
      [1 2 3]                      1 '((1 2 3))
      [1 2 3]                      3 '((1) (2) (3))
      [1 2 3 4]                    3 '((1 2) (3) (4))
      [1 2 3 4 5]                  3 '((1 2) (3 4) (5))
      [1 2 3 4 5]                  6 '((1) (2) (3) (4) (5))
      [1 2 3 4 5 6]                6 '((1) (2) (3) (4) (5) (6))
      [1 2 3 4 5 6 7]              6 '((1 2) (3) (4) (5) (6) (7))
      [1 2 3 4 5 6 7 8]            6 '((1 2) (3 4) (5) (6) (7) (8))
      [1 2 3 4 5 6 7 8 9 10 11 12] 6 '((1 2) (3 4) (5 6) (7 8) (9 10) (11 12))))

  (testing "Items are never dropped"
    (doseq [input-index (range 100)
            divisor (range 1 101)
            :let [input (range input-index)
                  output (sut/divide-by divisor input)]]
      (is (= (apply concat output)
             input)))))

(deftest partitioning-pmap
  (testing "Computes the same results as `clojure.core/map`"
    (are [f coll] (= (->> coll
                          (map f)
                          sort)
                     (->> coll
                          (sut/partitioning-pmap f)
                          sort))
      inc []
      inc [1]
      inc [1 2 3]))

  (testing "Runs things in parallel"
    (when (-> (Runtime/getRuntime)
              (.availableProcessors)
              (> 1))
      (let [proof (atom [])]
        (sut/partitioning-pmap (fn [_]
                                 (swap! proof conj (Thread/currentThread)))
                               [1 1])
        (is (= (-> @proof count)
               (-> @proof distinct count)))))))

(deftest first!
  (are [input] (thrown-with-msg? ExceptionInfo
                                 #"Non unique collection"
                                 (sut/first! input))
    [1 2 3]
    #{1 2 3})

  (are [input expected] (= expected
                           (sut/first! input))
    [1]   1
    [[1]] [1]

    []    nil
    [nil] nil))
