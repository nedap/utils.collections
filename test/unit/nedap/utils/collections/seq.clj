(ns unit.nedap.utils.collections.seq
  (:refer-clojure :exclude [flatten])
  (:require
   [clojure.test :refer :all]
   [nedap.utils.collections.seq :as sut])
  (:import
   (clojure.lang ExceptionInfo)))

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

(deftest flatten
  (testing "Basic behavior"
    (are [input expected] (testing input
                            (= expected
                               (sut/flatten input)))
      nil                               '()
      []                                '()
      [1 [[[[[[[[[[[[[[2]]]]]]]]]]]]]]] '(1 2)))

  (when *assert*
    (testing "Non-sequential top-level inputs are forbidden"
      (are [input] (thrown-with-msg? ExceptionInfo #"Validation failed" (with-out-str
                                                                          (sut/flatten input)))
        #{}
        {})))

  (testing "Non-sequential nested inputs are allowed"
    (are [input expected] (= expected
                             (sut/flatten input))
      [#{}]              '(#{})
      [#{1 2 3}]         '(#{1 2 3})
      [[[[[#{1 2 3}]]]]] '(#{1 2 3})
      [{}]               '({})
      [{1 2}]            '({1 2})
      [[[[[{1 2}]]]]]    '({1 2}))))
