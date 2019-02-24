FROM openjdk:8-jre-alpine
MAINTAINER Rudas Bálint <balintrudas@gmail.com>
ARG JAR_PATH
ARG EXPOSE
ADD $JAR_PATH auth-service.jar
ENTRYPOINT ["java", "-jar", "/auth-service.jar"]
EXPOSE $EXPOSE