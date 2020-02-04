FROM openjdk:11.0.5-jre-slim

VOLUME /tmp

EXPOSE 8087

ARG JAR_FILE=$PWD/target/service-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-Dserver.port=8087","-jar","/service-0.0.1-SNAPSHOT.jar"]