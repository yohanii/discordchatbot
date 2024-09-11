FROM openjdk:21-oracle

# 작업 디렉토리 설정
WORKDIR /app

VOLUME /tmp

ARG JAR_FILE=build/libs/discordchatbot-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/application.yml /app

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]