(defproject calorias-cli "0.1.0-SNAPSHOT"
  :description "Cliente de linha de comando da calculadora de calorias."
  :url "https://github.com/Px1906/calculadora-de-calorias"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [clj-http "3.9.1"]
                 [clojure.java-time "1.4.2"]
                 [cheshire "5.8.1"]]
  :main ^:skip-aot calorias-cli.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})