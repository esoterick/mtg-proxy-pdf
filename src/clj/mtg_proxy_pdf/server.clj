(ns mtg-proxy-pdf.server
  (:use ring.util.response)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer [GET defroutes]]
            [ring.middleware.json :as middleware]))

(defroutes app-routes
  (GET "/" [] (resource-response "index.html" {:root "public"}))
  (GET "/widgets" [] (response [{:name "Mark of the Vampire"}]))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app (-> (handler/api app-routes)
             (middleware/wrap-json-body)
             (middleware/wrap-json-response)))
