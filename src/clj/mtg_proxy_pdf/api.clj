(ns mtg-proxy-pdf.api
  (:require [cheshire.core :refer :all]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.core.cache :refer :all]
            [org.httpkit.client :as http]
            [monger.cache :refer :all]
            [monger.core :as monger]
            [monger.json]))

;; Define the api endpoints
;; TODO: move these in to a config file
(def api-base "https://api.deckbrew.com")
(def api-card (str/join [api-base "/mtg/cards/"]))
(def api-ac-url (str/join [api-base "/mtg/cards/typeahead?q="]))
(def api-all-cards (str/join [api-base "/mtg/cards"]))

(def conn (monger/connect))
(def db (monger/get-db conn "mtg"))
(def card-cache (basic-monger-cache-factory db "cache"))

;; (def card-cache (cache/ttl-cache-factory {} :ttl 3600))

(def options {:timeout 200})

(defn fetch-url-two
  [url]
  (let [cards (http/get url)]
    (println "headers: " ((:headers @cards) :link))))

(defn fetch-url
  "Creates HTTP request with address and returns a json object of returned data"
  [address]
  (with-open [stream (.openStream (java.net.URL. address))]
    (let  [buf (java.io.BufferedReader.
               (java.io.InputStreamReader. stream))]
      (json/read-str
       (apply str (line-seq buf))))))

(defn ac-card
  "Auto-complete for cards, This hits the api endpoint need to add a caching layer inbetween"
  [query]
  (let [url (str/join [api-ac-url query])]
    (fetch-url url)))

(defn get-card
  "Given a card id return card from cache if available, if not fetch card from api"
  [card-id]
  (let [card (symbol card-id)]
    (if (has? card-cache card)
      (hit card-cache card)
      (miss card-cache card (fetch-url (str/join [api-card card-id]))))))

(defn cache-card
  [card]
  (-> card-cache (assoc (symbol (card "id")) card)))

(defn cache-card-two
  [card]
  (println (generate-string card))
  (if (has? card-cache (card "id"))
      (hit card-cache (card "id"))
      (miss card-cache (card "id") card)))

(defn refresh-cache
  "Pull all the cards from the api and refresh our cache."
  []
  (let [cards (fetch-url api-all-cards)]
    (map cache-card-two cards)))

(defn view-cache
  "View data in he cache given an id"
  [id]
  (let [card (symbol id)]
    (if (has? card-cache card)
      (hit card-cache card)
      "No Object in Cache")))
