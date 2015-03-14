(ns mtg-proxy-pdf.api
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.core.cache :as cache]))

;; Define the api endpoints
;; TODO: move these in to a config file
(def api-base "https://api.deckbrew.com")
(def api-card (str/join [api-base "/mtg/cards/"]))
(def api-ac-url (str/join [api-base "/mtg/cards/typeahead?q="]))

(def card-cache (cache/ttl-cache-factory {}))

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
    (if (cache/has? card-cache card)
      (cache/hit card-cache card)
      (cache/miss card-cache card (fetch-url (str/join [api-card card-id]))))))
