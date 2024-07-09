# discordchatbot

### 기능
- 명령어 사용을 통한 명언 출력 및 관리
- 일정 시간마다 자동으로 명언 출력

### 명령어
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
- `/db-all`
  - DB 모든 명언 조회
- `/db-add {author} {quote}`
  - DB에 명언 추가
- `/db-update {id} {author} {quote}`
  - DB 명언 수정
- `/db-delete {id}`
  - DB 명언 삭제