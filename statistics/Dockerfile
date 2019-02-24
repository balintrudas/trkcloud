FROM openjdk:8-jre-alpine
MAINTAINER Rudas Bálint <balintrudas@gmail.com>
ARG JAR_PATH
ARG EXPOSE
ADD $JAR_PATH statistics-service.jar
ENTRYPOINT ["java", "-jar", "/statistics-service.jar"]
EXPOSE $EXPOSE