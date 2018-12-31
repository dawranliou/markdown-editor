(ns markdown-editor.core
    (:require [reagent.core :as reagent :refer [atom]]
              [markdown.core :refer [md->html]]
              [alandipert.storage-atom :refer [local-storage]]
              [cemerick.url :as url]
              [markdown-editor.icon :as icon]
              [markdown-editor.action :as action]))

(defonce app-state (local-storage
                    (atom {:text "# Cool Markdown Editor\n\n## Features\n\n- Edit markdown in your browser\n- Save your markdown and continue later\n- Download markdown to your computer\n\n![alt screenshot](https://imgs.xkcd.com/comics/lisp_cycles.png)\n\n__TRY NOW!__"})
                    :app-state))

(defn update-text [event]
  (swap! app-state assoc :text event.target.value))

(defn app [app-state]
  [:div.w-100.m0.h100.dib.h-100
   [:textarea.fl.w-50.h-100.bg-black-10.br.b--black-10.pa3.pa4-l.f6.f5-m.code
    {:onChange update-text
     :value (:text @app-state)}]
   [:div.fl.w-50.h-100.ph4
    [:div
     {:id "preview"
      :dangerouslySetInnerHTML {:__html (md->html (:text @app-state))}}]]
   [:div.flex.items-center.fixed.bottom-2.left-2
    [:a.black.bg-animate.hover-bg-black.items-center.pa3.ba.border-box.inline-flex.items-center.mr2
     {:onClick (partial action/download-markdown @app-state)}
     icon/download]
    [:a.black.bg-animate.hover-bg-black.items-center.pa3.ba.border-box.inline-flex.items-center
     {:onClick (partial action/share @app-state)}
     icon/share]]
   ])

(defn initialize-app-state! [app-state]
  (when-let [b64-text (-> js/window
                          .-location
                          url/url
                          :query
                          (get "t"))]
    (let [text (js/atob b64-text)]
      (swap! app-state assoc :text text))))

(defn main! []
  (enable-console-print!)
  (initialize-app-state! app-state)
  (reagent/render-component [app app-state]
                            (. js/document (getElementById "app"))))
