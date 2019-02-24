FROM openjdk:8-jre-alpine
MAINTAINER Rudas Bálint <balintrudas@gmail.com>
ARG JAR_PATH
ARG EXPOSE
ADD $JAR_PATH registry-service.jar
ENTRYPOINT ["java", "-jar", "/registry-service.jar"]
EXPOSE $EXPOSE