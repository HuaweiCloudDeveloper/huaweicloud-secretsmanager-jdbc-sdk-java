#!/usr/bin/env bash

CURDIR=$(cd 'dirname $0';pwd)

echo "=======================build.sh========================="

mvn -Dmaven.test.skip=true clean install