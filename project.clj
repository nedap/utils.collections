;; Please don't bump the library version by hand - use ci.release-workflow instead.
(defproject com.nedap.staffing-solutions/utils.collections "2.0.0"
  ;; Please keep the dependencies sorted a-z.
  :dependencies [[com.nedap.staffing-solutions/speced.def "2.0.0"]
                 [com.nedap.staffing-solutions/utils.spec.predicates "1.1.0"]
                 [org.clojure/clojure "1.10.1"]]

  :description "Utilities for collection processing."

  :url "https://github.com/nedap/utils.collections"

  :min-lein-version "2.0.0"

  :license {:name "EPL-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :signing {:gpg-key "releases-staffingsolutions@nedap.com"}

  :repositories {"releases" {:url      "https://nedap.jfrog.io/nedap/staffing-solutions/"
                             :username :env/artifactory_user
                             :password :env/artifactory_pass}}

  :repository-auth {#"https://nedap.jfrog\.io/nedap/staffing-solutions/"
                    {:username :env/artifactory_user
                     :password :env/artifactory_pass}}

  :deploy-repositories {"clojars" {:url      "https://clojars.org/repo"
                                   :username :env/clojars_user
                                   :password :env/clojars_pass}}
  :target-path "target/%s"

  :test-paths ["src" "test"]

  :monkeypatch-clojure-test false

  :plugins [[lein-pprint "1.1.2"]]

  ;; A variety of common dependencies are bundled with `nedap/lein-template`.
  ;; They are divided into two categories:
  ;; * Dependencies that are possible or likely to be needed in all kind of production projects
  ;;   * The point is that when you realise you needed them, they are already in your classpath, avoiding interrupting your flow
  ;;   * After realising this, please move the dependency up to the top level.
  ;; * Genuinely dev-only dependencies allowing 'basic science'
  ;;   * e.g. criterium, deep-diff, clj-java-decompiler

  ;; NOTE: deps marked with #_"transitive" are there to satisfy the `:pedantic?` option.
  :profiles {:dev  {:dependencies [[cider/cider-nrepl "0.16.0" #_"formatting-stack needs it"]
                                   [com.clojure-goes-fast/clj-java-decompiler "0.3.0"]
                                   [com.nedap.staffing-solutions/utils.modular "2.1.0"]
                                   [com.stuartsierra/component "0.4.0"]
                                   [com.taoensso/timbre "4.10.0"]
                                   [criterium "0.4.5"]
                                   [formatting-stack "4.0.0"]
                                   [lambdaisland/deep-diff "0.0-47"]
                                   [medley "1.2.0"]
                                   [org.clojure/core.async "1.0.567"]
                                   [org.clojure/math.combinatorics "0.1.6"]
                                   [org.clojure/test.check "1.0.0"]
                                   [org.clojure/tools.namespace "1.0.0"]]
                    :exclusions   [org.clojure/clojurescript]
                    :plugins      [[lein-cloverage "1.1.1"]]
                    :source-paths ["dev"]
                    :repl-options {:init-ns dev}}

             :test {:dependencies [[com.nedap.staffing-solutions/utils.test "1.6.2"]]
                    :jvm-opts     ["-Dclojure.core.async.go-checking=true"]}

             :ci   {:pedantic?    :abort
                    :jvm-opts     ["-Dclojure.main.report=stderr"]
                    :global-vars  {*assert* true} ;; `ci.release-workflow` relies on runtime assertions
                    :dependencies [[com.nedap.staffing-solutions/ci.release-workflow "1.6.0"]]}})
