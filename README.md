## 1. 서비스 개요
판매자에게는 상품을 전시하고 판매할 기회를, 구매자에게는 다양한 상품을 탐색하고 구매할 수 있는 기회를 제공하는 온라인 쇼핑몰 플랫폼으로 원활한 온라인 상거래가 이루어지도록 합니다.

## 2. 기획 동기
다양한 협업 툴(Git, Github, Jira, Figma, ERD Cloud, Swagger)과 백엔드/프론트엔드 기술(Java, Spring Boot, Spring Cloud, React), 데이터베이스(MySQL, Redis, Elasticsearch), 클라우드 인프라(AWS, Kubernetes), CI/CD 및 모니터링 툴(Docker, Jenkins, Harbor, Helm, ArgoCD, Prometheus, EFK, Grafana, Kafka)을 종합적으로 활용하는 과제로 전자상거래 플랫폼을 기획하였습니다. 종합적인 기술 스택을 통해 마이크로서비스 아키텍처를 구축하고, 신속한 배포와 안정적 운영, 실시간 모니터링 및 로그 분석, 그리고 효율적 협업 환경을 마련하고자 합니다.

---
## 3. 개발 이력
### 3.1. 개발기간: 2024년 12월 10일 ~ 2025년 2월 6일
### 3.2. 팀원: 김민우(Full-Stack), 정재훈(Back-end, Infra), 김성철
### 3.3. 기술스택
#### 3.3.1. 프런트엔드
<img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=REACT&logoColor=white">

#### 3.3.2. 백엔드
<img src="https://img.shields.io/badge/Java-F7DF1E?style=for-the-badge&logo=coffeescript&logoColor=white"> <img src="https://img.shields.io/badge/spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/JPA-59666C?style=for-the-badge&logo=hibernate&logoColor=white">

#### 3.3.3. 데이터베이스
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=redis&logoColor=white"> <img src="https://img.shields.io/badge/mongodb-47A248?style=for-the-badge&logo=mongodb&logoColor=white"> <img src="https://img.shields.io/badge/elasticsearch-005571?style=for-the-badge&logo=elasticsearch&logoColor=white">

##### 3.3.4. 클라우드 인프라
<img src="https://img.shields.io/badge/AWS EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"> <img src="https://img.shields.io/badge/AWS S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white"> <img src="https://img.shields.io/badge/AWS RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white"> <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"> <img src="https://img.shields.io/badge/kubernetes-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white"> <img src="https://img.shields.io/badge/apachekafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white"> <img src="https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white"> <img src="https://img.shields.io/badge/helm-0F1689?style=for-the-badge&logo=helm&logoColor=white"> <img src="https://img.shields.io/badge/argoCD-EF7B4D?style=for-the-badge&logo=argo&logoColor=white">

#### 3.3.5. CI/CD
<img src="https://img.shields.io/badge/jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white">

#### 3.3.6. 도구
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"> <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/Jira-0052CC?style=for-the-badge&logo=jira&logoColor=white"> <img src="https://img.shields.io/badge/ERD Cloud-60B932?style=for-the-badge&logo=ERD Cloud&logoColor=white"> <img src="https://img.shields.io/badge/slack-4A154B?style=for-the-badge&logo=slack&logoColor=white"> <img src="https://img.shields.io/badge/figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white">

