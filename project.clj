(defproject com.nedap.staffing-solutions/utils.collections "0.3.1"
  :description "Utilities for collection processing"
  :url "https://github.com/nedap/utils.collections"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"releases" {:url      "https://nedap.jfrog.io/nedap/staffing-solutions/"
                             :username :env/artifactory_user
                             :password :env/artifactory_pass}}
  :deploy-repositories [["releases" {:url           "https://nedap.jfrog.io/nedap/staffing-solutions/"
                                     :sign-releases false}]]
  :repository-auth {#"https://nedap.jfrog\.io/nedap/staffing-solutions/"
                    {:username :env/artifactory_user
                     :password :env/artifactory_pass}}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [com.nedap.staffing-solutions/utils.spec "0.6.1"]])
