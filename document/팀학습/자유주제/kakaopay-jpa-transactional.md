# 카카오페이 기술블로그 - JPA Transactional 잘 알고 쓰고 계신가요?

https://tech.kakaopay.com/post/jpa-transactional-bri/

## 해당 주제를 선택한 이유

- DB로 인한 시스템 전면 장애로 아무 것도 모르는 신입 시절 금요일 저녁 출근 후 토요일 오전 퇴근을 경험함
- 주문,결제 도메인을 주로 다루다보니 DB 장애에 민감  
- @Transactional 오남용 사례 코드를 꽤 많이 봄

## 배경

카카오페이 온라인결제 서비스 DB 리소스 사용률
- peak total qps: 24K
- select, commit 쿼리: 약 5K
- update, insert 쿼리: 약 3K 미만
- **set_option 쿼리: 약 14K**

> 작성자는 set_option 쿼리의 비중이 높다는 점에 주목


### set_option은 무엇인가?

JPA로 데이터를 조회했을 때 DB 로그
```
// @Transacitonal을 선언했을 때
'2025-02-05 13:09:19.122611','SET autocommit=0'
'2025-02-05 13:09:19.124989','select p1_0.product_id,p1_0.product_type,p1_0.created_at,p1_0.created_by,p1_0.product_amount,p1_0.product_code,p1_0.product_name,p1_0.product_status,p1_0.stock_id,p1_0.updated_at,p1_0.updated_by,p1_0.volt_type,p1_0.expiration_date from product p1_0 limit 0,10'
'2025-02-05 13:09:19.131072','select count(p1_0.product_id) from product p1_0'
'2025-02-05 13:09:19.133487','commit'
'2025-02-05 13:09:19.134738','SET autocommit=1'

// @Transacitonal(readOnly = true)을 선언했을 때
'2025-02-05 13:08:05.875828','set session transaction read only'
'2025-02-05 13:08:05.877544','SET autocommit=0'
'2025-02-05 13:08:05.879411','select p1_0.product_id,p1_0.product_type,p1_0.created_at,p1_0.created_by,p1_0.product_amount,p1_0.product_code,p1_0.product_name,p1_0.product_status,p1_0.stock_id,p1_0.updated_at,p1_0.updated_by,p1_0.volt_type,p1_0.expiration_date from product p1_0 limit 0,10'
'2025-02-05 13:08:05.884920','select count(p1_0.product_id) from product p1_0'
'2025-02-05 13:08:05.886825','commit'
'2025-02-05 13:08:05.888186','SET autocommit=1'
'2025-02-05 13:08:05.889682','set session transaction read write'
```

DBMS에 쿼리를 요청할 때 아래와 같은 옵션 쿼리들이 같이 요청 됨  
- set autocommit = 0; : 자동 커밋 비활성화
- set autocommit = 1; : 자동 커밋 활성화
- set session transaction read only; : 현재 세션의 트랜잭션을 읽기 전용 모드로 설정
- set session transaction read write; : 현재 세션의 트랜잭션을 읽기와 쓰기 모드로 설정 


### 실제로 set_option과 commit이 성능에 영향을 미칠까?

결과를 간단하게 요약하자면 트랜잭션이 없는 경우, select 쿼리가 약 2~3배 정도 증가해 DB 사용 성능이 향상된 것을 알 수 있었습니다. 
별도로 첨부하진 않았지만 api 테스트 결과 역시 마찬가지로 약 2배가량 좋아진 것을 확인했습니다.

이를 통해 set_option, commit 쿼리가 적어도 DB 조회 성능에 유의미 한 영향은 미칠 수 있다는 것을 한번 더 확인할 수 있었습니다.


## 개선 방향

작성자 팀에서 정의한 개선 방향
- transactional이 필요 없는 구간은 최대한 사용하지 않는다.
- transactional 사용 구간 안에 3rd party api가 끼지 않도록 persistence layer 바깥에서는 transactional을 사용하지 않는다.
- 의도치 않은 transaction 설정을 피하기 위해 class level에서의 transactional 설정은 하지 않는다.

> AOP 기반 @Transactional을 사용할 때 반드시 인지하고 있어야할 내용


### 1. 단건 요청에 대해서는 Transaction 제거하기

순화해서 다시 쓰자면 @Transactional을 쓸 상황을 최대한 줄이자, 가 되겠습니다.  

@Transactional은 spring에서 메서드의 원자성을 보장하기 위해 정의된 annotation interface입니다.
Spring이 데이터 저장소는 아니기 때문에 DB 등 다른 솔루션 없이 원자성을 보장하는 것에 한계가 있습니다. 
그러다 보니 자연스럽게 DB 조회 및 업데이트 관련 원자성 보장에 많이 활용하는 편입니다.

그렇다면 언제 이 원자성이 보장되어야 할까요?
바로 여러 데이터에 대한 update가 필요할 때입니다.

- 조회만 필요한 경우, Transactional이 필요하지 않습니다.
- 하나의 row만 update 할 경우, Transactional이 필요하지 않습니다.
- 동시성 제어만 필요한 경우, 다른 방법을 고려할 수 있습니다.

