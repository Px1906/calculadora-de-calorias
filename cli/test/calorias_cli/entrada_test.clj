(ns calorias-cli.entrada-test
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is testing]]
            [calorias-cli.entrada :as t]))

(defn- ler
  "Roda `f` com as linhas dadas como entrada, sem imprimir as mensagens de erro."
  [f & linhas]
  (binding [*out* (java.io.StringWriter.)]
    (with-in-str (str/join "\n" linhas) (f))))

(deftest ler-inteiro-test
  (testing "aceita inteiros, inclusive negativos"
    (is (= 42 (ler t/ler-inteiro "42")))
    (is (= -3 (ler t/ler-inteiro "-3"))))
  (testing "repete até receber um inteiro"
    (is (= 7 (ler t/ler-inteiro "abc" "1.5" "7")))))

(deftest ler-idade-test
  (testing "aceita idades entre 8 e 125"
    (is (= 8 (ler t/ler-idade "8")))
    (is (= 125 (ler t/ler-idade "125"))))
  (testing "rejeita idades fora da faixa"
    (is (= 30 (ler t/ler-idade "7" "126" "-1" "30")))))

(deftest ler-sexo-test
  (is (= 1 (ler t/ler-sexo "1")))
  (is (= 2 (ler t/ler-sexo "3" "x" "2"))))

(deftest ler-opcao-test
  (testing "aceita valores de 1 até o máximo"
    (is (= 1 (ler #(t/ler-opcao 2) "1")))
    (is (= 2 (ler #(t/ler-opcao 2) "2"))))
  (testing "rejeita zero, acima do máximo e texto"
    (is (= 2 (ler #(t/ler-opcao 2) "0" "3" "a" "2"))))
  (testing "o menu principal vai de 1 a 4"
    (is (= 4 (ler t/ler-opcao-menu "5" "4")))))

(deftest ler-texto-sem-numeros-test
  (is (= "arroz integral" (ler t/ler-texto-sem-numeros "arroz integral")))
  (is (= "feijao" (ler t/ler-texto-sem-numeros "feijao 2" "feijao"))))

(deftest ler-data-test
  (testing "converte dd-MM-yyyy para data ISO"
    (is (= "2026-09-21" (str (ler t/ler-data "21-09-2026")))))
  (testing "repete com data inválida"
    (is (= "2026-02-28" (str (ler t/ler-data "2026-02-28" "32-01-2026" "28-02-2026"))))))

(deftest ler-intervalo-de-datas-test
  (is (= ["2026-09-01" "2026-09-30"]
         (ler t/ler-intervalo-de-datas "01-09-2026" "30-09-2026"))))
