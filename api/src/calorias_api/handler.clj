(ns calorias-api.handler
  (:require
   [cheshire.core :as json]
   [compojure.core :refer [defroutes GET POST]]
   [compojure.route :as route]
   [calorias-api.armazenamento :as armazenamento]
   [calorias-api.lancamentos :as lancamentos]
   [calorias-api.servicos-externos :as servicos]
   [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
   [ring.middleware.json :refer [wrap-json-body]]))

(defn como-json
  ([dados] (como-json 200 dados))
  ([status dados]
   {:status status
    :headers {"Content-Type" "application/json"}
    :body (json/generate-string dados)}))

(defn- erro [mensagem]
  (como-json 400 {:erro mensagem}))

(defn cadastrar [corpo]
  (como-json (armazenamento/salvar-usuario! (select-keys corpo [:sexo :idade :peso]))))

(defn registrar-ganho [corpo]
  (if-not (lancamentos/ganho-valido? corpo)
    (erro "Informe alimento, quantidade (g) positiva e data (yyyy-MM-dd).")
    (let [calorias (servicos/obter-calorias-consumidas (:quantidade corpo) (:alimento corpo))]
      (como-json (armazenamento/adicionar-lancamento! (lancamentos/novo-ganho corpo calorias))))))

(defn registrar-perda [corpo]
  (let [peso (:peso (armazenamento/obter-usuario))]
    (cond
      (nil? peso)
      (erro "Faça o cadastro antes de registrar uma atividade.")

      (not (lancamentos/perda-valida? corpo))
      (erro "Informe atividade, duração (min) positiva e data (yyyy-MM-dd).")

      :else
      (let [calorias (servicos/obter-calorias-queimadas (:atividade corpo) (:duracao corpo) peso)]
        (como-json (armazenamento/adicionar-lancamento! (lancamentos/nova-perda corpo calorias)))))))

(defroutes app-routes
  (GET "/" [] "API da calculadora de calorias no ar")

  (GET "/cadastro" [] (como-json (armazenamento/obter-usuario)))
  (POST "/cadastro" {corpo :body} (cadastrar corpo))

  (POST "/ganho" {corpo :body} (registrar-ganho corpo))
  (POST "/perda" {corpo :body} (registrar-perda corpo))

  (GET "/extrato" [inicio fim]
    (como-json (lancamentos/filtrar-por-periodo (armazenamento/listar-lancamentos) inicio fim)))
  (GET "/saldo" [inicio fim]
    (como-json (lancamentos/saldo-por-periodo (armazenamento/listar-lancamentos) inicio fim)))

  (route/not-found "Recurso não encontrado"))

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true :bigdecimals? true})
      (wrap-defaults api-defaults)))
