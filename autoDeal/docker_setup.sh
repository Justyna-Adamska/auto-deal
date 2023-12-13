#!/bin/bash
#build docker image
sudo docker build --tag=jjk .
#run docker image
#map application port 8080 to host port 1234
sudo docker run -p1234:8080 jjk
