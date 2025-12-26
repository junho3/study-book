# MySQL JDBC Configuration for High-Performance Batch Jobs

https://gist.github.com/benelog/e7560ccf29c4365d939e9c3d210f9086

게시글을 GPT 도움을 받아 번역한 내용

## Using Server-Side Prepared Statements

PreparedStatement는 동일한 쿼리를 반복 실행할 때 성능을 최적화하는 데 도움이 된다.  
예를 들어 `SELECT * FROM CITY WHERE COUNTRY = 'KOREA' AND POPULATION > 10000`처럼 SQL 전체를 매번 문자열로 보내는 대신,
`SELECT * FROM CITY WHERE COUNTRY = ? AND POPULATION > ?` 처럼 변하지 않는 SQL 구조와 동적으로 바뀌는 파라미터를 분리한다.

쿼리가 반복 실행될 경우, 서버는 SQL을 한 번만 파싱하고 실행 계획을 생성한 뒤 이를 캐시에 저장하고, 이후에는 서로 다른 파라미터만 바꿔가며 재사용할 수 있다.  
이 방식은 서버의 CPU 사용량과 기타 리소스 소모를 줄여준다. 또한 JDBC 드라이버가 매번 전체 SQL이 아니라 변경되는 파라미터만 전송한다면 네트워크 트래픽 역시 감소시킬 수 있다.  
다만 이러한 최적화가 실제로 적용되는지는 DBMS의 구현 방식과 설정 옵션에 따라 달라진다.

MySQL에서는 `useServerPrepStmts` 옵션을 통해 `PreparedStatement`를 서버 측에서 처리할지 여부를 제어할 수 있다.

이 옵션의 기본값은 `false`다. 따라서 별도 설정을 하지 않으면 JDBC 드라이버는 매 실행마다 파라미터가 치환된 완전한 SQL 문자열을 서버로 전송한다.
이를 클라이언트 사이드 `PreparedStatement` 또는 `에뮬레이션 PreparedStatement`라고 부른다.
이 기본 방식의 장점은 쿼리 실행 시 네트워크 왕복이 한 번만 발생한다는 점이지만, 서버 차원의 PreparedStatement 최적화는 적용되지 않는다.

일반적인 웹 애플리케이션처럼 다양한 종류의 쿼리가 골고루 실행되는 환경에서는,
`useServerPrepStmts`를 켜기보다는 기본 동작을 유지하면서 `cachePrepStmts=true`를 활성화하고 관련 캐시 크기를 늘리는 편이 더 효율적인 경우가 많다.

`useServerPrepStmt=true`를 설정하면 PreparedStatement가 서버에서 파싱·준비된다.  
PreparedStatement 객체가 생성될 때 `?` 플레이스홀더를 포함한 템플릿 SQL이 서버로 전송되고, 서버는 이를 파싱해 준비 상태로 만든다.  
이후 execute()가 호출될 때마다 파라미터 값만 전달되며, 이미 준비된 쿼리가 그대로 실행된다.  
동일한 쿼리를 반복 실행하는 경우 SQL 파싱과 실행 계획 생성이 한 번만 이루어지므로 성능이 향상되고 네트워크 사용량도 줄어든다.

반면, 쿼리 실행을 위해 prepare → execute 최소 두 번의 네트워크 왕복이 필요하므로,
한 번만 실행되는 쿼리의 경우 오히려 성능이 나빠질 수도 있다.

MySQL에서 동일한 쿼리를 반복적으로 실행하는 애플리케이션이라면 `useServerPrepStmt=true`를 적극적으로 고려할 만하다.  
특히 소수의 쿼리를 수만 번 이상 반복 실행하는 배치 작업에서는 이 옵션이 매우 큰 성능 향상을 가져올 수 있다.

MySQL 5.1 이전 버전에서는 이 옵션을 사용하면 쿼리 캐시를 사용할 수 없었지만,
5.1 이후부터는 서버 캐시와 함께 사용할 수 있다.
또한 `MariaDB 10.6` 이상에서는 `useServerPrepStmts=true` 사용 시 메타데이터 재전송을 줄이는 추가 최적화가 적용되어, 성능이 더욱 개선된다.


