#!/bin/sh

java -cp build-example:`echo ../lib/*.jar ../dist/*.jar | tr ' ' ':'` \
  com.idot.twitter.client.TwitterClientDriver "$@"
