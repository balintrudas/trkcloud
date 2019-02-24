FROM openjdk:8-jre-alpine
MAINTAINER Rudas Bálint <balintrudas@gmail.com>
ARG JAR_PATH
ARG EXPOSE
ADD $JAR_PATH account-service.jar
ENTRYPOINT ["java", "-jar", "/account-service.jar"]
EXPOSE $EXPOSE