> 이 글의 핵심 내용  
> Spring은 트랜잭션이 AOP 기반으로 너무 쉽게 사용할 수 있어서 트랜잭션의 본래 취지를 놓치는게 아닌가하는 생각이 듦  
> 
> 팀원의 이전 조직에서는 기본 Datasource Connection이 Slave에 붙고, @Transactional이 있으면 Master로 붙도록 설정했다고 함  

```
// Kotlin Exposed
transaction(exposedDataSourceConfig.write()) {
    Members.batchInsert(
        data = members,
        ignore = false,
        shouldReturnGeneratedValues = false,
    ) {
        this[Members.name] = it.name
    }
}

// PHP Laravel
DB::transaction(function () {
    DB::update('update users set votes = 1');
    DB::delete('delete from posts');
});
```


### 2. @Transactional(readOnly=true)에 propagation 넣기

보통 @Transactional(readOnly=true)를 설정하면 조회용 dataSource를 접근하도록 설정을 하는데요.  

@Transactional(readOnly = true, propagation = SUPPORTS)    
다행히 @Transactional(readOnly=true)와 함께 propagation 설정을 잘 활용하면 readOnly DataSource는 보지만 transaction은 수행되지 않아 트랜잭션 없는 readOnly DB 접근이 가능해졌습니다.  
SUPPORTS는 메서드 단독 수행 시에는 트랜잭션이 동작하지 않지만 상위에 트랜잭션이 있는 경우 상위트랜잭션에 포함되어 수행되는 전파방식입니다.  

```
기술블로그를 통해 새롭게 알게 된 내용  

// @Transactional(readOnly = true, propagation = SUPPORTS)로 선언했을 때 read DataSource를 생성하는 로그  
com.zaxxer.hikari.HikariDataSource       : HikariPool-2 - Starting...  
com.zaxxer.hikari.pool.HikariPool        : HikariPool-2 - Added connection com.mysql.cj.jdbc.ConnectionImpl@3468588b  
com.zaxxer.hikari.HikariDataSource       : HikariPool-2 - Start completed.  

// set_option 쿼리 없음
'2025-02-05 13:06:37.979816','select count(p1_0.product_id) from product p1_0'
'2025-02-05 13:06:37.975190','select p1_0.product_id,p1_0.product_type,p1_0.created_at,p1_0.created_by,p1_0.product_amount,p1_0.product_code,p1_0.product_name,p1_0.product_status,p1_0.stock_id,p1_0.updated_at,p1_0.updated_by,p1_0.volt_type,p1_0.expiration_date from product p1_0 limit 0,10'
```


따라서 헷갈리지 않기 위해 공통 annotation 또는 확장 함수로 제공하여 비즈니스 로직에서는 직접적으로 @Transactional(readOnly=true)을 사용하지 않도록 PR 및 코드 컨벤션을 정했습니다.

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
annotation class ReadOnlyTransactional


```
// GPT의 도움으로 컨벤션을 ArchUnit으로 구현한 피트니스 함수
@ArchTest
@DisplayName("@Transactional(readOnly = true)를 선언한 곳이 있는지 검사")
void transactional_annotation_with_readOnly_true_should_not_use(JavaClasses importedClasses) {
    methods().that().areAnnotatedWith(Transactional.class)
        .should(new ArchCondition<>("not have readOnly = true") {
            @Override
            public void check(JavaMethod method, ConditionEvents events) {
                method.getAnnotations().stream()
                    .filter(annotation -> annotation.getRawType().isEquivalentTo(Transactional.class))
                    .forEach(annotation -> {
                        Map<String, Object> properties = annotation.getProperties();
                        if (properties.containsKey("readOnly") && Boolean.TRUE.equals(properties.get("readOnly"))) {
                            events.add(SimpleConditionEvent.violated(method,
                                method.getFullName() + " should use @ReadTransactional instead of @Transactional(readOnly = true)"));
                        }
                    });
            }
        }).check(importedClasses);
}
```

### 3. 조회가 빈번한 엔티티는 findById override 하기

```
class OrderService {
    
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    fun findById() { repository.findById() }
}

@Transactional(readOnly = true)
public class SimpleJpaRepository<T, ID> implements JpaRepositoryImplementation<T, ID> {
```

JPA에서 기본으로 제공하는 메소드 구현체에 `@Transactional(readOnly = true)`가 붙어 있음  
SimpleJpaRepository를 우회하려면 Querydsl로 override 필요  



## 개인적인 후기

최근에 본 기술블로그 중에서 가장 도움이 되었던 내용으로 기회가 된다면 실무에도 적용하고 싶음  
링크드인에서 복잡한 로직일 때 다른 결과가 나올 수 있다는 토비님의 의견도 있어서 상황에 맞게 적용할 필요가 있는 듯  
트랜잭션의 의미를 다시 생각하게 되었고, 업무 진행 시 목적에 맞는 `@Transactional`선언이 필요하다는 것을 느낌  

