(ns app.web
  (:require [aleph.http :as http]
            [manifold.deferred :as d]
            [byte-streams :as bs]
            [cheshire.core :as json]))

(defn index-handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Clojure Aleph API"})

(defn api-handler [request]
  (let [data (json/parse-string (-> @(http/get "http://localhost:9000/articles.json") :body bs/to-string))]
    {:status 200
    :headers {"Content-Type" "application/json"}
    :body (json/generate-string data)}))

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
    (handler request)))

(defn -main [& [port]]
  (let [port (Integer. (or port (System/getenv "PORT") 3000))]
    (println "Starting server on port " port)
    (http/start-server app {:port port :executor :none}))
    (println "Server started..."))
