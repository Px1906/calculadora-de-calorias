(ns calorias-cli.cliente-api
  "Chamadas HTTP à calorias-api."
  (:require [clj-http.client :as http]
            [cheshire.core :as json]))

(def url-api
  "Endereço da API. Pode ser trocado pela variável de ambiente CALORIAS_API_URL."
  (or (System/getenv "CALORIAS_API_URL") "http://localhost:3001"))

(defn- rota [caminho]
  (str url-api caminho))

(defn- enviar [caminho dados]
  (http/post (rota caminho)
             {:headers {"Content-Type" "application/json"}
              :body (json/generate-string dados)}))

(defn- consultar
  ([caminho] (consultar caminho nil))
  ([caminho parametros]
   (-> (http/get (rota caminho)
                 {:query-params parametros
                  :headers {"Accept" "application/json"}})
       :body
       (json/parse-string true))))

(defn cadastrar! [cadastro]
  (enviar "/cadastro" cadastro))

(defn obter-cadastro []
  (consultar "/cadastro"))

(defn registrar-ganho!
  "Envia o alimento consumido. Devolve a resposta HTTP."
  [registro]
  (enviar "/ganho" registro))

(defn registrar-perda!
  "Envia a atividade física. Devolve a resposta HTTP."
  [registro]
  (enviar "/perda" registro))

(defn extrato [inicio fim]
  (consultar "/extrato" {"inicio" inicio "fim" fim}))

(defn saldo [inicio fim]
  (consultar "/saldo" {"inicio" inicio "fim" fim}))
