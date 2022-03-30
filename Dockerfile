FROM openjdk:17
ARG BUILD_VERSION
COPY ./build/libs/wheaties-brain-java-${BUILD_VERSION}-all.jar /usr/app/wheaties-brain-java.jar
CMD ["java", "-jar", "/usr/app/wheaties-brain-java.jar"]
