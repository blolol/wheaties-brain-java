FROM openjdk:15-alpine
ARG BUILD_VERSION
COPY ./wheaties-brain-java-${BUILD_VERSION}-all.jar /usr/app/wheaties-brain-java.jar
CMD ["java", "-jar", "/usr/app/wheaties-brain-java.jar"]
