(ns nota5.apis
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [clojure.string]))

(defn getTraducao [texto]
  (let [chaveTraducao "85b6ee325cmsh20882dff5f8b7e3p1e76fajsn7a32bbceec18"
        urlTraducao "https://deep-translate1.p.rapidapi.com/language/translate/v2"
        hostTraducao "deep-translate1.p.rapidapi.com"
        response (http/post urlTraducao
                            {:headers {:x-rapidapi-key chaveTraducao
                                       :x-rapidapi-host hostTraducao}
                             :content-type :json
                             :form-params {:q texto
                                           :source "pt"
                                           :target "en"}})
        body (json/parse-string (:body response) true)]
    (get-in body [:data :translations :translatedText])))

(defn getGanhoCalorico [quantidadeGramas alimento]
  (let [chaveNinja "lp9iw0JgR04S6JfcDlzcaA==hOXtyrGu4UkqlYBd"
        urlNinja "https://api.calorieninjas.com/v1/nutrition?query="
        query (str quantidadeGramas "g " (first (getTraducao alimento)))
        response (http/get (str urlNinja query)
                           {:headers {"X-Api-Key" chaveNinja}})
        body (json/parse-string (:body response) true)]
    body))

(defn getPerdaCalorica [atividadeQueima tempoQueima peso]
  (let [chaveQueima "85b6ee325cmsh20882dff5f8b7e3p1e76fajsn7a32bbceec18"
        urlQueima "https://calories-burned-by-api-ninjas.p.rapidapi.com/v1/caloriesburned"
        hostQueima "calories-burned-by-api-ninjas.p.rapidapi.com"
        response (http/get urlQueima
                           {:headers {:x-rapidapi-key chaveQueima
                                      :x-rapidapi-host hostQueima}
                            :query-params {:activity (getTraducao atividadeQueima)
                                          :duration tempoQueima
                                          :weight peso}})
        body (json/parse-string (:body response) true)]
    body))


