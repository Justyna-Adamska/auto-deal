

FROM openjdk:17-jdk-alpine
MAINTAINER Justyna_Adamska
COPY autoDeal/target/autoDeal-0.0.1-SNAPSHOT.jar autoDeal-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/autoDeal-SNAPSHOT.jar"]
