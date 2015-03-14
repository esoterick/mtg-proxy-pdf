(ns mtg-proxy-pdf.server
  (:use ring.util.response)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer [GET defroutes]]
            [mtg-proxy-pdf.api :as api]
            [ring.middleware.json :as middleware]))

(defroutes app-routes
  (GET "/" [] (resource-response "index.html" {:root "public"}))
  (GET "/ac/:query" {{query :query} :params} (str (api/ac-card query)))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app (-> (handler/api app-routes)
             (middleware/wrap-json-body)
             (middleware/wrap-json-response)))
