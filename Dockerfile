FROM openjdk:17
WORKDIR /app
COPY build/libs/spring-security-demo-0.0.1-SNAPSHOT.jar security-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "security-app.jar"]