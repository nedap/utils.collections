(ns unit.nedap.utils.collections.maps
  (:require
   [clojure.test :refer :all]
   [nedap.utils.collections.maps :as sut]))

(deftest unambigiously-invertable?
  (are [input expected] (testing input
                          (= expected
                             (sut/unambigiously-invertable? input)))
    {}         true
    {1 1}      true
    {1 2}      true
    {1 2, 2 2} false))

(deftest invert
  (testing "Basic behavior"
    (are [input expected] (testing input
                            (= expected
                               (sut/invert input)))
      {}    {}
      {1 1} {1 1}
      {1 2} {2 1}))

  (when *assert*
    (testing "Conflicting would-be keys are forbidden"
      (is (spec-assertion-thrown? 'unambigiously-invertable? (sut/invert {:a 1 :b 1}))))))
