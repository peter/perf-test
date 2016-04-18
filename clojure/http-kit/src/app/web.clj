(ns app.web
  (:require [org.httpkit.server :as server]
            [org.httpkit.client :as client]
            [cheshire.core :as json]))

(defn index-handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Clojure http-kit API"})

(defn api-handler [request]
  (let [response (client/get "http://localhost:9000/articles.json")
        data (json/parse-string (:body @response))]
    {:status (:status response)
    :headers {"Content-Type" "application/json"}
    :body (json/generate-string data)}))

(defn missing-handler [request]
  {:status 404
   :headers {"Content-Type" "text/html"}
   :body "Not Found"})

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

(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@server :timeout 100)
    (reset! server nil)))

(defn -main [& args]
  ;; The #' is useful when you want to hot-reload code
  ;; You may want to take a look: https://github.com/clojure/tools.namespace
  ;; and http://http-kit.org/migration.html#reload
  (reset! server (server/run-server #'app {:port 3000})))
