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

(defn editor [app-state]
  [:div.w-100.m0.h100.dib.h-100
   [:textarea.fl.w-50.h-100
    {:onChange update-text}
    (:text app-state)]
   [:div.fl.w-50.h-100.ph4
    [:div
     {:id "preview"
      :dangerouslySetInnerHTML {:__html (md->html (:text app-state))}}]]
   [:a.fixed.bottom-1.left-1.black.bg-animate.hover-bg-black.hover-white.items-center.pa3.ba.border-box
    {:onClick download-markdown}
    "Download Markdown"]])

(defn app []
  [editor @app-state])

(defn main! []
  (reagent/render-component [app]
                            (. js/document (getElementById "app"))))