---
## 4. 시스템 아키텍처 (기획안)
![](https://velog.velcdn.com/images/deer0123/post/d551bc23-410e-4b80-a706-968517c6aee3/image.png)

![](https://velog.velcdn.com/images/deer0123/post/49a509b9-0c36-400e-8735-74b43b628cc4/image.png)

![](https://velog.velcdn.com/images/deer0123/post/48c9b542-c02c-458d-8e70-01e7e6e40886/image.png)

![](https://velog.velcdn.com/images/deer0123/post/83f943d6-6bc5-4454-b989-4f12c5a5ba82/image.png)

---
## 5. WBS
![](https://velog.velcdn.com/images/deer0123/post/d977e848-eb12-4c09-ae9b-ec0df8ea7d54/image.png)

![](https://velog.velcdn.com/images/deer0123/post/a394e55a-4698-4047-99d4-8534bdcf945d/image.png)

---
## 6. 주요 기능
6.1. 판매자는 판매할 상품을 등록하고 관리한다.

6.2. 구매자는 원하는 상품을 탐색하고 주문 후 결제한다.

6.3. 구매자는 상품에 대한 리뷰 또는 문의를 남겨 판매자와 소통한다.

6.4. 관리자는 상품 할인, 쿠폰 발행, 배너 광고, 배치 처리, 알림을 통해 상거래를 촉진한다.

6.5. 관리자는 고객센터의 공지사항, 1:1 문의, FAQ를 통해 회원들과 소통한다.

---
## 7. 요구사항 명세서
![](https://velog.velcdn.com/images/deer0123/post/feb28c8b-a445-43a3-a04c-d66b38b732b6/image.png)
![](https://velog.velcdn.com/images/deer0123/post/06cda368-1863-4683-8744-b3c5121e82c7/image.png)
![](https://velog.velcdn.com/images/deer0123/post/1c3ee028-ff9a-4acb-9461-72967fd6b6c0/image.png)
![](https://velog.velcdn.com/images/deer0123/post/1b8a24c9-8941-4065-bad8-22a4b188d3cf/image.png)
![](https://velog.velcdn.com/images/deer0123/post/733529ae-2def-4a8a-9464-1fb76ad69dee/image.png)


---
## 8. 화면설계서
### 8.1. 사용자
![](https://velog.velcdn.com/images/deer0123/post/1642c53b-d358-453b-a166-27dd18819c78/image.png)
![](https://velog.velcdn.com/images/deer0123/post/18fe32ff-c752-4d3b-97f8-80d77b7e89c1/image.png)
![](https://velog.velcdn.com/images/deer0123/post/4cdf25fc-7d54-4e99-8b8b-6eb15c46dbce/image.png)

### 8.2. 판매자
![](https://velog.velcdn.com/images/deer0123/post/b37b5d34-6f7c-4861-b368-461a1ef6e42a/image.png)

### 8.3. 관리자
![](https://velog.velcdn.com/images/deer0123/post/0ea7ecca-92af-4fb7-8fd6-0cbdd2b0fdf9/image.png)
![](https://velog.velcdn.com/images/deer0123/post/988fc2dc-254f-4adc-92a5-1652750b2e43/image.png)


### 8.4. 공통
![](https://velog.velcdn.com/images/deer0123/post/87587b22-b6e5-4742-ab1b-17ba42c6cb66/image.png)

---
## 9. ERD
![](https://velog.velcdn.com/images/deer0123/post/f17bc7b4-257f-4470-9dad-c70c9b6a6aef/image.png)

![](https://velog.velcdn.com/images/deer0123/post/2127d45d-cee9-49f2-9b69-dc64b79828e8/image.png)

![](https://velog.velcdn.com/images/deer0123/post/c573f433-4ce1-4e7e-a654-d00a60ed4364/image.png)

![](https://velog.velcdn.com/images/deer0123/post/bd455025-2956-400d-94af-fefe61c29bc0/image.png)

![](https://velog.velcdn.com/images/deer0123/post/e5bb7f28-2702-43ba-a9a0-494b23256d9b/image.png)

![](https://velog.velcdn.com/images/deer0123/post/4aa8baf7-d48b-4082-9096-9e4963ae5e0b/image.png)

![](https://velog.velcdn.com/images/deer0123/post/d0a60443-5aa3-42f4-b119-edafe1b49a74/image.png)

![](https://velog.velcdn.com/images/deer0123/post/c9b5941c-a5b5-4ebe-8748-a9a3129a77a5/image.png)

![](https://velog.velcdn.com/images/deer0123/post/ad57a45d-2be5-43b4-8ae6-f0c872ef5559/image.png)

![](https://velog.velcdn.com/images/deer0123/post/00a295d0-b945-47a8-bda5-953fecebe2d3/image.png)

---
## 10. API
![](https://velog.velcdn.com/images/deer0123/post/169c9449-ee45-4df9-95de-b0bce2166ca9/image.png)

![](https://velog.velcdn.com/images/deer0123/post/9d7bf629-de04-4f49-8212-b79111e56dc3/image.png)

![](https://velog.velcdn.com/images/deer0123/post/ecd695a8-e082-4085-aa08-890d2597f90f/image.png)

![](https://velog.velcdn.com/images/deer0123/post/2023a60b-2c7b-4f73-98ee-7ac60e63c246/image.png)

![](https://velog.velcdn.com/images/deer0123/post/a00a8349-c497-4571-8857-cdb4d8125b0b/image.png)

![](https://velog.velcdn.com/images/deer0123/post/30e6954b-df63-4df4-9e40-0facea0a020f/image.png)

![](https://velog.velcdn.com/images/deer0123/post/adfca9e6-4b2f-4e0e-903e-160afe0bc638/image.png)

---
## 11. 문제와 해결
### 11.1. 사용자 유형에 따른 코드 작성 방대함
#### 11.1.1. 문제
회원, 판매자, 관리자로 구분되는 사용자 유형별 비즈니스 로직을 단순 조건문으로 처리하여 코드가 복잡하고 유지보수가 어려웠습니다.

#### 11.1.2. 해결
전략 패턴을 도입하여 공통 인터페이스를 정의하고 각 사용자 유형별로 전략을 구현함으로써 코드의 가독성과 유지보수성을 향상시켰습니다.

### 11.2. MSA 상에서 주문-결제-상품이 나눠져 있는 상황
#### 11.2.1. 주문-결제-상품 프로세스 문제
주문 생성, 결제 처리, 상품 재고 관리 등 복잡한 프로세스에서 오류 발생 시 데이터 정합성이 깨질 수 있었습니다.

#### 11.2.2. 해결
각 단계에서 오류 발생 시 이전 작업을 역순으로 복구하는 보상 함수를 구현하여 데이터 정합성을 보장했습니다.

### 11.3. 검색 기능
#### 11.3.1. 검색 기능문제
직접 데이터베이스 조회로 인한 성능 저하와 LIKE 검색의 한계로 사용자 요구사항을 충족시키지 못했습니다.

#### 11.3.2. 해결
엘라스틱서치를 도입하여 전문 검색과 유사 검색 기능을 구현함으로써 성능과 사용성을 개선했습니다.
