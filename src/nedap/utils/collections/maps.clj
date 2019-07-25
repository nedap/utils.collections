(ns nedap.utils.collections.maps
  (:require
   [clojure.set :as set]
   [nedap.utils.spec.api :refer [check!]]
   [nedap.speced.def :as speced]))

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
