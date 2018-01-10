#!/usr/bin/env bash

set -e

cd payment/libs
mvn install:install-file -DgroupId=com.alipay -DartifactId=alipay-sdk-java -Dversion=20180104135026 -Dpackaging=jar -Dfile=alipay-sdk-java20180104135026.jar