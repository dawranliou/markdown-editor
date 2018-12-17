(ns markdown-editor.action)

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
