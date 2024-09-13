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

### 피드백
- mac.bae(주강사)
  - 기능 요구사항에 따라 코드를 잘 작성하였습니다.
  - 기능과 역할에 따라 클래스 객체를 잘 설계하여 구현하였습니다.
  - DTO와 Entity를 활용하여 계층 간 통신 및 서비스 로직에서 데이터가 올바른 형식으로 처리될 수 있도록 잘 구현하였습니다.
  - getDBRandomQuotes / getDBRandomQuote 와 같이 데이터의 단수/복수 를 다루는 함수에서, 단수 함수를 활용하여 복수 함수를 구현한다면 동일 로직을 재사용하는 방식으로 코드를 리팩토링 할 수 있을 것 같습니다.
  - 즉, 복수 함수에서는 단수 함수를 N번 실행하는 방식으로 코드를 구현할 수 있습니다. 
  - 현재 코드에서는 함수를 수정하려면 두 가지 함수 모두 수정해야 하는 단점이 있습니다.