CREATE SEQUENCE quote_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS quote (
    quote_id INT AUTO_INCREMENT PRIMARY KEY,
    author VARCHAR(50) NOT NULL,
    quote VARCHAR(300) NOT NULL
);

SET character_set_client = utf8mb4;
SET character_set_connection = utf8mb4;
SET character_set_results = utf8mb4;
SET collation_connection = utf8mb4_general_ci;

ALTER DATABASE chatbotdb CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE quote CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

INSERT INTO quote (author, quote) VALUES
    ('Edsger W. Dijkstra', '미래를 예측하는 가장 좋은 방법은 그것을 발명하는 것이다.'),
    ('Linus Torvalds', '말은 싸다. 코드를 보여줘.'),
    ('Donald Knuth', '조급한 최적화는 모든 악의 근원이다.'),
    ('Steve Jobs', '배고픔을 유지하고, 어리석음을 유지하라.'),
    ('Bill Gates', '가장 불행한 고객이 가장 큰 배움의 원천이다.'),
    ('Martin Fowler', '컴퓨터가 이해할 수 있는 코드를 쓰는 것은 바보나 할 수 있는 일이다. 좋은 프로그래머는 사람이 이해할 수 있는 코드를 쓴다.'),
    ('Jeff Atwood', '빠르게 가는 유일한 방법은 잘 가는 것이다.'),
    ('Kent Beck', '나는 훌륭한 프로그래머가 아니라, 좋은 습관을 가진 훌륭한 프로그래머일 뿐이다.'),
    ('Robert C. Martin', '깨끗한 코드는 단지 작동하는 코드가 아니다. 깨끗한 코드는 유지보수 가능하고 이해할 수 있는 코드이다.'),
    ('James Gosling', '버그가 아니라 문서화되지 않은 기능이다.'),
    ('Peter Drucker', '측정되는 것은 관리된다.'),
    ('Michael Feathers', '빠르게 가는 유일한 방법은 잘 가는 것이다.'),
    ('Guido van Rossum', '코드는 쓰는 것보다 읽는 것이 훨씬 더 많다.'),
    ('Ward Cunningham', '미래를 예측하는 가장 좋은 방법은 그것을 발명하는 것이다.'),
    ('Alan Turing', '우리는 이미 컴퓨터 시뮬레이션 안에 있을지도 모른다.'),
    ('Bjarne Stroustrup', '프로그래밍은 시스템 설계 행위이다.'),
    ('John Carmack', '집중은 당신이 하지 않을 것들을 결정하는 문제이다.'),
    ('Elon Musk', '무언가가 충분히 중요하다면, 확률이 불리하더라도 해야 한다.'),
    ('Larry Page', '그 아이디어를 개발하는 데 100명의 회사가 필요하지 않다.'),
    ('Mark Zuckerberg', '유일하게 실패가 보장된 전략은 위험을 감수하지 않는 것이다.'),
    ('Jeff Bezos', '우리는 비전에는 고집스럽다. 세부 사항에는 유연하다.'),
    ('Rich Hickey', '소프트웨어 설계의 목표는 이해하기 쉽고 유지보수하기 쉬운 것을 만드는 것이다.'),
    ('David Parnas', '소프트웨어에서 가장 중요한 것은 유지보수 가능성이다.'),
    ('Mary Poppendieck', '사업을 구축하는 것이 아니라 사람을 구축하고, 그러면 사람들이 사업을 구축한다.'),
    ('Sandi Metz', '소프트웨어의 근본적인 문제는 항상 변화한다는 것이다.'),
    ('Eric S. Raymond', '좋은 해커는 과학자만큼 엔지니어이다.'),
    ('Jim McCarthy', '훌륭한 프로그래머가 되고 싶다면 프로그래밍의 학생이 되어야 한다.'),
    ('Andy Hunt', '가장 중요한 것은 코드를 계속 작성하는 것이다.'),
    ('Joel Spolsky', '소프트웨어 개발자가 되기 위해 가장 중요한 것은 호기심을 유지하고 계속 배우는 것이다.'),
    ('Michael Lopp', '소프트웨어에는 “정해진 방식”이라는 것이 없다.'),
    ('Tim Peters', '파이썬의 선: 아름다움은 추하고, 명확함은 암시적이다. 단순함은 복잡함보다 낫고, 복잡함은 복잡한 것보다 낫다. 평면적은 중첩보다 낫고, 드문 것은 밀집보다 낫다.'),
    ('Martin Odersky', '무언가를 이해하고 싶다면, 직접 구현해보라.'),
    ('Cory House', '코드는 유머와 같다. 설명해야 한다면, 그것은 나쁘다.'),
    ('Alan Perlis', '프로그래밍 언어가 생각하는 방식을 바꾸지 않으면 배울 가치가 없다.'),
    ('Brett Victor', '당신이 보는 것이 당신이 얻는 것이다.'),
    ('Jeff Atwood', '더 나은 프로그래머가 되는 가장 좋은 방법은 코드를 작성하는 것이다.'),
    ('David Heinemeier Hansson', '당신이 아는 것이 아니라, 당신이 아는 것을 가지고 무엇을 할 수 있는지가 중요하다.'),
    ('Paul Graham', '미래를 예측하는 가장 좋은 방법은 그것을 발명하는 것이다.'),
    ('Don Norman', '디자인은 단지 어떻게 보이고 느끼는가가 아니다. 디자인은 어떻게 작동하는가이다.'),
    ('Dave Thomas', '훌륭한 프로그래머가 되기 위해서는 문제 해결 능력을 갖추어야 한다.'),
    ('John Resig', '다른 사람의 실수에서 배우는 것이 자신의 실수에서 배우는 것보다 쉽다.'),
    ('Mitch Hedberg', '나는 카드 게임을 하는 것을 좋아한다. 때때로 나는 사람들과 카드 게임을 하는 것도 좋아한다.'),
    ('Richard P. Feynman', '무언가를 배우고 싶다면 읽어라. 이해하고 싶다면 써라.'),
    ('Charles Simonyi', '중요한 것은 좋은 코드를 작성하는 것이 아니라, 좋은 방법으로 좋은 코드를 작성하는 것이다.'),
    ('Michael Abrash', '성능에 신경 쓰지 않으면, 성능에 대해 어떻게 신경 쓸지에 신경 써야 한다.'),
    ('Steve McConnell', '버그를 피하는 가장 좋은 방법은 처음부터 버그를 작성하지 않는 것이다.'),
    ('Doug McIlroy', 'UNIX 철학: 하나의 일을 잘 하고, 그 일을 잘 하도록 하라.'),
    ('Nassim Nicholas Taleb', '세 가지 가장 해로운 중독은 헤로인, 탄수화물, 그리고 월급이다.'),
    ('Harlan Mills', '문제를 해결하는 첫 번째 단계는 그것이 존재한다는 것을 인식하는 것이다.'),
    ('David Allen', '측정하지 않으면 관리할 수 없다.'),
    ('Chris Pine', '프로그래밍을 배우려면 코드를 읽어야 한다.'),
    ('Jim Collins', '좋은 것이 위대한 것이 되는 것은 최고의 리더가 아니라 최고의 팀이다.'),
    ('Jared Spool', '좋은 디자인은 무엇이 이해할 수 있고 기억에 남는 것이다. 훌륭한 디자인은 기억에 남고 의미 있는 것이다.');
