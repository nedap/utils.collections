(ns nedap.utils.collections.maps
  (:require
   [clojure.set :as set]
   [nedap.speced.def :as speced]
   [nedap.utils.spec.api :refer [check!]]))

(speced/defn ^boolean? unambigiously-invertable?
  [^map? m]
  (let [vs (vals m)]
    (or (empty? vs)
        (apply distinct? vs))))

(speced/defn ^map? invert
  "Returns the map with the vals mapped to the keys."
  [^map? m]
  {:pre [(check! unambigiously-invertable? m)]}
  (set/map-invert m))
