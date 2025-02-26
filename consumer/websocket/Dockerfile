FROM openjdk:17

WORKDIR /app

COPY build/libs/websocket-0.0.1-SNAPSHOT.jar websocket.jar

ENTRYPOINT ["java", "-jar", "websocket.jar"]
