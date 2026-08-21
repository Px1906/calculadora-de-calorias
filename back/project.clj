(defproject nota5 "0.1.0-SNAPSHOT"
  :description "API de Calculadora de Calorias que precisamos NOTA 5"
  :url "http://example.com/nota5"
  :min-lein-version "2.0.0"

  ;; Lista de dependências necessárias para o funcionamento da API.
  :dependencies [[org.clojure/clojure "1.10.0"] ;; A linguagem Clojure.
                 [compojure "1.6.1"]          ;; Framework para definir rotas HTTP.
                 [cheshire "5.8.1"]           ;; Biblioteca para manipulação de JSON.
                 [ring/ring-defaults "0.3.2"] ;; Configurações padrão para aplicações Ring.
                 [ring/ring-json "0.4.0"]     ;; Middleware para lidar com requisições e respostas JSON.
                 [clj-http "3.9.1"]
                 [clojure.java-time "0.3.3"]]          ;; Biblioteca para realizar requisições HTTP externas.

  ;; Plugins auxiliares para desenvolvimento.
  :plugins [[lein-ring "0.12.5"]  ;; Plugin para facilitar a execução da aplicação web.
            [lein-midje "3.2.1"]] ;; Plugin para suporte a testes automatizados com Midje.

  ;; Define o manipulador principal da aplicação.
  :ring {:handler nota5.handler/app
         :port 3001}

  ;; Configuração de perfis específicos para desenvolvimento.
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]      ;; Biblioteca para funcionalidades de servidor web.
                                  [ring/ring-mock "0.3.2"]               ;; Ferramenta para testes simulando requisições HTTP.
                                  [midje "1.9.6"]                        ;; Framework de testes para validação de código.
                                  [ring/ring-core "1.7.1"]               ;; Componentes essenciais do framework Ring.
                                  [ring/ring-jetty-adapter "1.7.1"]]}})  ;; Adaptador para rodar a aplicação no servidor Jetty.

