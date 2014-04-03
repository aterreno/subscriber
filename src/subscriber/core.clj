(ns subscriber.core
  (:gen-class)
  (:require [langohr.core      :as rmq]
            [langohr.channel   :as lch]
            [langohr.queue     :as lq]
            [langohr.consumers :as lc]
            [langohr.basic     :as lb]
            [subscriber.mysql  :as mysql]
            [cheshire.core :refer :all]
            [monger.core :refer [connect! connect set-db! get-db]]
            [monger.collection :refer [insert insert-batch]]))

(connect!)
(set-db! (monger.core/get-db "monger-test"))

(def ^{:const true}
  default-exchange-name "")

(defn message-handler
  [ch {:keys [content-type delivery-tag type] :as meta} ^bytes payload]
  (let [message  (String. payload "UTF-8")]
    (println (format "Got a message %s" message))
    (insert-batch "fruits" (mysql/get-by-id (get (parse-string message) "id")))
    (println (format "Saved in mongo OK, waiting for more"))))

(defn -main
  [& args]
  (let [conn  (rmq/connect)
        ch    (lch/open conn)
        qname "hello-world"]
    (println (format "[main] Connected. Channel id: %d" (.getChannelNumber ch)))
    (lq/declare ch qname :exclusive false :auto-delete true)

    (lc/blocking-subscribe ch qname message-handler :auto-ack true)

    (rmq/close ch)
    (rmq/close conn)))
