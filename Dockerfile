#docker build -t <image_tag> . && docker run -p localhost:8080:8081 --name geekenglish-demo <image_tag> $ docker bootJar --build-arg JAR_FILE=build/libs/*.jar -t myorg/myapp .
FROM openjdk:21-jdk
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","/app.jar"]