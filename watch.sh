#!/bin/bash

function build() {
    mvn install -DskipTests
}

build
inotifywait -m -e close_write -r src |
while read -r filename event; do
    build
done
