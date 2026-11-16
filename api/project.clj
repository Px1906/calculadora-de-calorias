(defproject calorias-api "0.1.0-SNAPSHOT"
  :description "API REST da calculadora de calorias: cadastro de usuário, registro de consumo/queima calórica e extrato por período."
  :url "https://github.com/Px1906/calculadora-de-calorias"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :min-lein-version "2.0.0"

  ;; Lista de dependências necessárias para o funcionamento da API.
  :dependencies [[org.clojure/clojure "1.11.1"]  ;; A linguagem Clojure.
                 [compojure "1.6.1"]           ;; Framework para definir rotas HTTP.
                 [cheshire "5.8.1"]            ;; Biblioteca para manipulação de JSON.
                 [ring/ring-defaults "0.3.2"]  ;; Configurações padrão para aplicações Ring.
                 [ring/ring-json "0.4.0"]      ;; Middleware para lidar com requisições e respostas JSON.
                 [clj-http "3.9.1"]            ;; Biblioteca para realizar requisições HTTP externas.
                 [clojure.java-time "1.4.2"]]  ;; Datas (filtro do extrato por período).

  ;; Plugins auxiliares para desenvolvimento.
  :plugins [[lein-ring "0.12.5"]  ;; Plugin para facilitar a execução da aplicação web.
            [lein-midje "3.2.2"]] ;; Plugin para suporte a testes automatizados com Midje.

  ;; Define o manipulador principal da aplicação.
  :ring {:handler calorias-api.handler/app
         :port 3001}

  ;; Configuração de perfis específicos para desenvolvimento.
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]      ;; Biblioteca para funcionalidades de servidor web.
                                  [ring/ring-mock "0.3.2"]               ;; Ferramenta para testes simulando requisições HTTP.
                                  [midje "1.10.10"]                      ;; Framework de testes para validação de código.
                                  [ring/ring-core "1.7.1"]               ;; Componentes essenciais do framework Ring.
                                  [ring/ring-jetty-adapter "1.7.1"]]}})
