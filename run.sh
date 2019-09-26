#!/bin/bash
gradle clean
gradle build docker
docker run -p 8080:8080 --rm sujeetk/concat-rest-service