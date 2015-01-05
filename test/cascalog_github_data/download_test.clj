(ns cascalog-github-data.download-test
  (:use midje.sweet)
  (:require [clj-http.lite.client :as http]
            [cascalog-github-data.download :refer :all]
            [clj-time.core :as tc]
            [clj-time.periodic :as tp]
            [clj-time.format :as tf]
            [clojure.java.io :as io]
            ))

(facts "about `hour-range`"
       (hour-range "2015-01-01" "2015-01-02")
       => (take
            24
            (tp/periodic-seq
              (tf/parse (tf/formatter "yyyy-MM-dd")
                        "2015-01-01")
              (tc/hours 1))))

(facts "about `download-hour`"
       (fact "`2015-01-01 01`"
             (download-hour (tc/date-time 2015 1 1 1) "/tmp") => nil
             (provided 
               (http/get
                 "http://data.githubarchive.org/2015-01-01-1.json.gz"
                 anything)
               => {:status 200 :body (byte-array 10)}
               (io/output-stream "/tmp/2015-01-01-1.json.gz")
               => (io/output-stream "/tmp/2015-01-1.fake")
               ))
       (fact "`2015-01-01 11`"
             (download-hour (tc/date-time 2015 1 1 11) "/tmp") => nil
             (provided 
               (http/get
                 "http://data.githubarchive.org/2015-01-01-11.json.gz"
                 anything)
               => {:status 200 :body (byte-array 10)}
               (io/output-stream "/tmp/2015-01-01-11.json.gz")
               => (io/output-stream "/tmp/2015-01-11.fake")
               )))

(facts "about `download-hour-range`"
       (download-hour-range "2015-01-01" "2015-01-02" "/tmp") => nil
       (provided
         (download-hour (checker [h]
                                 (tc/within?
                                   (tc/date-time 2015 1 1 0)
                                   (tc/date-time 2015 1 1 23)
                                   h))
                        anything)
         => nil :times 24))
