(ns calorias-api.apis
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [clojure.string]))

(defn traduzir-texto [texto]
  (let [chave-traducao (System/getenv "RAPIDAPI_TRANSLATE_KEY")
        url-traducao "https://deep-translate1.p.rapidapi.com/language/translate/v2"
        host-traducao "deep-translate1.p.rapidapi.com"
        response (http/post url-traducao
                            {:headers {:x-rapidapi-key chave-traducao
                                       :x-rapidapi-host host-traducao}
                             :content-type :json
                             :form-params {:q texto
                                           :source "pt"
                                           :target "en"}})
        body (json/parse-string (:body response) true)]
    (get-in body [:data :translations :translatedText])))

(defn obter-calorias-consumidas [quantidade-gramas alimento]
  (let [chave-calorie-ninjas (System/getenv "CALORIENINJAS_API_KEY")
        url-nutricao "https://api.calorieninjas.com/v1/nutrition?query="
        query (str quantidade-gramas "g " (first (traduzir-texto alimento)))
        response (http/get (str url-nutricao query)
                           {:headers {"X-Api-Key" chave-calorie-ninjas}})
        body (json/parse-string (:body response) true)]
    body))

(defn obter-calorias-queimadas [atividade duracao peso]
  (let [chave-calorias-queimadas (System/getenv "RAPIDAPI_CALORIES_BURNED_KEY")
        url-calorias-queimadas "https://calories-burned-by-api-ninjas.p.rapidapi.com/v1/caloriesburned"
        host-calorias-queimadas "calories-burned-by-api-ninjas.p.rapidapi.com"
        response (http/get url-calorias-queimadas
                           {:headers {:x-rapidapi-key chave-calorias-queimadas
                                      :x-rapidapi-host host-calorias-queimadas}
                            :query-params {:activity (traduzir-texto atividade)
                                          :duration duracao
                                          :weight peso}})
        body (json/parse-string (:body response) true)]
    body))
