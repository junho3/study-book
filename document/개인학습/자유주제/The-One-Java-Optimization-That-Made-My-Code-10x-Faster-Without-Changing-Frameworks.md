# The One Java Optimization That Made My Code 10x Faster — Without Changing Frameworks

https://medium.com/@kanishksinghpujari/the-one-java-optimization-that-made-my-code-10x-faster-without-changing-frameworks-1541cf79ec35

글쓴이는 하루에 수백만 건의 데이터를 처리하는 어플리케이션을 운영 중  
어느 날부터 p99 지연시간이 증가하고, GC가 자주돌고, 작은 트래픽에도 CPU 사용률이 높아짐  

스레드 풀 설정과 IO 메트릭을 점검했지만, 결정적인 원인을 발견할 수 없었음  
그러다가 비즈니스 로직에서 ObjectMapper 객체를 생성하는 코드를 볼 수 있었고, 해당 코드가 문제라는 것을 알게 됨  

## 범인 : 반복 객체 생성

```
public String processRecord(Data data) {
    ObjectMapper mapper = new ObjectMapper();
    try {
        return mapper.writeValueAsString(data);
    } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
    }
}
```
ObjectMapper는 가벼운 객체가 아님  
내부적으로 직렬화를 설정하고, 클래스를 스캔하고, 캐시 및 버퍼를 생성함  
ObjectMapper 객체를 매번 생성하여 GC와 CPU를 낭비하는 원인이 됨  

## 최적화 : 전역 싱글톤

```
public class MapperProvider {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static ObjectMapper get() {
        return MAPPER;
    }
}
```
ObjectMapper는 스레드 세이프하므로 싱글톤으로 구성하여 성능을 개선함  
하지만, 다수의 스레드로 부하를 발생시키자 여러 스레드가 동시에 접근하면서, 내부 버퍼나 구조들이 서로 엉키기 시작하였고, 성능 저하가 발생  


## 최적화 : 스레드 스코프 싱글 톤

```
public class ThreadLocalMapper {
    private static final ThreadLocal<ObjectMapper> mapper =
        ThreadLocal.withInitial(ObjectMapper::new);

    public static ObjectMapper get() {
        return mapper.get();
    }
}
```
ObjectMapper를 ThreadLocal을 이용해 스레드 단위로 관리하도록 변경  
이 방식은 각 스레드가 자기 전용 ObjectMapper 인스턴스를 하나만 생성하고, 이후로 계속 재사용  
이 변경을 통해 객체 생성 비용은 줄이면서, 스레드 간 경쟁 상태(inter-thread contention)도 제거할 수 있었음  


## ThreadLocal을 사용할 때 고려사항

ThreadLocal 최적화는 모든 경우에 적용할 수 있는 만능 해결책이 아니며, 세 가지 조건이 모두 충족될 때 효과적  

1. 객체 생성 비용이 높을 때 (예: ObjectMapper, StringBuilder, ByteBuffer)
2. 스레드별로 재사용해도 안전한 경우
3. 성능이 중요한 코드 경로에 있을 때

### 주의사항

ThreadLocal은 신중하게 사용해야 합니다:

메모리 누수 위험: 스레드 풀을 사용하는 환경에서는 ThreadLocal에 저장된 객체가 제거되지 않으면 메모리 누수가 발생할 수 있습니다.  
항상 ThreadLocal.remove()를 호출하여 정리해야 합니다.  
불변성 유지: 한 번 생성된 후에는 수정하지 않고 읽기 전용으로 다루는 것이 안전합니다.  


## 최적화를 적용한 결과:

- p99 지연 시간 80% 이상 감소
- GC 일시 정지 현상 사라짐
- 피크 시 CPU 사용량 50% 감소
- 코드는 여전히 단순하고 테스트 가능하며 예측 가능하게 유지됨

