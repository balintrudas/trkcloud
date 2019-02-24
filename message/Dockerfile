FROM openjdk:8-jre-alpine
MAINTAINER Rudas Bálint <balintrudas@gmail.com>
ARG JAR_PATH
ARG EXPOSE
ADD $JAR_PATH message-service.jar
ENTRYPOINT ["java", "-jar", "/message-service.jar"]
EXPOSE $EXPOSE