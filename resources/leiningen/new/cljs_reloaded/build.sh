#!/usr/bin/env bash

cd "$(dirname "$0")/.."

java -cp "$(lein with-profile -dev classpath)" clojure.main \
 -i "build/prod_build.clj" \
 -e "(prod-build/build)"
