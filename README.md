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

[노션 이동 > 요구 사항 정리](https://www.notion.so/5-923d79a0f3fa46ff931b2c8648cc49c0?p=0f541c2270bc48d5923b6934645f35e4&pm=s)
</details>

<details>
<summary>📦 ERD </summary>

![ERD](https://prod-files-secure.s3.us-west-2.amazonaws.com/bd96b46c-359f-495d-8a5b-33df5e09796a/392b92bc-988e-495e-aff3-561135a8a8ba/14b3a7a8-02c6-48c1-bb68-b8a54afe6d9a.png)
</details>

<details>
<summary>📦 시스템 구성도 </summary>

![시스템 구성도](https://prod-files-secure.s3.us-west-2.amazonaws.com/bd96b46c-359f-495d-8a5b-33df5e09796a/e6f1710e-6928-46b9-805d-e14a5a45c0a3/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-09-24_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_4.45.04.png)
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


## 🧑🏻‍ 팀원 소개 및 역할

|                                       문성현                                        |                                     정예찬(팀장)                                     |                                       김예찬                                       |                                       김연수                                       |                                       정승주                                        |
|:--------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------:|
| <img src="https://avatars.githubusercontent.com/u/101376904?v=4" width="200px"/> | <img src="https://avatars.githubusercontent.com/u/38793560?v=4" width="200px"/> | <img src="https://avatars.githubusercontent.com/u/97962775?v=4" width="200px"/> | <img src="https://avatars.githubusercontent.com/u/91796400?v=4" width="200px"/> | <img src="https://avatars.githubusercontent.com/u/114729161?v=4" width="200px"/> 
|                   [@sunghyun](https://github.com/sunghyun0610)                   |                      [@ycjung](https://github.com/skfk286)                      |              [@macmorning0116](https://github.com/macmorning0116)               |                    [@yeonsu00](https://github.com/yeonsu00)                     |                    [@Icecoff22](https://github.com/Icecoff22)                    
|                                  공통 예외 API <br/>& 댓글                                  |                                     티켓 & 쿠폰                                     |                       Kakao 소셜 로그인 <br/>& 유저 권한 <br/>& JWT 토큰                        |                     Naver 소셜 로그인 <br/>& 유저 권한 <br/>& JWT 토큰                     |                                        공연                                        |

