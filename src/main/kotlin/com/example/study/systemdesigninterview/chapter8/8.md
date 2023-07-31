# 8장 URL 단축기 설계

## 1단계 문제 이해 및 설계 범위 확정

- 주어진 긴 URL을 훨씬 짧게 줄인다.
- 축약된 URL로 HTTP 요청이 오면 원래 URL로 안내
- 높은 가용성과 규모 확장성, 그리고 장애 감내가 요구됨

## 2단계 개략적 설계안 제시 및 동의 구하기

### API 엔드포인트

### URL 리다이렉션

단축 URL을 받은 서버는 그 URL을 원래 URL로 바꾸어서 301 응답의 Location 헤더에 넣어 반환한다.

- 301 Permanently Moved: 이 응답은 해당 URL에 대한 HTTP 요청의 처리 책임이 영구적으로 Location 헤더에 반환된 URL로 이전되었다는 응답이다. 브라우저는 이 응답을 캐시한다.
- 302 Found: 이 응답은 주어진 URL로의 요청이 일시적으로 Location 헤더가 지정하는 URL에 의해 처리되어야 한다는 응답이다. 따라서 클라이언트의 요청은 언제나 단축 URL 서버에 먼저 보내진 후에 원래 URL로 리다이렉션 되어야 한다.

> 광고 클릭시 클릭 로그를 남겨야 하므로 반드시 서버에 요청을 해야함. 그래서 302로 리다이렉션 했음  
> 
> 광고 클릭 URL
> "adclick.g.doubleclick.net/aclk?sa=l&amp;ai=CRY96HbDHZOKOE8mjpt8PuYe2-AXFpez6cayQ1tCbEIekt6-XKRABIKOYmXxgm6PphLgpoAHU2aPVAcgBCakCan3Pd1z2Cj7gAgCoAwHIAwqqBLoCT9A8MeShBhQEYdoU2AcFsHQqBUsH2yBLytlqchXvnXJqUPD47A3irqsazptBxgJjW4HdZWr4HwKRXSWZP_k2l3T_RzJIwSwi50tlDYDWOPkkQkJYOVmbqHjlmSflEYb3y8DstJWng4EF3zekCuSY9oxd-UtLPhJrOrkkvmXvyCv5RWKsGUpQHLuRh2ltmjzhpDEuBOnoInS607UtGMO5jQyyDRHzDR3sbm6OQEOxrR_uNqPp54m7paUwSOHp-0Gfd8kz0Z0dXSjlYuLWqs9UpMYcWAqJb-HWACAtvTl8SmOC4FcQRb9eit-IlOVnKU4itD0Sbvcp3tG7q5j_4FaMnx36wkzMDxER3W6sEaTQkqLf2xqS7p_RLRIQWYemmdjNe4kaQ-YHfACQsbqnvBIuSJG1oN5bRLeLZ73ABI2lvPbVA-AEAaAGLoAHlKbcqgKoB9m2sQKoB47OG6gHk9gbqAfulrECqAf-nrECqAeko7ECqAfVyRuoB6a-G6gHmgaoB_PRG6gHltgbqAeqm7ECqAeDrbECqAf_nrECqAffn7ECqAfKqbECqAfrpbEC2AcA0ggUCIBhEAEYHTICigI6AoBASL39wTqxCbv028D2NPHagAoBmAsByAsBgAwBuAwB2gwQCgoQsJC9u8qVnI11EgIBA6oNAktSuBPkA9gTDIgUAdAVAZgWAfgWAYAXAQ&amp;ae=1&amp;ase=2&amp;num=1&amp;cid=CAQSTABpAlJWr0sFT3wHho4jm8ojpuXL7DLmO5v75VVdrz2gHKY3cmQAaq3fX5RXSrJs8anSIkeFHhS5puSjIsaUuuSwCGHCT1JREIINHX0YAQ&amp;sig=AOD64_0zR_rTeIfuwmBIcN4Lf1D3N5LITg&amp;client=ca-pub-7162146779303471&amp;rf=4&amp;nb=9&amp;adurl=**http://ad.ekolance.com/event/event_191202.asp%3Ftarget%3Dgdn1_ft%26bunya%3Dcpa_malist78%26gclid%3DEAIaIQobChMIopGt-P-4gAMVyZHpBR25gw1fEAEYASAAEgIoZvD_BwE**"
> 
> 광고 노출 수집용 URL (아마도)
> "securepubads.g.doubleclick.net/pagead/adview?ai=C4ChBTLHHZNrAGNSo29gPyI2yIMWl7PpxrJDW0JsQh6S3r5cpEAEgvpiZfGCbo-mEuCmgAdTZo9UByAEJqQJqfc93XPYKPuACAKgDAcgDCqoEuAJP0IufOuIF1reaXDlOA9edMQgaRr3j4klsBOdticlUQpn8-p4alkxm5Niei6m59ut7OYmLaXrPW3xKS5FYbqTuH7OmkTuqM6IRrBfmQPYOahoJ2oeByy_j43oVCpltxYjVLoAuaryJFcn4xT9mXhHoS7c_tF7sRabJYiWxKt5t8HT6uZOcFmiFPikPAq-7OGaEnncFANMqBKuvyAmolKL3ozxkCBCXjFXZABF9Fou1EcryiK8lmm67cNUdyvbXhzmVYG4VjgIm7LkqFMM07yI2ImOs51REnoaNXcMvtDlCLbr1buGle0lozs3PWXlR2YnEcDvutwLgZPeWfLPwgPkXj1z9TTqwLPROq7aWbeNh2L-dCBRp7mAiGDoBizsPZpTijqmJS7xynUl71DE_rKGT-peVnQyJ8XTABI2lvPbVA-AEAZIFBAgEGAGSBQQIBRgEkgUECAUYGJIFBQgFGKgBoAYugAeUptyqAqgH2baxAqgHjs4bqAeT2BuoB-6WsQKoB_6esQKoB6SjsQKoB9XJG6gHpr4b2AcA8gcEEIbnBNIIFAiAYRABGB0yAooCOgKAQEi9_cE6gAoDyAsBuBPkA9gTDIgUAdAVAZgWAYAXAbIXHgocCAASFHB1Yi03MTYyMTQ2Nzc5MzAzNDcxGLXUdA&sigh=_QqIOrgLM8c&uach_m=[]&cid=CAQSTABpAlJWro9IPPNHHyxW0zfUFLppQSufyDKyto_YLrG-CgWuGXep1Kv4bXupO4HMXdNQm7tjPhkRTF3GfvzWbG5DA-330WlOrQGkJ10YAQ&template_id=484&cbvp=2"

