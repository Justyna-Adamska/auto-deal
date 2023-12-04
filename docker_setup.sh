#!/bin/bash
#create tmp directory to get dependencies
rm -rf tmp
mkdir tmp
cd tmp
#get jdk
wget https://download.java.net/java/GA/jdk21.0.1/415e3f918a1f4062a0074a2794853d0d/12/GPL/openjdk-21.0.1_linux-x64_bin.tar.gz
tar -xzvf openjdk-21.0.1_linux-x64_bin.tar.gz
#get mvn
wget https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz
tar -xzvf apache-maven-3.9.6-bin.tar.gz
#build jar file
cd ..
JAVA_HOME=tmp/jdk-21.0.1/ tmp/apache-maven-3.9.6/bin/mvn --file autoDeal/pom.xml clean package
#build docker image
sudo docker build --tag=jjk .
#run docker image
#map application port 8080 to host port 1234
sudo docker run -p1234:8080 jjk
