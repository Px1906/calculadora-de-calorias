(ns calorias-api.banco-de-dados
  (:require
   [java-time :as jt]
   [calorias-api.apis :as apis]))

(defn filtrar-por-periodo [bd data-inicio-str data-fim-str]
  (let [data-inicio (jt/local-date data-inicio-str)
        data-fim    (jt/local-date data-fim-str)]
    (filter
      (fn [transacao]
        (let [data (jt/local-date (:data transacao))]
          (and (not (jt/before? data data-inicio))
               (not (jt/after? data data-fim)))))
      @bd)))

(defn saldo-por-periodo [bd data-inicio data-fim]
  (let [transacoes (filtrar-por-periodo bd data-inicio data-fim)]
    (reduce
     (fn [acc t]
       (case (:tipo t)
         "perda"   (- acc (:calorias t))   ;; Subtrai valor se for perda
         "ganho"   (+ acc (:calorias t))   ;; Soma valor se for ganho
         acc))
     0
     transacoes)))

(defn registrar-perda [lancamento bd peso]
  (swap! bd conj {:tipo "perda"
                  :atividade (:atividade lancamento)
                  :data (:data lancamento)
                  :calorias (:total_calories (first (apis/obter-calorias-queimadas (:atividade lancamento)
                                                   (:duracao lancamento)
                                                   peso)))
                  :duracao (:duracao lancamento)}))

(defn registrar-ganho [lancamento bd]
  (swap! bd conj {:tipo "ganho"
                  :comida (:alimento lancamento)
                  :data (:data lancamento)
                  :calorias (:calories (first (:items (apis/obter-calorias-consumidas (:quantidade lancamento)
                                                   (:alimento lancamento)))))
                  :quantidade (:quantidade lancamento)}))
