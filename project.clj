(defproject cascalog-github-data "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [me.raynes/fs "1.4.6"]
                 [clj-http-lite "0.2.0"]
                 [clj-time "0.9.0"]]
  :profiles {:dev {:dependencies  [[midje "1.6.3"]]
                   :plugins  [[lein-midje "3.1.3"]]}}
  )
