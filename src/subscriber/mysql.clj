(ns subscriber.mysql
  (:require [clojure.java.jdbc :as j]))

(def db {:subprotocol "mysql"
               :subname "//127.0.0.1:3306/clojure_test"
               :user "clojure_test"
               :password "clojure_test"})

;(j/insert! db :fruit
;  {:name "Apple" :appearance "rosy" :cost 24}
;  {:name "Orange" :appearance "round" :cost 49})
;
(defn get-by-id
  [id]
  (j/query db
           ["select * from fruit where id = ?" id]))

; create database clojure_test;
; create user 'clojure_test'@localhost identified by 'clojure_test';
; GRANT ALL PRIVILEGES ON *.* TO 'clojure_test'@'localhost' WITH GRANT OPTION;
; CREATE TABLE fruit (
;	ID INT UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
;	name VARCHAR(40) NOT NULL,
;	appearance VARCHAR(40) NOT NULL,
;	cost INT NOT NULL,
;	PRIMARY KEY (ID)
;
;                    )
