FROM openjdk:8-jdk-alpine
LABEL maintainer="sujeet.kr@hotmail.com"
EXPOSE 8080
VOLUME /tmp
COPY concat-rest-service-0.1.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
