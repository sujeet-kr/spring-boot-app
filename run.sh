#!/bin/bash
gradle clean
gradle build docker
docker run -p 5000:8080 --rm sujeetk/concat-rest-service