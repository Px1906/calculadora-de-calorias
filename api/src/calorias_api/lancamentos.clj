(ns calorias-api.lancamentos
  "Regras dos lançamentos calóricos. Funções puras: sem HTTP e sem estado."
  (:require [clojure.string :as str]
            [java-time :as jt]))

;; Validação dos dados recebidos

(defn- texto? [x]
  (and (string? x) (not (str/blank? x))))

(defn- positivo? [x]
  (and (number? x) (pos? x)))

(defn data-valida?
  "Data no formato ISO (yyyy-MM-dd)."
  [x]
  (and (string? x)
       (try (jt/local-date x) true
            (catch Exception _ false))))

(defn ganho-valido? [{:keys [alimento quantidade data]}]
  (boolean (and (texto? alimento) (positivo? quantidade) (data-valida? data))))

(defn perda-valida? [{:keys [atividade duracao data]}]
  (boolean (and (texto? atividade) (positivo? duracao) (data-valida? data))))

;; Construção

(defn novo-ganho [{:keys [alimento quantidade data]} calorias]
  {:tipo "ganho"
   :comida alimento
   :quantidade quantidade
   :data data
   :calorias calorias})

(defn nova-perda [{:keys [atividade duracao data]} calorias]
  {:tipo "perda"
   :atividade atividade
   :duracao duracao
   :data data
   :calorias calorias})

;; Consultas

(defn filtrar-por-periodo
  "Lançamentos com data entre `inicio` e `fim` (strings ISO, inclusive)."
  [lancamentos inicio fim]
  (let [data-inicio (jt/local-date inicio)
        data-fim    (jt/local-date fim)]
    (filter (fn [lancamento]
              (let [data (jt/local-date (:data lancamento))]
                (and (not (jt/before? data data-inicio))
                     (not (jt/after? data data-fim)))))
            lancamentos)))

(defn saldo
  "Soma das calorias de ganho menos as de perda."
  [lancamentos]
  (reduce (fn [acc {:keys [tipo calorias]}]
            (case tipo
              "ganho" (+ acc calorias)
              "perda" (- acc calorias)
              acc))
          0
          lancamentos))

(defn saldo-por-periodo [lancamentos inicio fim]
  (saldo (filtrar-por-periodo lancamentos inicio fim)))
