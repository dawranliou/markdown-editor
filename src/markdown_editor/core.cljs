(ns markdown-editor.core
    (:require [reagent.core :as reagent :refer [atom]]
              [alandipert.storage-atom :refer [local-storage]]
              [cemerick.url :as url]
              [markdown-editor.icon :as icon]
              [markdown-editor.action :as action]))

(def default-welcome-text
  (clojure.string/join "\n" ["# Cool Markdown Editor"
                             ""
                             "## Features"
                             ""
                             "- Edit markdown in your browser"
                             "- Save your markdown *as pdf!*"
                             "- Download markdown to your computer"
                             "- Share permanent link"
                             ""
                             "__TRY NOW!__"]))

(def app-state (local-storage
                (atom {:text default-welcome-text})
                :app-state))

(defn editor-component [app-state]
  [:textarea.fl.w-100.w-50-l.vh-25.vh-100-l.bg-black-10.br.b--black-10.ph3.ph4-l.pt5.pb2.f6.f5-l.code
   {:value    (:text @app-state)
    :onChange #(swap! app-state assoc :text (-> % .-target .-value))}])

(defn markdown-render [app-state]
  [:div.fl.w-100.w-50-l.ph4.vh-75.vh-100-l.overflow-y-scroll
   [:div
    {:id "preview"
     :dangerouslySetInnerHTML {:__html (js/marked (:text @app-state))}}]])

(defn highlight-code! [html-node]
  (doseq [node (-> html-node
                 (.querySelectorAll "pre code")
                 array-seq)]
    (.highlightBlock js/hljs node)))

(defn preview-component [app-state]
  (reagent/create-class
   {:reagent-render      markdown-render
    :component-did-mount
    (fn [this] (-> this reagent/dom-node highlight-code!))
    :component-did-update
    (fn [this] (-> this reagent/dom-node highlight-code!))}))

(defn tools-component [app-state]
  [:div.flex.items-center.fixed.bottom-2.right-2
   [:a.black.bg-animate.hover-bg-black.items-center.pa3.ba.border-box.inline-flex.items-center.mr2
    {:onClick (partial action/download-pdf @app-state)}
    icon/download]
   [:a.black.bg-animate.hover-bg-black.items-center.pa3.ba.border-box.inline-flex.items-center
    {:onClick (partial action/share @app-state)}
    icon/share]])

(defn app [app-state]
  [:div.w-100.m0.h100.dib.h-100
   [editor-component app-state]
   [preview-component app-state]
   [tools-component app-state]])

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
