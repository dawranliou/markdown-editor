(ns markdown-editor.core
    (:require [reagent.core :as reagent :refer [atom]]
              [markdown.core :refer [md->html]]))

(enable-console-print!)

(defonce app-state (atom {:text "#Write some markdown here...\nYes, right here."}))

(defn update-text [event]
  (swap! app-state assoc :text event.target.value))

(defn download-markdown [event]
  (.preventDefault event)
  (let [data (js/Blob. [(:text @app-state)] {:type "text/plain;charset=utf-8;"})
        url (.createObjectURL (.-URL js/window) data)
        tmp-link (.createElement js/document "a")]
    (set! (.-href tmp-link) url)
    (set! (.-download tmp-link) "content.md")
    (.appendChild (.-body js/document) tmp-link)
    (.click tmp-link)
    (.removeChild (.-body js/document) tmp-link)))

(defn menu-bar []
  [:ul
   [:li]])

(defn editor [app-state]
  [:div
   {:id "editor"}
   [:textarea
    {:onChange update-text}
    (:text app-state)]
   [:div
    [:div
     {:id "preview"
      :dangerouslySetInnerHTML {:__html (md->html (:text app-state))}}]]
   [:button
    {:onClick download-markdown}
    "Download Markdown"]])

(defn app []
  [editor @app-state]
  )

(reagent/render-component [app]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
