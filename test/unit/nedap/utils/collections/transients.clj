(ns unit.nedap.utils.collections.transients
  (:require
   [clojure.test :refer :all]
   [nedap.utils.collections.transients :as sut]))

(deftest into!
  (are [a b expected] (= expected
                         (-> (sut/into! (transient a)
                                        (transient b))
                             persistent!))
    []  []  []
    [1] []  [1]
    []  [1] [1]
    [1] [1] [1 1]))
