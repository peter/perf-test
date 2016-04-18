(ns app.web
  (:require [immutant.web :as web]
            [clj-http.client :as client]
            [cheshire.core :as json]))

(defn index-handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Clojure Immutant API"})

(defn api-handler [request]
  (let [response (client/get "http://localhost:9000/articles.json" {:as :json})]
    {:status (:status response)
    :headers {"Content-Type" "application/json"}
    :body (json/generate-string (:body response))}))

(defn missing-handler [request]
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body "Missing"})

(def routes [
    {:methods #{:get} :path "/" :handler index-handler}
    {:methods #{:get} :path "/1" :handler api-handler}
  ])

(defn route-match? [request route]
  (and ((:methods route) (:request-method request))
       (= (:path route) (:uri request))))

(defn app [request]
  (let [route (first (filter (partial route-match? request) routes))
        handler (get route :handler missing-handler)]
    ;(println "app request " (:request-method request) (:uri request) (pr-str route))
    (handler request)))

(defn -main [& [port]]
  (let [port (Integer. (or port (System/getenv "PORT") 3000))]
    (web/run app {
      :port port
      :dispatch? false})))
