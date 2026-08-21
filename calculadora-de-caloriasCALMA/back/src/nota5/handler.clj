(ns nota5.handler
   (:require
    [cheshire.core :as json]
    [compojure.core :refer :all]
    [compojure.route :as route]
    [nota5.apis :as apis]
    [nota5.banco-de-dados :as bd]
    [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
    [ring.middleware.json :refer [wrap-json-body]]))

(def usuario (atom {}))
(def bdBancoDeDados (atom []))

(defn comoJson [data]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string data)})

(defn setUsuario [dados]
   (let [novo-usuario {:sexo (:sexo dados)
                      :idade (:idade dados)
                      :peso (:peso dados)}]
    (reset! usuario novo-usuario) novo-usuario))
 
;; (println "testePerda: " (:total_calories (first (apis/getPerdaCalorica "Correr" 10 55))))
;; (println "testeGanho: " (:calories (first (:items (apis/getGanhoCalorico 500 "tomate")))))

(defroutes app-routes
  (GET "/" [] "Hello Word")



  (GET "/cadastro" [] (comoJson @usuario))
  (GET "/extrato" [inicio fim]
    (comoJson (bd/filtrarPorPeriodo bdBancoDeDados inicio fim)))
  (GET "/saldo" [inicio fim] 
    (comoJson (bd/saldoPorPeriodo bdBancoDeDados inicio fim)))
  
  (GET "/testeGanho" [] (comoJson (:calories (first (:itens (apis/getGanhoCalorico 500 "tomate"))))))
  (GET "/testePerda" [] (comoJson (:total_calories (first (:calories (first (apis/getPerdaCalorica "Correr" 10 55)))))))
  ;; (GET "/testeTraducao" [] (comoJson (apis/getTraducao "Correr")))

  ;----------------------------------------------------------------------------------------------------------------------------------------

  (POST "/perda" {body :body} (comoJson (bd/registroPerda body bdBancoDeDados (:peso @usuario))))
  (POST "/ganho" {body :body} (comoJson (bd/registroGanho body bdBancoDeDados)))

  (POST "/cadastro" {body :body}
    (do
      (setUsuario body)
      (comoJson @usuario)))

  (route/not-found "Recurso não encontrado"))

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true :bigdecimals? true})
      (wrap-defaults api-defaults)))