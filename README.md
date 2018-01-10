# xdu-spm-parknshop

[![Build Status](https://www.travis-ci.org/xdu-spm-0x5f3759df/xdu-spm-parknshop.svg?branch=master)](https://www.travis-ci.org/xdu-spm-0x5f3759df/xdu-spm-parknshop)

ParkNShop - 2017 XDU SPM Final Assignment

Technology:

- Spring Boot 2.0 (with Spring 5.0, Spring WebFlux, in asynchronous, reactive pattern)
- Project Reactor (reactive programming, event driven)

> Note: This repository only contains the backend code. The project is the very simple initial version with monolithic architecture. 
It should only be used as a blueprint project for study. The authentication component is very simple and security is not guaranteed.


# Build

Before you compile the project, set up the local maven repository for AliPay SDK:

```bash
cd payment/libs
mvn install:install-file -DgroupId=com.alipay -DartifactId=alipay-sdk-java -Dversion=20180104135026 -Dpackaging=jar -Dfile=alipay-sdk-java20180104135026.jar
```

Then compile and package the project:

```bash
mvn clean package
```

Ensure your MySQL (>= 5.7) and Redis Server (>= 4.0) is running, then run the backend:

```bash
java -jar ./shop-api/target/shop-api-1.0.jar
```
