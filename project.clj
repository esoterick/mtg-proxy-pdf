(defproject mtg-proxy-pdf "0.1.0-SNAPSHOT"
  :source-paths ["src/clj"]
  :description "Given a list of magic cards, create a file (html or pdf) of the card images."
  :url "https://github.com/jvalentini/mtg-proxy-pdf"
  :min-lein-version "2.0.0"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[cheshire "5.3.1"]
                 [clj-pdf "1.11.15"]
                 [com.novemberain/monger "2.0.0"]
                 [compojure "1.2.2"]
                 [enlive "1.1.5"]
                 [hiccup "1.0.5"]
                 [http-kit "2.1.16"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2850"]
                 [org.clojure/core.cache "0.6.4"]
                 [org.clojure/data.json "0.2.4"]
                 [org.clojure/tools.cli "0.3.1"]
                 [reagent "0.5.0-alpha3"]
                 [ring/ring-codec "1.0.0"]
                 [ring/ring-core "1.3.2"]
                 [ring/ring-json "0.3.1"]]

  :plugins [[lein-cljsbuild "1.0.4"]
            [lein-ring "0.8.13"]]

  :ring {:handler mtg-proxy-pdf.server/app}

  :cljsbuild {:builds [{:source-paths ["src/cljs"]
                        :compiler {:output-to "resources/public/js/client.js"
                                   :output-dir "resources/public/js/out"
                                   :optimizations :none
                                   :source-map true}}]}

  :profiles { :dev {:dependencies [[org.clojure/tools.trace "0.7.6"]]}}

  :main mtg-proxy-pdf.core
  :aot [mtg-proxy-pdf.core])
