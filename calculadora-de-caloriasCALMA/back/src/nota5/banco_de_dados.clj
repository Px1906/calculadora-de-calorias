(ns nota5.banco-de-dados
  (:require
   [java-time :as jt]
   [nota5.apis :as apis]))

(defn filtrarPorPeriodo [bd data-inicio-str data-fim-str]
  (let [data-inicio (jt/local-date data-inicio-str)
        data-fim    (jt/local-date data-fim-str)]
    (filter
      (fn [transacao]
        (let [data (jt/local-date (:data transacao))]
          (and (not (jt/before? data data-inicio))
               (not (jt/after? data data-fim)))))
      @bd)))

(defn saldoPorPeriodo [bd data-inicio data-fim]
  (let [transacoes (filtrarPorPeriodo bd data-inicio data-fim)]
    (reduce
     (fn [acc t]
       (case (:tipo t)
         "perda"   (- acc (:calorias t))   ;; Subtrai valor se for perda
         "ganho"   (+ acc (:calorias t))   ;; Soma valor se for ganho
         acc))
     0
     transacoes)))

(defn registroPerda [lancamento col peso]
  (swap! col conj {:tipo "perda"
                   :atividade (:atividade lancamento)
                   :data (:data lancamento)
                   :calorias (:total_calories (first (apis/getPerdaCalorica (:atividade lancamento)
                                                    (:duracao lancamento)
                                                    peso)))
                   :duracao (:duracao lancamento)}))

(defn registroGanho [lancamento col]
  (swap! col conj {:tipo "ganho"
                   :comida (:alimento lancamento)
                   :data (:data lancamento) 
                   :calorias (:calories (first (:items (apis/getGanhoCalorico (:quantidade lancamento) 
                                                    (:alimento lancamento)))))
                   :quantidade (:quantidade lancamento)}))

