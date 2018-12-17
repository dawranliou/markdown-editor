(ns markdown-editor.core
    (:require [reagent.core :as reagent :refer [atom]]
              [markdown.core :refer [md->html]]
              [alandipert.storage-atom :refer [local-storage]]
              [markdown-editor.icon :as icon]))

(enable-console-print!)

(defonce app-state (local-storage
                    (atom {:text "# Cool Markdown Editor\n\n## Features\n\n- Edit markdown in your browser\n- Save your markdown and continue later\n- Download markdown to your computer\n\n![alt screenshot](https://imgs.xkcd.com/comics/lisp_cycles.png)\n\n__TRY NOW!__"})
                    :app-state))

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
   [:textarea.fl.w-50.h-100.bg-black-10.br.b--black-10.pa3.pa4-l.f6.f5-m.f4-l.code
    {:onChange update-text
     :value (:text app-state)}]
   [:div.fl.w-50.h-100.ph4
    [:div
     {:id "preview"
      :dangerouslySetInnerHTML {:__html (md->html (:text app-state))}}]]
   [:a.fixed.bottom-1.left-1.black.bg-animate.hover-bg-black.items-center.pa3.ba.border-box
    {:onClick download-markdown}
    icon/download]])

(defn app []
  [editor @app-state])

(defn main! []
  (reagent/render-component [app]
                            (. js/document (getElementById "app"))))
