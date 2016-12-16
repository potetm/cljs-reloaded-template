#!/usr/bin/env bash

cd "$(dirname "$0")/.."

lein with-profile +cljs-dev repl
