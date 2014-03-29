(ns mtg-proxy-pdf.core-test
  (:require [clojure.test :refer :all]
            [mtg-proxy-pdf.core :refer :all]
            [mtg-proxy-pdf.decklist-parser :as decklist-parser]
            [clojure.java.io :as io])
  (:use midje.sweet))

(def test-card-name "Academy Rector")
(def test-query-url "http://magiccards.info/query?q=Academy%20Rector&v=card&s=cname")
(def test-image-src "http://magiccards.info/scans/en/ud/1.jpg")

;; (def test-card-name "Archangel of Thune")
;; (def test-query-url "http://magiccards.info/query?q=Archangel%20of%20Thune&v=card&s=cname")
;; (def test-image-src "http://magiccards.info/scans/en/m14/5.jpg")

;; (def test-card-name "Avacyn's Pilgrim")
;; (def test-query-url "http://magiccards.info/query?q=Avacyn%27s%20Pilgrim&v=card&s=cname")
;; (def test-image-src "http://magiccards.info/scans/en/isd/170.jpg")

(def test-card-record { :name test-card-name, :quantity 1 })
(def test-decklist [test-card-record
                    { :name "Birthing Pod",        :quantity 1 }
                    { :name "Kitchen Finks",       :quantity 2 }
                    { :name "Fall of the Hammer",  :quantity 3 }
                    { :name "Lich's Mirror",       :quantity 1 }
                    { :name "Mana Flair",          :quantity 4 }])

(def test-image-src-list `(~test-image-src
                           "http://magiccards.info/scans/en/nph/104.jpg"
                           "http://magiccards.info/scans/en/mma/190.jpg"
                           "http://magiccards.info/scans/en/mma/190.jpg"
                           "http://magiccards.info/scans/en/bng/93.jpg"
                           "http://magiccards.info/scans/en/bng/93.jpg"
                           "http://magiccards.info/scans/en/bng/93.jpg"
                           "http://magiccards.info/scans/en/ala/210.jpg"
                           "http://magiccards.info/scans/en/uh/81.jpg"
                           "http://magiccards.info/scans/en/uh/81.jpg"
                           "http://magiccards.info/scans/en/uh/81.jpg"
                           "http://magiccards.info/scans/en/uh/81.jpg"))

(def test-decklist-images (decklist->images-urls test-decklist))

(def test-large-decklist [{ :name "Academy Rector",      :quantity 1 }
                          { :name "Angelic Renewal",     :quantity 1 }
                          { :name "Archangel Of Thune",  :quantity 1 }
                          { :name "Ashen Rider",         :quantity 1 }
                          { :name "Avacyn's Pilgrim",    :quantity 1 }
                          { :name "Barren Moor",         :quantity 1 }
                          { :name "Bayou",               :quantity 1 }
                          { :name "Birds of Paradise",   :quantity 1 }
                          { :name "Birthing Pod",        :quantity 1 }])

(def test-query-urls '("http://magiccards.info/query?q=Academy%20Rector&v=card&s=cname" "http://magiccards.info/query?q=Angelic%20Renewal&v=card&s=cname" "http://magiccards.info/query?q=Archangel%20Of%20Thune&v=card&s=cname" "http://magiccards.info/query?q=Ashen%20Rider&v=card&s=cname" "http://magiccards.info/query?q=Avacyn%27s%20Pilgrim&v=card&s=cname" "http://magiccards.info/query?q=Barren%20Moor&v=card&s=cname" "http://magiccards.info/query?q=Bayou&v=card&s=cname" "http://magiccards.info/query?q=Birds%20Of%20Paradise&v=card&s=cname" "http://magiccards.info/query?q=Birthing%20Pod&v=card&s=cname"))
(def test-card-names '("Academy Rector" "Angelic Renewal" "Archangel Of Thune" "Ashen Rider" "Avacyn's Pilgrim" "Barren Moor" "Bayou" "Birds Of Paradise" "Birthing Pod"))
(def test-images '("http://magiccards.info/scans/en/ud/1.jpg" "http://magiccards.info/scans/en/wl/120.jpg" "http://magiccards.info/scans/en/m14/5.jpg" "http://magiccards.info/scans/en/ths/187.jpg" "http://magiccards.info/scans/en/isd/170.jpg" "http://magiccards.info/scans/en/c13/277.jpg" "http://magiccards.info/scans/en/rv/283.jpg" "http://magiccards.info/scans/en/m12/165.jpg" "http://magiccards.info/scans/en/nph/104.jpg"))

(def test-out-file-name "test/target/test")
(def test-out-file (io/as-file test-out-file-name))
(def test-in-file-name "test/templates/decklist.txt")
(def test-in-file (io/as-file test-in-file-name))

(def test-out-file-name-pdf (apply str test-out-file-name ".pdf"))
(def test-out-file-pdf (io/as-file test-out-file-name-pdf))

(def test-out-file-name-html (apply str test-out-file-name ".html"))
(def test-out-file-html (io/as-file test-out-file-name-html))

(deftest build-query-url-test
  (testing "it builds a url to magiccards.info"
    (is (= test-query-url
           (build-query-url test-card-record)))))

(deftest image-src-test
  (testing "it returns the url to the card image"
    (is (= test-image-src
           (fetch-image-src test-card-record)))))

(deftest image-srcs-test
  (testing "it returns the urls of multiple cards"
    (is (= test-images
           (map fetch-image-src test-large-decklist)))))

;; (deftest cache-uri-test
;;   (testing "it caches a retrieved uri onto disk"
;;     (let [expected-file (io/as-file "test/templates/1.jpg")]
;;       (is (.exists expected-file))
;;       (is (= expected-file (cache-uri "http://magiccards.info/scans/en/ud/1.jpg"))))))

(deftest decklist->images-urls-test
  (testing "it converts a decklist to a list of image urls"
    (is (= test-image-src-list
           test-decklist-images))))

(deftest repeat-card-per-quantity-test
  (testing "it returns a card image src once for each quantity"
    (is (= '("http://magiccards.info/scans/en/mma/190.jpg" "http://magiccards.info/scans/en/mma/190.jpg")
           (cached-image-src { :name "Kitchen Finks", :quantity 2 })))))

(deftest cached-image-src-test
  (testing "it caches list of image sources"
    (is (= (list test-image-src)
           (cached-image-src test-card-record)))))

(deftest read-cache-test
  (testing "it reads from cache"
    (is (= test-image-src
           (get-cache test-card-record)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; SIDE-EFFECTS TESTS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftest decklist->images-urls-from-parsed-file-test
  (testing "it converts a decklist to a list of image urls from a parsed file"
    (is (= test-image-src-list
           (decklist->images-urls (decklist-parser/parse-text-file test-in-file-name))))))

(deftest images->html-test
  (testing "it writes the card images to html"
    (io/delete-file test-out-file-name-html true) ;; true to ignore error if file doesn't exist
    (images->html test-decklist-images test-out-file-name-html)
    (is (.exists test-out-file-html))))

(deftest images->pdf-test
  (testing "it writes the card images to a pdf"
    (io/delete-file test-out-file-name-pdf true) ;; true to ignore error if file doesn't exist
    (images->pdf test-decklist-images test-out-file-name-pdf)
    (is (.exists test-out-file-pdf))))

(deftest generate-test
  (testing "takes a decklist file and creates an html page with the images"
    (is (.exists test-in-file)) ;; ensure our input file exists
    (io/delete-file test-out-file-name true) ;; delete output file if it exists
    (generate test-in-file-name test-out-file-name)
    (is (.exists test-out-file))))
