# Java 17 기반 이미지 사용
FROM openjdk:17
WORKDIR /app

# JAR 파일 복사
COPY build/libs/consumer-0.0.1-SNAPSHOT.jar consumer-service.jar

# 애플리케이션 실행
CMD ["java", "-jar", "consumer-service.jar"]
