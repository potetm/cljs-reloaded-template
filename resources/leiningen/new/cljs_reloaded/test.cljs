(ns {{main-ns}}-test
  (:require [cljs.test :refer-macros [deftest
                                      testing
                                      is]]
            [{{main-ns}} :as core]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))
