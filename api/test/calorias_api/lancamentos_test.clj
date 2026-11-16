(ns calorias-api.lancamentos-test
  (:require [midje.sweet :refer :all]
            [calorias-api.lancamentos :as l]))

(def lancamentos
  [{:tipo "ganho" :comida "arroz" :calorias 250 :data "2026-09-01"}
   {:tipo "perda" :atividade "correr" :calorias 100 :data "2026-09-10"}
   {:tipo "ganho" :comida "feijao" :calorias 150 :data "2026-09-20"}
   {:tipo "perda" :atividade "nadar" :calorias 400 :data "2026-10-05"}])

(facts "filtrar-por-periodo"
  (fact "inclui as datas de início e fim"
    (map :data (l/filtrar-por-periodo lancamentos "2026-09-01" "2026-09-20"))
    => ["2026-09-01" "2026-09-10" "2026-09-20"])

  (fact "exclui lançamentos fora do período"
    (map :data (l/filtrar-por-periodo lancamentos "2026-09-02" "2026-09-30"))
    => ["2026-09-10" "2026-09-20"])

  (fact "devolve vazio quando não há lançamentos no período"
    (l/filtrar-por-periodo lancamentos "2027-01-01" "2027-01-31") => empty?))

(facts "saldo-por-periodo"
  (fact "soma ganhos e subtrai perdas"
    (l/saldo-por-periodo lancamentos "2026-09-01" "2026-09-30") => 300)

  (fact "pode ficar negativo"
    (l/saldo-por-periodo lancamentos "2026-10-01" "2026-10-31") => -400)

  (fact "é zero sem lançamentos"
    (l/saldo-por-periodo [] "2026-09-01" "2026-09-30") => 0))

(facts "novo-ganho e nova-perda"
  (l/novo-ganho {:alimento "arroz" :quantidade 200 :data "2026-09-21"} 254.6)
  => {:tipo "ganho" :comida "arroz" :quantidade 200 :data "2026-09-21" :calorias 254.6}

  (l/nova-perda {:atividade "correr" :duracao 30 :data "2026-09-21"} 311)
  => {:tipo "perda" :atividade "correr" :duracao 30 :data "2026-09-21" :calorias 311})

(facts "ganho-valido?"
  (l/ganho-valido? {:alimento "arroz" :quantidade 200 :data "2026-09-21"}) => true
  (l/ganho-valido? {:alimento "" :quantidade 200 :data "2026-09-21"}) => false
  (l/ganho-valido? {:alimento "arroz" :quantidade 0 :data "2026-09-21"}) => false
  (l/ganho-valido? {:alimento "arroz" :quantidade "200" :data "2026-09-21"}) => false
  (l/ganho-valido? {:alimento "arroz" :quantidade 200 :data "21-09-2026"}) => false
  (l/ganho-valido? {:alimento "arroz" :quantidade 200}) => false)

(facts "perda-valida?"
  (l/perda-valida? {:atividade "correr" :duracao 30 :data "2026-09-21"}) => true
  (l/perda-valida? {:atividade "correr" :duracao -5 :data "2026-09-21"}) => false
  (l/perda-valida? {:duracao 30 :data "2026-09-21"}) => false)