### 셀프 Q&A

#### Q1: 어플리케이션 로그에 ?가 보이는데, DBMS가 PreparedStatement를 사용하는건가요?  

A1. 아닙니다.

- Hibernate 로그는 DB로 보내기 전 SQL 템플릿
- DBMS 사이드 PreparedStatement 사용 여부와는 무관
- 실제 DBMS PREPARE 여부는 JDBC 드라이버 설정에 의해 결정
  - jdbc:mysql://localhost:3306/practice?**useServerPrepStmts=true**
  - useServerPrepStmts=true가 아니면 DBMS PREPARE 안 함
  - useServerPrepStmts의 기본 값은 false
    - https://dev.mysql.com/doc/connector-j/en/connector-j-connp-props-prepared-statements.html


#### Q3: 웹 서비스에서도 쿼리 템플릿 수가 적으면 `useServerPrepStmts=true`가 좋지 않나요?

A2. 조건부로는 좋을 수 있습니다.

- 긍정적인 조건 
  - 쿼리 템플릿 수 적음 
  - 동일 쿼리 반복 빈도 높음 
  - DB CPU 병목 감소
    - `useServerPrepStmts=true`: SQL 문자열 파싱 (1회) → 옵티마이저 실행 (1회) → 실행계획 생성 (1회) → 실행
    - `useServerPrepStmts=false`: SQL 문자열 파싱 → 옵티마이저 실행 → 실행 계획 생성 → 실행
- 주의점 
  - prepare + execute = RTT(어플리케이션과 DBMS간 통신) 2회
    - 일반 쿼리: RTT 1회
  - 커넥션 풀 × 쿼리 수 만큼 서버 PS 증가

#### Q4. `?`가 무슨 값인지 모르는데 어떻게 파싱과 옵티마이저를 건너뛸 수 있나요?

A3. DB는 ‘값을 몰라도’ SQL의 구조만으로 파싱과 실행계획을 만든다. 값은 실행 단계에서만 필요하다.

- MySQL(및 대부분의 RDBMS)은 SQL을 이렇게 처리합니다
  1. Parsing (문법 분석)
  2. Optimization (실행계획 생성)
  3. Execution (실행)
- Prepared Statement는 1, 2단계를 `값 없이` 수행합니다.
- Parsing: 값이 없어도 가능한 이유
  - `UPDATE orders SET status = ? WHERE id = ?`
  - 파서가 보는 것은:
    - 테이블: orders 
    - 컬럼: status, id 
    - 연산: = 
    - 조건 구조: WHERE id = ? 
  - 값은 파서에게 중요하지 않습니다.
    - 문법이 맞는지 
    - 테이블/컬럼이 존재하는지 
    - 타입이 맞는지
- Optimizer: 값 없이도 플랜을 만들 수 있는 이유
  - 옵티마이저가 결정하는 것:
    - 어떤 인덱스를 사용할지 
    - 어떤 접근 방식(range / ref / const 등)
    - 조인 순서 
  - 이 판단은 대부분 SQL 구조 + 통계 정보로 가능합니다.
- 그럼 값에 따라 플랜이 달라지는 경우는?
  - WHERE created_at > ?
  - 실제 row 수는 달라질 수 있음
  - 그래도 동일한 플랜 사용


#### Q5. 배치에서는 왜 useServerPrepStmts=true가 좋은가요?

A. RTT보다 파싱/옵티마이저 비용이 훨씬 크기 때문입니다.

- 배치는 동일 SQL 수천~수만 번 실행
- false: 매번 SQL 파싱 + 플랜 생성
- true: 파싱/플랜 1회, 이후 EXECUTE만 반복
- RTT 증가는 무시 가능한 수준


## Improving Batch Update Performance

