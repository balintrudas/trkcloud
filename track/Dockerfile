FROM openjdk:8-jre-alpine
MAINTAINER Rudas Bálint <balintrudas@gmail.com>
ARG JAR_PATH
ARG EXPOSE
ADD $JAR_PATH track-service.jar
ENTRYPOINT ["java", "-jar", "/track-service.jar"]
EXPOSE $EXPOSE