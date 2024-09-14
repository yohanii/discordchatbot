# 🤖 명언봇 - 디스코드챗봇
<img src="https://github.com/user-attachments/assets/cae62dd2-adcd-49ea-83ef-e6ba9681bbad" width=350>

> 명언을 통해 동기부여 해주는 디스코드 챗봇입니다.

### 개요
- 👨‍🔧 1인 개발
- 📆 개발 기간 : 24.07.02 ~ 24.09.13 (실개발 2주)


---

### 🔧 기능
- 명령어 사용을 통한 명언 출력 및 관리
  - OpenFeign을 사용해 외부 API 연동
  - DB 명언 CRUD
- 일정 시간마다 자동으로 명언 출력
  - java.util.Timer를 사용한 스케줄링
- Docker를 사용해, AWS EC2에 배포

<br>

### 🖥️ 명령어
- `/help`
  - 명령어 목록과 사용 방법 출력
- `/api-today`
  - Quotes API로 오늘의 명언 출력
- `/db-random`
  - DB에서 random한 명언 출력
- `/db-count {num}` 
  - 0 < num < 10
  - DB에서 num 개수만큼 명언 출력
- `/db-loop {time}`
  - time 단위 : s
  - 단위 시간 마다 명언 출력
- `/db-loop-stop`
  - dp-loop 종료
- `/db-all`
  - DB 모든 명언 조회
- `/db-add {author} {quote}`
  - DB에 명언 추가
- `/db-update {id} {author} {quote}`
  - DB 명언 수정
- `/db-delete {id}`
  - DB 명언 삭제

---

### 📚 기술 스택
- Spring Boot, JPA, OpenFeign
- MySQL
- Docker, AWS EC2

<br>

### 🗺️ 아키텍처
![discordchatbot_아키텍처](https://github.com/user-attachments/assets/879c4797-221f-4904-83b5-e2ca147f349d)


<br>


### ⚠️ 트러블 슈팅

목차
- [DB에서 Random한 명언 조회 시, 조회 오류 해결](#db에서-random한-명언-조회-시-조회-오류-해결)
- [Docker compose로 init.sql 실행 안되는 문제](#docker-compose로-initsql-실행-안되는-문제)
- [JDA String Characters 2000자 제한 문제](#jda-string-characters-2000자-제한-문제)
- [init.sql로 생성한 테이블에, JPA가 Row 추가 못하는 문제](#initsql로-생성한-테이블에-jpa가-row-추가-못하는-문제)
- [디스코드 답변 한글 깨지는 문제](#디스코드-답변-한글-깨지는-문제)
- [로컬에서 도커 띄웠을 때, 도커 컨테이너에서 local DB 연결 안되는 문제](#로컬에서-도커-띄웠을-때-도커-컨테이너에서-local-db-연결-안되는-문제)



<br>

#### DB에서 Random한 명언 조회 시, 조회 오류 해결
  - 문제
    - ID들이 띄엄띄엄 있을 때, 조회 오류 다수 발생하는 상황
    - 테이블 row Count를 받아, 그 안에서 Random id를 뽑아 조회하는 기존 방식
  - 해결
    - Native Query로 테이블 안 row들 중 Random Select로 해결
    - ```java
      @Query(value = "SELECT * FROM quote ORDER BY RAND() LIMIT 1", nativeQuery = true)
      Quote findRandom();
      ```
<br>

#### Docker compose로 init.sql 실행 안되는 문제
  - 해결
    - JPA가 table 생성 못하도록, Docker Container 간 depends_on 설정
      - ```yaml
        services:
          web:
            image: yohanii/dicobot_prod:1.16
            ...
            depends_on:
              db:
                condition: service_healthy
          db:
            ...
            healthcheck:
              test: [ "CMD", "mysqladmin", "ping", "-h", "localhost" ]
              interval: 10s
              timeout: 5s
              retries: 5
        ```
    - `sudo rm -rf ./mysqldata/*` 명령어를 통해 mysqldata 직접 삭제
      - `docker-compose down -v` 명령어가 mysqldata를 삭제 안 해주었기 떄문
    - MySQL docker image 버전 변경
      - latest -> 8.0

<br>

#### JDA String Characters 2000자 제한 문제
  - 문제
    - 디스코드 답변 String 2000자 제한으로 오류 발생
    - ```java
      java.lang.IllegalArgumentException: Content may not be longer than 2000 characters! Provided
      ```
  - 해결
    - Chunk Size 정해서, 15개 명언씩 출력
    - 답변을 보내는 `reply()`는 Interaction 당 1번이기 때문에, 메세지를 보내는 `sendMessage()`로 보내준다.

<br>

#### init.sql로 생성한 테이블에, JPA가 Row 추가 못하는 문제
  - 문제
    - ID가 겹쳐서 발생한 문제
    - ```java
      Caused by: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry '1' for key 'quote.PRIMARY'
      ```
  - 해결
    - init.sql의 quote 테이블 생성시 id 자료형 변경
      - INT -> BIGINT

<br>

#### 디스코드 답변 한글 깨지는 문제
  - 문제
    - DB shell 접속해서 확인했을 때 정상이지만, 조회 후 답변 시 한글 깨지는 상황
  - 해결
    - database, table에 설정 추가
    - ```mysql
      SET character_set_client = utf8mb4;
      SET character_set_connection = utf8mb4;
      SET character_set_results = utf8mb4;
      SET collation_connection = utf8mb4_general_ci;
      
      ALTER DATABASE chatbotdb CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
      ALTER TABLE quote CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
      ```

<br>

#### 로컬에서 도커 띄웠을 때, 도커 컨테이너에서 local DB 연결 안되는 문제
  - 해결
    - url : localhost -> host.docker.internal

---

### 😃 피드백
<img width="479" alt="image" src="https://github.com/user-attachments/assets/557793a4-64f5-4037-b92a-f76a3902ff8f">
<img width="603" alt="image" src="https://github.com/user-attachments/assets/173a3cb0-a55a-44c1-970e-c010a0a3abe3">


- mac.bae (피드백 반영완료)
  - 기능 요구사항에 따라 코드를 잘 작성하였습니다.
  - 기능과 역할에 따라 클래스 객체를 잘 설계하여 구현하였습니다.
  - DTO와 Entity를 활용하여 계층 간 통신 및 서비스 로직에서 데이터가 올바른 형식으로 처리될 수 있도록 잘 구현하였습니다.
  - getDBRandomQuotes / getDBRandomQuote 와 같이 데이터의 단수/복수 를 다루는 함수에서, 단수 함수를 활용하여 복수 함수를 구현한다면 동일 로직을 재사용하는 방식으로 코드를 리팩토링 할 수 있을 것 같습니다.
  - 즉, 복수 함수에서는 단수 함수를 N번 실행하는 방식으로 코드를 구현할 수 있습니다. 
  - 현재 코드에서는 함수를 수정하려면 두 가지 함수 모두 수정해야 하는 단점이 있습니다.
- bryan.kim (피드백 반영 중)
  - 하루에 하나 랜덤 출력 할 수 있으면 좋겠다.
  - 현재도 있지만, 명령어가 직관적이지 않은듯
