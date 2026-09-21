(ns calorias-api.handler
   (:require
    [cheshire.core :as json]
    [compojure.core :refer :all]
    [compojure.route :as route]
    [calorias-api.apis :as apis]
    [calorias-api.banco-de-dados :as bd]
    [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
    [ring.middleware.json :refer [wrap-json-body]]))

(def usuario (atom {}))
(def banco-de-dados (atom []))

(defn como-json [data]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string data)})

(defn definir-usuario [dados]
   (let [novo-usuario {:sexo (:sexo dados)
                      :idade (:idade dados)
                      :peso (:peso dados)}]
    (reset! usuario novo-usuario) novo-usuario))

;; (println "testePerda: " (:total_calories (first (apis/obter-calorias-queimadas "Correr" 10 55))))
;; (println "testeGanho: " (:calories (first (:items (apis/obter-calorias-consumidas 500 "tomate")))))

(defroutes app-routes
  (GET "/" [] "Hello Word")



  (GET "/cadastro" [] (como-json @usuario))
  (GET "/extrato" [inicio fim]
    (como-json (bd/filtrar-por-periodo banco-de-dados inicio fim)))
  (GET "/saldo" [inicio fim]
    (como-json (bd/saldo-por-periodo banco-de-dados inicio fim)))

  (GET "/testeGanho" [] (como-json (:calories (first (:itens (apis/obter-calorias-consumidas 500 "tomate"))))))
  (GET "/testePerda" [] (como-json (:total_calories (first (:calories (first (apis/obter-calorias-queimadas "Correr" 10 55)))))))
  ;; (GET "/testeTraducao" [] (como-json (apis/traduzir-texto "Correr")))

  ;----------------------------------------------------------------------------------------------------------------------------------------

  (POST "/perda" {body :body} (como-json (bd/registrar-perda body banco-de-dados (:peso @usuario))))
  (POST "/ganho" {body :body} (como-json (bd/registrar-ganho body banco-de-dados)))

  (POST "/cadastro" {body :body}
    (do
      (definir-usuario body)
      (como-json @usuario)))

  (route/not-found "Recurso não encontrado"))

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true :bigdecimals? true})
      (wrap-defaults api-defaults)))
