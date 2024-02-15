FROM openjdk:17-jdk-alpine3.13
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV PORT=80
EXPOSE $PORT
EXPOSE 587
EXPOSE 465
EXPOSE 25
ENTRYPOINT ["java","-jar","/app.jar"]
