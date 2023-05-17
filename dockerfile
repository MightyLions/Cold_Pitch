# Base image
FROM openjdk:11-jdk-slim

# 애플리케이션을 빌드하는 데 필요한 디렉토리 생성
WORKDIR /app

# 필요한 파일 복사
COPY . /app

# 애플리케이션 빌드
RUN ./gradlew build

# JAR 파일을 Docker 이미지에 복사
COPY build/libs/ColdPitch-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9000

# 컨테이너 실행 시 실행할 명령
# RUN ["apt", "update"]
# RUN ["apt", "install", "-y", "vim"]
CMD ["java", "-jar", "app.jar"]
