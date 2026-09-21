(ns calcapp.tratamentos
  (:require [java-time :as jt]))

(defn ler-inteiro []
  (let [input (read-line)]
    (if (re-matches #"-?\d+" input)
      (Integer/parseInt input)
      (do
        (println "Por favor digite um numero inteiro")
        (recur)))))

(defn ler-idade []
  (let [input (read-line)]
    (if (and (re-matches #"\d+" input)
             (let [idade (Integer/parseInt input)]
               (and (>= idade 8) (<= idade 125))))
      (Integer/parseInt input)
      (do
        (println "Por favor, digite uma idade entre 8 e 125 anos.")
        (recur)))))

(defn ler-sexo []
    (let [input (read-line)]
      (if (or (= input "1") (= input "2"))
        (Integer/parseInt input)
        (do
          (println "Por favor, digite 1 ou 2")
          (recur)))))

(defn ler-opcao-menu []
    (let [input (read-line)]
      (if (or (= input "1") (= input "2") (= input "3") (= input "4"))
        (Integer/parseInt input)
        (do
          (println "Por favor, digite um numero entre 1 e 4")
          (recur)))))

(defn ler-texto-sem-numeros []
  (let [input (read-line)]
    (if (re-matches #"^[^\d]+$" input)
      input
      (do
        (println "Por favor, digite apenas letras (sem numeros).")
        (recur)))))

(defn ler-data []
  (let [input (read-line)]
    (if (try
          (jt/local-date "dd-MM-yyyy" input)
          true
          (catch Exception _ false))
      (jt/local-date "dd-MM-yyyy" input)
      (do
        (println "Data invalida! Use o formato dd-MM-yyyy.")
        (recur)))))

(defn ler-intervalo-de-datas []
  (println "Informe a data inicial:")
  (let [data-inicio (ler-data)]
    (println "Informe a data final:")
    (let [data-fim (ler-data)]
      [(str data-inicio) (str data-fim)])))
