(ns calorias-api.armazenamento
  "Estado da aplicação, guardado em memória: reiniciar a API apaga tudo.")

(defonce ^:private usuario (atom {}))
(defonce ^:private lancamentos (atom []))

(defn obter-usuario []
  @usuario)

(defn salvar-usuario! [novo-usuario]
  (reset! usuario novo-usuario))

(defn listar-lancamentos []
  @lancamentos)

(defn adicionar-lancamento!
  "Guarda o lançamento e devolve a lista completa."
  [lancamento]
  (swap! lancamentos conj lancamento))

(defn limpar!
  "Apaga usuário e lançamentos (usado nos testes)."
  []
  (reset! usuario {})
  (reset! lancamentos []))
