(ns markdown-editor.action
  (:require [clojure.string :as string]
            [cemerick.url :as url]))

(defn download-markdown [app-state event]
  (.preventDefault event)
  (let [data (js/Blob. [(:text app-state)] {:type "text/plain;charset=utf-8;"})
        url (.createObjectURL (.-URL js/window) data)
        tmp-link (.createElement js/document "a")]
    (set! (.-href tmp-link) url)
    (set! (.-download tmp-link) "content.md")
    (.appendChild (.-body js/document) tmp-link)
    (.click tmp-link)
    (.removeChild (.-body js/document) tmp-link)))

(defn copy-text-to-clipboard [text]
  (let [el (js/document.createElement "textarea")]
    (set! (.-textContent el) text)
    (.appendChild (.-body js/document) el)
    (.select el)
    (.execCommand js/document "copy")
    (.removeChild (.-body js/document) el)))

(defn share [app-state event]
  (.preventDefault event)
  (let [url (-> js/window
                .-location
                url/url)
        b64-full-text (-> app-state
                          :text
                          string/trim
                          js/btoa)
        perm-link (-> url
                      (assoc :query {:t b64-full-text})
                      str)]
    (copy-text-to-clipboard perm-link)))
