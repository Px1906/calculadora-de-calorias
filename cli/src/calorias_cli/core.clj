(ns calorias-cli.core
  "Menus do cliente de linha de comando."
  (:gen-class)
  (:require [calorias-cli.cliente-api :as api]
            [calorias-cli.entrada :as entrada]))

(defn- mostrar-resposta [enviar! registro]
  (try
    (let [resposta (enviar! registro)]
      (println "Resposta do servidor:" (:status resposta))
      (println "Corpo da resposta:" (:body resposta)))
    (catch Exception e
      (println "Erro ao enviar dados para o servidor:" (.getMessage e)))))

(defn novo-cadastro []
  (println "\nNOVO CADASTRO:")
  (println "Digite o numero com base no seu sexo: \n1. Masculino \n2. Feminino")
  (let [sexo (entrada/ler-sexo)]
    (println "Digite sua idade:")
    (let [idade (entrada/ler-idade)]
      (println "Digite seu peso:")
      (let [peso (entrada/ler-inteiro)
            cadastro {:sexo sexo :idade idade :peso peso}]
        (println "Dados cadastrados com sucesso")
        (api/cadastrar! cadastro)
        cadastro))))

(defn consultar-cadastro []
  (let [{:keys [sexo idade peso]} (api/obter-cadastro)]
    (if (some? idade)
      (do (println "\nSexo:" (if (= sexo 1) "Masculino" "Feminino"))
          (println "Idade:" idade "anos")
          (println "Peso:" peso "Kg"))
      (println "\n\nFaca um cadastro antes!"))))

(defn menu-cadastro []
  (println "\nOPCOES DE CADASTRO:\n1. Novo cadastro\n2. Consulta")
  (case (entrada/ler-opcao 2)
    1 (novo-cadastro)
    2 (consultar-cadastro)))

(defn lancar-ganho-calorico []
  (println "\nREGISTRO DE GANHO CALORICO (ALIMENTO):")
  (println "Digite o nome do alimento:")
  (let [alimento (entrada/ler-texto-sem-numeros)]
    (println "Digite a quantidade (em gramas):")
    (let [quantidade (entrada/ler-inteiro)]
      (println "Digite a data (formato: dd-mm-yyyy):")
      (let [registro {:alimento alimento
                      :quantidade quantidade
                      :data (str (entrada/ler-data))}]
        (println "\nEnviando dados do alimento para o servidor...")
        (mostrar-resposta api/registrar-ganho! registro)
        registro))))

(defn lancar-perda-calorica []
  (println "\nREGISTRO DE PERDA CALORICA (ATIVIDADE FISICA):")
  (println "Digite o nome da atividade fisica:")
  (let [atividade (entrada/ler-texto-sem-numeros)]
    (println "Digite a duracao (em minutos):")
    (let [duracao (entrada/ler-inteiro)]
      (println "Digite a data (formato: dd-mm-yyyy):")
      (let [registro {:atividade atividade
                      :duracao duracao
                      :data (str (entrada/ler-data))}]
        (println "\nEnviando dados da atividade para o servidor...")
        (mostrar-resposta api/registrar-perda! registro)
        registro))))

(defn consultas []
  (let [[inicio fim] (entrada/ler-intervalo-de-datas)
        lancamentos (api/extrato inicio fim)
        saldo (api/saldo inicio fim)]
    (println "\nEXTRATO COMPLETO NO PERIODO:")
    (println (str "De " inicio " até " fim))

    (println "\nGANHOS CALORICOS:")
    (run! #(println "Alimento:" (:comida %) " Quantidade:" (:quantidade %) "g Calorias:" (:calorias %) " Data:" (:data %))
          (filter #(= (:tipo %) "ganho") lancamentos))

    (println "\nPERDAS CALORICAS")
    (run! #(println "Atividade:" (:atividade %) " Duracao:" (:duracao %) "min Calorias:" (:calorias %) " Data:" (:data %))
          (filter #(= (:tipo %) "perda") lancamentos))

    (println "\nSALDO FINAL")
    (println "Saldo de calorias por período:" saldo "calorias\n")))

(defn menu []
  (println "\nMENU:\n1. Cadastro\n2. Ganho calorico\n3. Perda calorica\n4. Consultas")
  (case (entrada/ler-opcao-menu)
    1 (menu-cadastro)
    2 (lancar-ganho-calorico)
    3 (lancar-perda-calorica)
    4 (consultas))
  (recur))

(defn -main
  "Ponto de entrada do programa."
  [& _args]
  (menu))
