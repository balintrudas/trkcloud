FROM openjdk
MAINTAINER Rudas Bálint <balintrudas@gmail.com>
ARG JAR_PATH
ARG EXPOSE
ADD $JAR_PATH config-service.jar
ENTRYPOINT ["java", "-jar", "/config-service.jar"]
EXPOSE $EXPOSE