FROM openjdk:8-jre-alpine
MAINTAINER Rudas Bálint <balintrudas@gmail.com>
ARG JAR_PATH
ARG EXPOSE
ADD $JAR_PATH gateway-service.jar
ENTRYPOINT ["java", "-jar", "/gateway-service.jar"]
EXPOSE $EXPOSE