여러 행을 INSERT 또는 UPDATE할 때, 단건 쿼리를 반복해서 `executeUpdate()`로 실행하는 것보다 JDBC의 `Statement.batchUpdate()` 메서드를 사용하는 편이 더 빠를 수 있습니다.  
MySQL에서는 JDBC 연결 URL에 `rewriteBatchedStatements=true` 옵션을 설정하면 batchUpdate 성능을 추가로 최적화할 수 있습니다.

이 옵션의 기본값은 false이므로, 사용하려면 명시적으로 활성화해야 합니다.  
옵션을 켜면 MySQL JDBC 드라이버가 여러 개의 개별 쿼리를 하나의 SQL 문으로 합쳐서 전송합니다.  

예를 들어, 다음과 같은 INSERT 쿼리를 사용해 batchUpdate로 3건을 삽입한다고 가정해보겠습니다.

### 단일 INSERT 형태

```
INSERT INTO access_log(access_date_time, ip, username) VALUES (?, ?, ?);
```

rewriteBatchedStatements=true가 설정되지 않은 경우, 드라이버는 위 쿼리를 3번 각각 실행합니다.

반면 이 옵션을 활성화하면, 드라이버는 이를 하나의 쿼리로 병합합니다.


### 병합된 INSERT 형태

```
INSERT INTO access_log(access_date_time, ip, username) VALUES
(?, ?, ?),
(?, ?, ?),
(?, ?, ?);
```

다중 VALUES 구문을 사용할 수 없는 경우에도, 드라이버는 여러 개의 INSERT 또는 UPDATE 문을 `;`로 연결해 하나의 요청으로 묶어 전송합니다.

이 방식의 장점은 다음과 같습니다.

- 네트워크 왕복(Round Trip) 횟수가 줄어들고
- 서버가 SQL을 한 번만 파싱하고 실행하게 되어
- 전체 성능이 향상되고 DB CPU 사용량도 감소합니다

다만, 하나로 합쳐진 쿼리는 실행 시간이 길어질 수 있기 때문에, 복제(Replication) 환경에서는 복제 지연(replication lag) 이 더 커질 수 있습니다.


### ⚠️ 주의할 점

`rewriteBatchedStatements=true` 옵션은 다른 JDBC 옵션들과 충돌할 수 있습니다.

- `useServerPrepStmts=true`와 함께 사용하면 오류가 발생할 수 있고
- `useCursorFetch=true`를 지정하면 내부적으로 useServerPrepStmts=true가 자동 활성화되므로 동일한 주의가 필요합니다.

`Connector/J 8.0.29`까지는
`rewriteBatchedStatements가` `useCursorFetch=true` 또는
`useServerPrepStmts=true`와 함께 사용되면 무시된다고 문서에 명시되어 있었지만,
8.0.30부터는 이 제한이 제거되었습니다.

또한, 병합된 배치 쿼리의 크기가 MySQL의 `max_allowed_packet` 값을 초과하면 오류가 발생합니다.
대량의 데이터를 한 번에 INSERT/UPDATE하려면 이 값을 충분히 크게 설정해야 합니다.

### max_allowed_packet 관련

- MySQL 서버 설정 또는 세션 단위로 설정 가능
- 다음 쿼리로 확인할 수 있음:

```
SHOW VARIABLES LIKE 'max%';
```

한편, bulk_insert_buffer_size 설정은 현재 거의 사용되지 않는 MyISAM 엔진 전용 설정이므로, InnoDB를 사용하는 경우에는 신경 쓰지 않아도 됩니다.

---

이처럼 JDBC 옵션들 간에는 상호작용과 충돌이 많기 때문에, 읽기(Read)와 쓰기(Write) 모두에 최적인 단 하나의 DB 설정을 찾는 것은 어렵습니다.

대안으로는, 대규모 읽기 작업용 DataSource 대규모 쓰기(batch) 작업용 DataSource 를 애플리케이션 레벨에서 분리해 운영하는 방법도 고려할 수 있습니다.

마지막으로, Connector/J 8.0.28 이하 버전에서는 batchUpdate로 BLOB(Binary Large Object) 데이터를 INSERT할 때 NullPointerException이 발생하는 버그가 있었지만, 이는 최신 버전에서 이미 수정되었습니다.



