#!/usr/bin/env bash

cd "$(dirname "$0")/.."

java -cp "$(lein with-profile cljs classpath)" clojure.main \
 -i "src/build-cljs/prod_build.clj" \
 -e "(prod-build/build)"
