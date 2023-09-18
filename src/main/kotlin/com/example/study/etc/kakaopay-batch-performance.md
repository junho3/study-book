# Batch Performance 극한으로 끌어올리기: 1억 건 데이터 처리를 위한 노력 / if(kakao)2022 소개

https://youtu.be/L9K0l65wMbQ?feature=shared
https://tech.kakaopay.com/post/ifkakao2022-batch-performance-read/

41분짜리 영상
1. 대량 데이터 READ
2. 데이터 Aggregation 처리
3. 대량 데이터 WRITE
4. Batch 구동 환경
5. 대량 데이터 처리 방식 총 정리

## 영상을 선택한 이유

- 결제 도메인을 하게 되면 같이 따라오는 것이 정산 도메인
- 보통 배치 서비스는 스케일 아웃이 불가능하여, 제한된 리소스에서 효율적으로 처리 필요
- 코드 레벨 외에도 퀴리문, Redis를 활용한 아키텍처 소개

<img src="https://github.com/junho3/study-book/assets/54342973/d6622e13-da62-4def-8287-9bfa511086e6">

<img src="https://github.com/junho3/study-book/assets/54342973/41a39e58-1567-4283-adb2-0a930dbae6a6">

## 1. 대량 데이터 READ

### Chunk

<img src="https://github.com/junho3/study-book/assets/54342973/2378b621-3442-422c-815e-53b02f77fec3">

<img src="https://github.com/junho3/study-book/assets/54342973/f4300940-a6d5-4117-8779-2be5c1cc122f">

배치는 데이터 조회 비중이 높으므로, 특정 조건의 데이터를 효율적으로 가져오는 것이 성능 개선의 핵심

<img src="https://github.com/junho3/study-book/assets/54342973/98a878ed-7b20-410f-88cb-ddb9312783bb">

대량의 데이터를 한번에 읽어서 처리할 수 없음  
어플리케이션에서는 OOM이 발생하고, RDB에서는 CPU 사용률이 올라가면서 읽기/쓰기 지연이 발생  
따라서 특정 단위로 데이터를 잘라서 Chunk 처리 해야 함  

<img src="https://github.com/junho3/study-book/assets/54342973/ffb1667d-50bd-44bc-87c3-eae440336296">

기본적으로 page limit을 사용하여 데이터를 조회할 수 있지만, limit offset이 커질수록 성능 저하 발생  
Mysql RDB 입장에서는 5,000만번째의 데이터를 조회하는 것조차 부담  

<img src="https://github.com/junho3/study-book/assets/54342973/d2d6612f-cc64-4893-8318-9273bd3a8aa6">

id (PK) 조건을 추가하고, limit offset을 0으로 유지함  

### Cursor

<img src="https://github.com/junho3/study-book/assets/54342973/dee17cbc-08a8-4341-9e51-31ddecc7c386">

JdbcCursorItemReader를 사용하려면 Native Query를 사용해야하므로 유지보수가 좋지 않음  

> Cursor는 실제로 JDBC ResultSet의 기본 기능입니다.  
> ResultSet이 open 될 때마다 next() 메소드가 호출 되어 Database의 데이터가 반환 됩니다.  
> 이를 통해 필요에 따라 Database에서 데이터를 Streaming 할 수 있습니다.  
> https://jojoldu.tistory.com/336  
> 
> 컬리에서 친구추천적립금 지급 배치 개발 때 PHP(Laravel Eloquent)로 chunk를 사용한 경험이 있음  
> 특정 조건의 데이터 결과가 변할 수 있다면 chunk 대신 cursor를 사용 해야함  
> chunk는 limit SELECT 쿼리를 매번 DBMS에 요청하기 때문에 처리 과정에서 데이터가 추가/삭제/변경 된다면 의도하지 않은 결과를 발생할 수 있음  
> cursor는 한번의 SELECT 쿼리의 데이터 결과를 DBMS가 가지고 있고, 지정된 사이즈만큼 어플리케이션에 내려주기 때문에 처리 과정에서 데이터가 추가/삭제/변경 되어도 결과에 영향이 없음  

### Exposed

<img src="https://github.com/junho3/study-book/assets/54342973/b007a1d5-3ae0-48e4-bc16-4445065873ec">

코틀린 경량 ORM인 Exposed를 사용

