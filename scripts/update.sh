#!/bin/bash

DIRECTORY_TO_OBSERVE="/home/crochefolle/apps/jscgdrive/drive"
function block_for_change {
  inotifywait --recursive -qq \
    --event modify,move,create,delete \
    $DIRECTORY_TO_OBSERVE
}
BUILD_SCRIPT=/home/crochefolle/apps/jscgdrive/bin/jscdrive
function build {
  bash $BUILD_SCRIPT
}
build
while block_for_change; do
  build
done