### URL 단축

- 입력으로 주어지는 긴 URL이 다른 값이면 해시 값도 달라야 한다.
- 계산된 해시 값은 원래 입력으로 주어졌던 긴 URL로 복원될 수 있어야 한다.

## 3단계 상세 설계

### 데이터 모델

해시 테이블은 메모리가 유한하고 비싸기 때문에 실제 시스템에서 사용하는 것은 어려움  
더 나은 방법은 관계형 데이터 베이스에 저장하는 것  

### 해시 함수

해시 함수는 원래 URL을 단축 URL로 변환하는 데 쓰인다.

#### 해시 값 길이

#### 해시 후 충돌 해소

CRC32, MD5, SHA-1  
해시 값에서 처음 7개 글자만 사용하고, 충돌할 경우 충돌이 해소될 때까지 사전에 정한 문자열을 해시값에 덧붙인다.  
단축 URL을 생성할 때 한 번 이상 데이터베이스 질의를 해야 하므로 오버헤드가 크다.  

#### base-62 변환

단축 URL 길이가 가변적

### URL 단축기 상세 설계

### URL 리디렉션 상세 설계

캐시를 먼저 조회하고, 데이터가 없으면 RDB 조회 후 리디렉션

## 4단계 마무리

- 처리율 제한 장치
- 웹 서버의 규모 확장
- 데이터베이스의 규모 확장
- 데이터 분석 솔루션
- 가용성, 데이터 일관성, 안정성
