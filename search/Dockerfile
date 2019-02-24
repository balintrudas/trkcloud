FROM openjdk:8-jre-alpine
MAINTAINER Rudas Bálint <balintrudas@gmail.com>
ARG JAR_PATH
ARG EXPOSE
ADD $JAR_PATH search-service.jar
ENTRYPOINT ["java", "-jar", "/search-service.jar"]
EXPOSE $EXPOSE