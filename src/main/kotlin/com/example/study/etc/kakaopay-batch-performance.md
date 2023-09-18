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


<img src="https://github.com/junho3/study-book/assets/54342973/d6622e13-da62-4def-8287-9bfa511086e6">

<img src="https://github.com/junho3/study-book/assets/54342973/41a39e58-1567-4283-adb2-0a930dbae6a6">

## 대량 데이터 

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

<img src="https://github.com/junho3/study-book/assets/54342973/dee17cbc-08a8-4341-9e51-31ddecc7c386">

> Cursor는 실제로 JDBC ResultSet의 기본 기능입니다.  
> ResultSet이 open 될 때마다 next() 메소드가 호출 되어 Database의 데이터가 반환 됩니다.  
> 이를 통해 필요에 따라 Database에서 데이터를 Streaming 할 수 있습니다.  
> https://jojoldu.tistory.com/336  
> 
> 컬리에서 친구추천적립금 지급 배치 개발 때 PHP(Laravel Eloquent)로 chunk를 사용한 경험이 있음  
> 특정 조건의 데이터 결과가 변할 수 있다면 chunk 대신 cursor를 사용 해야함  
> chunk는 limit SELECT 쿼리를 매번 DBMS에 요청하기 때문에 처리 과정에서 데이터가 추가/삭제/변경 된다면 의도하지 않은 결과를 발생할 수 있음  
> cursor는 한번의 SELECT 쿼리의 데이터 결과를 DBMS가 가지고 있고, 지정된 사이즈만큼 어플리케이션에 내려주기 때문에 처리 과정에서 데이터가 추가/삭제/변경 되어도 결과에 영향이 없음  

