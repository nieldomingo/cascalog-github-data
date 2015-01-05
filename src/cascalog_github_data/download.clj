(ns cascalog-github-data.download
  (:require [clj-http.lite.client :as http]
            [clj-time.core :as tc]
            [clj-time.periodic :as tp]
            [clj-time.format :as tf]
            [clojure.java.io :as io]
            ))

(defn hour-range [begin-date end-date]
  (let [date-format (tf/formatter "yyyy-MM-dd")
        begin-date (tf/parse date-format begin-date)
        end-date (tf/parse date-format end-date)]
    (take-while #(not= end-date %) (tp/periodic-seq begin-date (tc/hours 1)))))

(defn download-hour [hour output-dir]
  (let [output-file (str (tf/unparse (tf/formatter "yyyy-MM-dd-H") hour) ".json.gz")
        url (str "http://data.githubarchive.org/" output-file)
        output-path (str output-dir "/" output-file)
        req (http/get url {:as :byte-array})]
    (if (= (:status req)  200)
      (with-open [w (io/output-stream output-path)]
        (.write w (:body req))))))

(defn download-hour-range [begin-date end-date output-dir]
  (doseq [hour (hour-range begin-date end-date)]
    (download-hour hour output-dir)))

(defn -main [begin-date end-date output-dir]
  (download-hour-range begin-date end-date output-dir))

