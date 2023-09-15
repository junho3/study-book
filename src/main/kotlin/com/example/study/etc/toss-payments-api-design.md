# "토스ㅣSLASH 21 - 결제 시스템의 SDK와 API 디자인" 영상 소개

https://youtu.be/E4_0WWqmF3M?feature=shared

27분짜리 영상
1. 결제 시스템 사용 겸험 
2. 결제 API 디자인 사례
3. Javascript SDK

## 영상을 선택한 이유

- 주요 업무인 Json 상하차를 하면서 자주 고민하게 되는 API 관련 주제  
- 네이버페이, 카카오페이, 토스, 스마일페이, 다날, 페이코, Chai 등 외부 결제 API를 연동하면서 PG사별 다른 연동 경험을 하게 됨  
- 최신 API일수록 스펙이 간소화되지만, 그 과정의 고민을 들어볼 수 있는 영상

## 결제 시스템 사용 경험

### PG 연동을 어렵게 하는 요인들

<img src="https://github.com/junho3/practice-java-spring-boot/assets/54342973/f0a8104c-67c7-4d49-8a97-0cc24c3ffb1d">

<img src="https://github.com/junho3/practice-java-spring-boot/assets/54342973/9a791cfe-9061-4392-b3ba-70c908591dbe">

> 구 LG U+(현 토스페이먼츠)의 연동 스펙으로 보임  


### 개선 방향

<img src="https://github.com/junho3/practice-java-spring-boot/assets/54342973/fa33f516-5258-4657-a561-775e6712133a">

> 뒷단 (DB, 테이블, 코어)까지 빅뱅으로 한번에 개선할 수 없으므로 인터페이스부터 변경하는 전략을 선택  
> 가맹점 입장에서는 뒷단은 관심없기 때문에 인터페이스가 개선된 것 만으로 아주 편해짐  

<img src="https://github.com/junho3/practice-java-spring-boot/assets/54342973/dfd09bdc-f828-493c-9921-c5f48c0f58f0">

<img src="https://github.com/junho3/practice-java-spring-boot/assets/54342973/bebc1bd0-7f41-45de-9c28-938a2ef8e7f3">

> PG 연동 과정에서 연동 모듈은 개발과 디버깅을 어렵게 하는 요소  
> 최악의 경우에는 연동 모듈을 암호화해둬서 가맹점에서 디버깅을 불가능하게 만듬  

<img src="https://github.com/junho3/practice-java-spring-boot/assets/54342973/614292d3-d0a6-4804-8d87-b42826004ff7">

> 인상 깊었던 부분  
> 토스에서는 PG 연동에 걸리는 시간을 수집한다고 함  

## 결제 API 디자인 사례

<img src="https://github.com/junho3/practice-java-spring-boot/assets/54342973/e65e514d-07fb-45d8-baa0-ce9e8407cac3">

<img src="https://github.com/junho3/practice-java-spring-boot/assets/54342973/605f9769-0aa0-4d38-9a98-1041624990ec">

<img src="https://github.com/junho3/practice-java-spring-boot/assets/54342973/379d41a1-6018-487d-ab04-d0f605610cfc">

> 내부 결제시스템 API 설계했을 때도 GET과 POST만 사용 했음  
> PUT과 DELETE를 사용할 수 있었으나, 부분취소의 멱등성을 따졌을 때 POST가 맞다고 판단했기 때문  
> 
> 10,000원 결제  
> 1,000원 부분취소 (잔액 9,000원)   
> 1,000원 부분취소 (잔액 8,000원)  
> 처음 1,000원과 두번째 1,000원은 다른 부분취소

<img src="https://github.com/junho3/practice-java-spring-boot/assets/54342973/bd3c6b9d-00f4-4dc5-a696-db575c3654d6">

<img src="https://github.com/junho3/practice-java-spring-boot/assets/54342973/de568267-ef5a-4f07-89d0-40d8fed7d603">

<img src="https://github.com/junho3/practice-java-spring-boot/assets/54342973/f532062e-805a-4a8f-be31-1b0fb2e6b277">

<img src="https://github.com/junho3/practice-java-spring-boot/assets/54342973/173a02a9-735e-44ea-aa03-40505632e910">

> 주제와 다소 벗어난 내용이지만, 결제 수단이 다른 카드결제 / 휴대폰결제 API가 각각 존재했을 때 Request 스펙을 어떻게 구성할 것인가?
> 
> 카드결제와 휴대폰결제 API 스펙을 다르게 가져간다.  
> /v1/payments/card
> {
>   transactionAmount: 1000,
>   cardNo: xxxxxxxx
> }  
> /v1/payments/phone
> {
>   transactionAmount: 1000,
>   phoneNo: 010-0000-0000
> }
> 
> 카드결제와 휴대폰결제 API 스펙을 동일하게 가져간다.  
> /v1/payments/card
> {
>   transactionAmount: 1000,
>   cardNo: xxxxxxxx,
>   phoneNo: null
> }  
> /v1/payments/phone
> {
>   transactionAmount: 1000,
>   cardNo: null,
>   phoneNo: 010-0000-0000
> }
> 
> 상황에 따라 다르겠지만, 내부 결제 API를 사용하는 프론트앤드 또는 주문 백엔드팀에서는 스펙을 동일하게 가져가는 것을 선호 했음  
> 결제수단별로 파라미터 등을 제어하는 것을 원하지 않음  
> API를 만드는 입장이 아닌 사용하는 입장도 고려해야한다고 느꼈던 계기  

<img src="https://github.com/junho3/practice-java-spring-boot/assets/54342973/2f50238b-b548-445e-874a-90f94086f6ee">

> 기본적인 CRUD API의 응답 포맷을 동일하게 가져가는 것도 인상적이었음  

<img src="https://github.com/junho3/practice-java-spring-boot/assets/54342973/beb5f1ec-10ab-4294-86df-01862752a2f6">

> 매직 넘버를 코드화한 것을 넘어서 헤더의 Accept-Language를 활용하여 국문 코드도 받을 수 있도록 함  

<img src="https://github.com/junho3/practice-java-spring-boot/assets/54342973/d40fa299-6755-4b03-ac4d-d2d18e4aebe5">

<img src="https://github.com/junho3/practice-java-spring-boot/assets/54342973/34b2f75d-a351-4eb0-913b-2f259032b4d9">

<img src="https://github.com/junho3/practice-java-spring-boot/assets/54342973/ec050433-2088-4187-b689-2eaf63ff66e3">
