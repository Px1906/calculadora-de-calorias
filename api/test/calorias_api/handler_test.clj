(ns calorias-api.handler-test
  (:require [midje.sweet :refer :all]
            [cheshire.core :as json]
            [ring.mock.request :as mock]
            [calorias-api.armazenamento :as armazenamento]
            [calorias-api.handler :refer [app]]
            [calorias-api.servicos-externos :as servicos]))

(defn- requisitar
  ([metodo caminho] (requisitar metodo caminho nil))
  ([metodo caminho corpo]
   (let [resposta (app (cond-> (mock/request metodo caminho)
                         corpo (mock/json-body corpo)))]
     (update resposta :body #(json/parse-string % true)))))

(defn- consultar [caminho inicio fim]
  (:body (requisitar :get (str caminho "?inicio=" inicio "&fim=" fim))))

(background
  (before :facts (armazenamento/limpar!))
  (around :facts (with-redefs [servicos/obter-calorias-consumidas (fn [_ _] 250)
                               servicos/obter-calorias-queimadas (fn [_ _ _] 100)]
                   ?form)))

(facts "cadastro"
  (fact "começa vazio"
    (:body (requisitar :get "/cadastro")) => {})

  (fact "guarda e devolve o usuário"
    (requisitar :post "/cadastro" {:sexo 1 :idade 25 :peso 70})
    (:body (requisitar :get "/cadastro")) => {:sexo 1 :idade 25 :peso 70}))

(facts "lançamentos"
  (fact "registra ganho e perda e calcula extrato e saldo"
    (requisitar :post "/cadastro" {:sexo 1 :idade 25 :peso 70})
    (:status (requisitar :post "/ganho" {:alimento "arroz" :quantidade 200 :data "2026-09-01"})) => 200
    (:status (requisitar :post "/perda" {:atividade "correr" :duracao 30 :data "2026-09-02"})) => 200

    (map :tipo (consultar "/extrato" "2026-09-01" "2026-09-30")) => ["ganho" "perda"]
    (consultar "/saldo" "2026-09-01" "2026-09-30") => 150
    (consultar "/saldo" "2026-10-01" "2026-10-31") => 0)

  (fact "recusa ganho inválido e não guarda nada"
    (requisitar :post "/ganho" {:alimento "arroz" :quantidade -1 :data "2026-09-01"})
    => (contains {:status 400})
    (armazenamento/listar-lancamentos) => [])

  (fact "recusa perda antes do cadastro"
    (requisitar :post "/perda" {:atividade "correr" :duracao 30 :data "2026-09-02"})
    => (contains {:status 400 :body {:erro "Faça o cadastro antes de registrar uma atividade."}})))

(fact "rota inexistente devolve 404"
  (:status (app (mock/request :get "/nao-existe"))) => 404)