> 최근에 내부 정산 서비스 개발 시 Exposed를 사용 했음  
> 대량의 PG(카카오페이) 정산 데이터 수집 시 Jpa의 saveAll(), deleteAll()은 부적합함  
> Jpa는 @GeneratedValue(strategy = IDENTITY)인 경우 bulk insert를 지원하지 않기 때문에 saveAll을 선언해도 데이터 한 건씩 Insert 쿼리문 발생  
> deleteAll() 하여도 select로 데이터를 한 건씩 조회하고, 삭제하기 때문에 느림   
> 
> 하나의 테이블에 대하여 Jpa Entity와 Exposed Entity 두 객체 정의  
> 비즈니스 로직은 Jpa로 처리하고, bulk insert, delete는 Exposed로 처리하도록 함  
> 하나의 프로젝트에서 Jpa와 Exposed를 같이 사용하도록 환경 설정 과정에서 트러블 슈팅 경험  

<img src="https://github.com/junho3/study-book/assets/54342973/5729c799-a5b3-48d5-93bc-93a6a2027a9a">

<img src="https://github.com/junho3/study-book/assets/54342973/06037b11-fb8f-49ee-83ea-482c4e1060d4">

### 성능 비교

<img src="https://github.com/junho3/study-book/assets/54342973/dd144015-78f1-49e4-abba-061bcef2e214">

JpaPagingItemReader는 데이터 수에 비해 선형적으로 성능저하가 발생하지 않음  
데이터는 3배가 증가했지만, 8배의 성능저하 발생  

### 정리

<img src="https://github.com/junho3/study-book/assets/54342973/9d4df6c7-7f71-45a8-ab3e-0eaca55949c4">

<img src="https://github.com/junho3/study-book/assets/54342973/4b24b681-af0c-45e6-bbf0-dfba3e9c28bd">

## 2. 데이터 Aggregation 처리

### 쿼리(DB)에 의존적인 시스템의 한계

<img src="https://github.com/junho3/study-book/assets/54342973/570af69a-6a5b-4dc2-8598-5967280fd3f0">

<img src="https://github.com/junho3/study-book/assets/54342973/6b4ae142-9dd3-4ab4-81d4-ef33c5ec8585">

> 일반적으로 쿼리문만으로 로직을 구성하려 함  
> 컬리에 있을 때 외주에서 개발한 정산 배치도 SELECT INSERT 로우 쿼리만으로 구현되어 있었는데, 하나의 쿼리문이 모니터 화면을 넘어갔음  
> 유지보수 불가능  

### 개선 방향

<img src="https://github.com/junho3/study-book/assets/54342973/b122c1b6-53ce-4421-ace0-84645223cda5">

<img src="https://github.com/junho3/study-book/assets/54342973/7dfab539-cbe6-4fd2-adbc-bff246ecbdb0">

> 로우 데이터를 조회하여 Redis에서 집계 처리  
> 
> 발표자 안성훈님에게 답변 받은 내용  
> 예를 들면 1000개의 아이템을 저장한다고 할 때 일정 사이즈가 될때까지 메모리에 아이템을 보관했다기 보다는 일정 사이즈(chunk size) 만큼의 Redis명령어를 모아서 요청한 것이 맞습니다.  
> 
> <b>일반적으로는 redis jpa를 사용하면 1개 redis-cli set 명령어를 1000번 요청할겁니다.</b>  
> 
> 그러나 제가 소개한 케이스의 경우에는 1000개의 set 명령어를 한번에 모아서 요청하게됩니다.  
> 즉, 토탈 명령어 개수는 1000개로 차이가 전혀 없으나 모아서 요청했냐 따로 요청했냐 이차이만 있습니다.  
> 실제로 redis에서 명령어를 처리하는 속도는 나노급으로 매우 빠릅니다. 그러나, 네트워크를 타고 명령어가 전달되는데 너무 많은 리소스 손실과 cpu의 blocking이 발생하기 떄문에 이것을 대량으로 명령어를 모아서 요청합니다.  

<img src="https://github.com/junho3/study-book/assets/54342973/278987ba-52ca-4472-949f-04f2165cc769">

<img src="https://github.com/junho3/study-book/assets/54342973/28464a6e-9f81-4030-8bd2-f411cb9633d4">

## 3. 대량 데이터 WRITE

### Batch Insert, Update

<img src="https://github.com/junho3/study-book/assets/54342973/58679117-805a-4797-9f6b-164f47e90a28">

<img src="https://github.com/junho3/study-book/assets/54342973/562d444c-0d65-4d94-a4c5-6607b8dd57fd">

<img src="https://github.com/junho3/study-book/assets/54342973/3447df08-5ac4-4ced-a9a2-e5f5b964ef63">

<img src="https://github.com/junho3/study-book/assets/54342973/0dbedd3e-ebbe-4e96-b1c2-b39141847a1f">

### 성능 비교

<img src="https://github.com/junho3/study-book/assets/54342973/49c44cda-c8d0-4ad9-9b5e-f164c1aa258d">


