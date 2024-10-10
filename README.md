# NBE1_2_Team05
공연 관리자와 일반 사용자를 위한 소규모 공연 및 티켓 관리 서비스

## 🛠️ 개발환경

|      **Framework & Library**     | **Database** | **Build Tool** | **Java Version** |
|:--------------------------------:|:------------:|:--------------:|:----------------:|
|       Spring Boot 3.3.4          |    MySQL     |     Gradle     |      Java 17      |
|        Spring Data JPA           |              |                |                   |
|           QueryDSL               |              |                |                   |
|         Spring JDBC              |              |                |                   |
|        Spring Security           |              |                |                   |
|           Spring Web             |              |                |                   |
|        Spring WebFlux            |              |                |                   |
|            Lombok                |              |                |                   |
|            JUnit                 |              |                |                   |
|        OpenAPI (Springdoc)       |              |                |                   |
|      Spring RestDocs (Asciidoctor) |              |                |                   |
|       JWT (io.jsonwebtoken)       |              |                |                   |

## 💡 산출물
<details>
<summary>📦 요구사항 명세서 </summary>

[5팀 노션 이동 > 요구 사항 명세서](https://www.notion.so/5-923d79a0f3fa46ff931b2c8648cc49c0?p=0f541c2270bc48d5923b6934645f35e4&pm=s)
</details>

<details>
<summary>📦 ERD </summary>

<img width="1289" alt="erd" src="https://github.com/user-attachments/assets/fcf1e0f8-1429-4333-8cca-d5f3efa48957">
</details>

<details>
<summary>📦 스키마 </summary>

## 🗃️ 스키마 설명

| **Table Name**             | **Description**                                                                                         |
|----------------------------|---------------------------------------------------------------------------------------------------------|
| **member**                 | 회원 정보를 저장하는 테이블. 이메일, 패스워드, 프로바이더, 역할 등의 정보를 관리합니다.                     |
| **refresh_tokens**          | 리프레시 토큰을 저장하는 테이블. 회원과 연관된 리프레시 토큰과 만료 시간을 관리합니다.                       |
| **performance**             | 공연 정보를 저장하는 테이블. 공연 제목, 설명, 일정, 장소, 가격, 남은 티켓 수 등을 관리합니다.                |
| **comment**                 | 공연에 대한 댓글을 저장하는 테이블. 회원이 남긴 댓글과 상태 정보를 관리합니다.                             |
| **ticket**                  | 회원이 예매한 티켓 정보를 저장하는 테이블. 공연, 회원, 예매 시간, 예매 인원 등을 관리합니다.                 |
| **coupon**                  | 회원이 소유한 쿠폰 정보를 저장하는 테이블. 쿠폰 이름, 할인율, 사용 여부, 만료 시간 등을 관리합니다.            |
| **category**                | 공연 및 회원과 연관된 카테고리 정보를 저장하는 테이블. 카테고리 이름(한글/영문)을 관리합니다.                |
| **member_categories**       | 회원과 카테고리의 연관 관계를 저장하는 테이블. 특정 회원이 관심있는 카테고리를 관리합니다.                  |
| **performance_categories**  | 공연과 카테고리의 연관 관계를 저장하는 테이블. 특정 공연이 속한 카테고리를 관리합니다.                     |

## 🗃️ DDL

```sql
CREATE TABLE member (
    member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255),
    provider ENUM('LOCAL', 'NAVER', 'KAKAO') NOT NULL,
    provider_id VARCHAR(255),
    name VARCHAR(100) UNIQUE NOT NULL,
    role ENUM('ROLE_USER','ROLE_PADMIN','ROLE_ADMIN') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);


CREATE TABLE refresh_tokens (
    token_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT,
    refresh_token VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(member_id)
);

CREATE TABLE `performance` (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '공연 고유 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    title VARCHAR(50) NOT NULL COMMENT '공연 제목',
    date_st_time DATETIME NOT NULL COMMENT '공연 시작 일자 및 시간',
    date_end_time DATETIME NOT NULL COMMENT '공연 종료 일자 및 시간',
    description TEXT NOT NULL COMMENT '공연 설명',
    max_audience BIGINT NULL COMMENT '최대 관객 수 (NULL이면 제한 없음)',
    address VARCHAR(100) NOT NULL COMMENT '공연 장소',
    image_url VARCHAR(100) NULL COMMENT '공연 이미지 URL',
    price BIGINT NOT NULL DEFAULT 0 COMMENT '공연 가격',
    remaining_tickets BIGINT NULL COMMENT '남은 티켓 수',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '업데이트 시간',
    deleted_at DATETIME NULL COMMENT '삭제 시간',
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
);

CREATE TABLE comment (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '댓글 고유 ID',
    performance_id BIGINT NOT NULL COMMENT '공연 ID',
    user_id BIGINT NOT NULL COMMENT '회원 ID',
    comment TEXT NOT NULL COMMENT '댓글 내용',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '업데이트 시간',
    parent_id BIGINT NULL DEFAULT NULL COMMENT '부모 댓글 ID',
    status ENUM('ACTIVE', 'DELETED') NOT NULL DEFAULT 'ACTIVE' COMMENT '댓글 상태',
    PRIMARY KEY (id),
    FOREIGN KEY (performance_id) REFERENCES performance(id),
    FOREIGN KEY (user_id) REFERENCES member(member_id)
);

CREATE TABLE ticket (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '티켓 고유 ID',
    performance_id BIGINT NOT NULL COMMENT '공연 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    date_time DATETIME(6) NOT NULL COMMENT '티켓 예매 시간',
    quantity INT NOT NULL COMMENT '예매 인원',
    price INT NOT NULL DEFAULT 0 COMMENT '티켓 가격',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '업데이트 시간',
    deleted_at TIMESTAMP NULL COMMENT '삭제 시간',
    PRIMARY KEY (id),
    FOREIGN KEY (performance_id) REFERENCES performance(id),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
);

CREATE TABLE coupon (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '쿠폰 고유 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    name VARCHAR(30) NOT NULL COMMENT '쿠폰 이름',
    percent INT NOT NULL COMMENT '할인율',
    is_used BOOLEAN NOT NULL DEFAULT false COMMENT '쿠폰 사용 여부',
    expire_time TIMESTAMP NOT NULL COMMENT '쿠폰 만료 시간',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '쿠폰 발급 시간',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '쿠폰 수정 시간',
    deleted_at TIMESTAMP NULL COMMENT '쿠폰 삭제 시간',
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
);

CREATE TABLE category (
    category_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '카테고리 고유 ID',
    name_kr VARCHAR(30) COMMENT '카테고리 이름 (한글)',
    name_en VARCHAR(30) COMMENT '카테고리 이름 (영문)', // 'MUSIC', 'THEATER', 'DANCE', 'COMEDY', 'CIRCUS', 'MAGIC', 'VARIETY', 'EXHIBITION', 'FESTIVAL', 'OPERA', 'PUPPETRY', 'STANDUP', 'ETC') NOT NULL COMMENT '카테고리 이름'
	PRIMARY KEY (`category_id`)
);

CREATE TABLE member_categories (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '회원 카테고리 연결 고유 ID',
    category_id BIGINT NOT NULL COMMENT '카테고리 ID',
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '업데이트 시간',
    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES category(id),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
);

CREATE TABLE performance_categories (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '공연 카테고리 연결 고유 ID',
    performance_id BIGINT NOT NULL COMMENT '공연 ID',
    category_id BIGINT NOT NULL COMMENT '카테고리 ID',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '업데이트 시간',
    PRIMARY KEY (id),
    FOREIGN KEY (performance_id) REFERENCES performance(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

```

</details>

<details>
<summary>📦 시스템 구성도 </summary>

<img width="698" alt="아키텍처" src="https://github.com/user-attachments/assets/6c9ad89e-061a-4e6c-8568-7323a3438713">
</details>

<details>
<summary>📦 패키지 구조 </summary>

├─ src<br>
&nbsp;&nbsp;├─ main<br>
&nbsp;&nbsp;&nbsp;&nbsp;├─ java<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─ org<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─ socialculture<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─ platform<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;├─ comment<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;├─ config<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;├─ coupon<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;├─ global<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;├─ member<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;├─ performance<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;├─ ticket<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─ SocialCultureApplication.java<br>
&nbsp;&nbsp;&nbsp;&nbsp;└─ resources<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─ application.yml<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─ logback.xml<br>


├─ src<br>
&nbsp;&nbsp;├─ test<br>
&nbsp;&nbsp;&nbsp;&nbsp;├─ java<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─ org<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─ socialculture<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─ platform<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;├─ comment<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;├─ coupon<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;├─ member<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;├─ performance<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;├─ ticket<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;├─ util<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└─ SocialCultureApplicationTests.java<br>
</details>

<details>
<summary>📦 API 명세서 </summary>

[5팀 노션 이동 > API 명세서](https://www.notion.so/5-923d79a0f3fa46ff931b2c8648cc49c0?p=03230dc989784ce7b47e21d5cec919e2&pm=s)
</details>

## 💡 컨벤션
<details>
<summary>📦 깃 컨벤션 </summary>

| **커밋 유형**       | **의미**                                                    |
|---------------------|-------------------------------------------------------------|
| `Feat`              | 새로운 기능 추가                                             |
| `Fix`               | 버그 수정                                                    |
| `Docs`              | 문서 수정                                                    |
| `Style`             | 코드 formatting, 세미콜론 누락, 코드 자체의 변경이 없는 경우 |
| `Refactor`          | 코드 리팩토링                                                |
| `Test`              | 테스트 코드, 리팩토링 테스트 코드 추가                       |
| `Chore`             | 패키지 매니저 수정, 그 외 기타 수정 (ex: `.gitignore`)       |
| `Design`            | CSS 등 사용자 UI 디자인 변경                                 |
| `Comment`           | 필요한 주석 추가 및 변경                                     |
| `Rename`            | 파일 또는 폴더 명을 수정하거나 옮기는 작업만인 경우           |
| `Remove`            | 파일을 삭제하는 작업만 수행한 경우                           |
| `!BREAKING CHANGE`  | 커다란 API 변경의 경우                                       |
| `!HOTFIX`           | 급하게 치명적인 버그를 고쳐야 하는 경우                      |
</details>

<details>
<summary>📦 코드 컨벤션 </summary>

| **규칙** | **설명** |
|------------------------|------------------------------------------------------------|
| **문자열 처리**        | 문자열을 처리할 때는 쌍따옴표를 사용합니다.                |
| **함수명, 변수명**      | 카멜케이스로 작성하며, 의미와 의도를 가진 이름을 사용합니다.  |
| **클래스 명**           | UpperCamelCase로 작성합니다.                               |
| **상수 명**             | CONSTANT_CASE로 작성합니다.                                |
| **연산자와 공백**       | 연산자 사이, 소괄호와 중괄호 사이에는 공백을 넣습니다.      |
| **else, catch 등**      | 닫는 중괄호와 같은 줄에 else, catch, finally, while을 선언합니다. |
| **주석 달기**           | 클래스 및 메서드마다 주석을 달아줍니다. Javadoc보다는 이해를 돕기 위한 설명 위주로 작성합니다. |
| **한 줄 최대 길이**     | 한 문장은 최대 100글자로 제한합니다. 예외적으로 패키지 및 import 문, 주석 내 URL 등은 제외됩니다. |
| **줄 바꿈**             | 가독성을 위해 열 제한을 넘지 않아도 줄 바꿈을 할 수 있으며, "." 앞에서 끊습니다. |
| **중괄호 사용**         | 코드가 한 줄일 때에도 중괄호를 사용합니다.                 |
| **정적 팩터리 메서드**  | 객체 생성 시 정적 팩터리 메서드를 사용합니다. 매개변수가 4개 이상일 때는 빌더 패턴을 사용합니다. |
| **record 사용**         | 불변성을 유지하기 위해 DTO 또는 응답 데이터 객체로 `record` 문법을 사용합니다. |
| **인터페이스 사용 범위** | `ServiceImpl` 클래스를 작성하는 것으로 합니다.            |
| **의존성 주입**         | 생성자 주입을 사용하며, final 키워드를 붙입니다.           |

</details>

## 🧑🏻‍ 팀원 소개 및 역할

|                                       문성현                                        |                                     정예찬(팀장)                                     |                                       김예찬                                       |                                       김연수                                       |                                       정승주                                        |
|:--------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------:|
| <img src="https://avatars.githubusercontent.com/u/101376904?v=4" width="200px"/> | <img src="https://avatars.githubusercontent.com/u/38793560?v=4" width="200px"/> | <img src="https://avatars.githubusercontent.com/u/97962775?v=4" width="200px"/> | <img src="https://avatars.githubusercontent.com/u/91796400?v=4" width="200px"/> | <img src="https://avatars.githubusercontent.com/u/114729161?v=4" width="200px"/> 
|                   [@sunghyun](https://github.com/sunghyun0610)                   |                      [@ycjung](https://github.com/skfk286)                      |              [@macmorning0116](https://github.com/macmorning0116)               |                    [@yeonsu00](https://github.com/yeonsu00)                     |                    [@Icecoff22](https://github.com/Icecoff22)                    
|                                  공통 예외 API <br/>& 댓글                                  |                                     티켓 & 쿠폰                                     |                       Kakao 소셜 로그인 <br/>& 유저 권한 <br/>& JWT 토큰                        |                     Naver 소셜 로그인 <br/>& 유저 권한 <br/>& JWT 토큰                     |                                        공연                                        |

