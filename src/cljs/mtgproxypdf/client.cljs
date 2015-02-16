(ns mtgproxypdf.client
  (:require [reagent.core :as reagent :refer [atom]]))

(defn header []
  [:div
   [:div [:h2 "Search"]]])

(reagent/render-component [header] (.getElementById js/document "header"))

(defn text-input [label]
  [:div.row
   [:div.large-12.columns
    [:label label
     [:input {:type "text"}]]]])

(defn app []
  [:div
   [:form
    [text-input "search"]
    ]])

(reagent/render-component [app] (.getElementById js/document "app"))
