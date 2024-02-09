FROM openjdk:17-jdk-alpine3.13
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV PORT=80
EXPOSE $PORT
ENTRYPOINT ["java","-jar","/app.jar"]
