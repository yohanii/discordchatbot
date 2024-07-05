# discordchatbot

### 기능
- 명령어 사용
- 일정 시간마다 자동으로 명언 출력 with Quotes API

### 명령어
- `/api-random`
  - Quotes API로 random한 명언 출력
- `/api-count {num}` 
  - 0 < num < 10
  - Quotes API로 num 개수만큼 명언 출력
- `/db-random`
  - DB에서 random한 명언 출력
- `/db-count {num}` 
  - 0 < num < 10
  - DB에서 num 개수만큼 명언 출력
- `/db-add {author} {quote}`
  - DB에 명언 추가