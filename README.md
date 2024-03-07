# 모놀리식이지만 동시성은 해결하고 싶어 - 선착순

## BlockingQueue를 통해 Producer - Consumer 패턴 구현하기

### 배경

- "실습으로 배우는 선착순 이벤트 시스템"을 학습하면서, 분산 환경에서 선착순으로 발생하는 동시성 문제를 해결하는 방법을 학습했습니다.
- 이를 모놀리식 환경에 적용하기 위해, Redis와 Kafka를 각각 AtomicInteger와 BlockingQueue로 대체하여 자바 코드로 구현했습니다.

> 자세한 설명은 [블로그](https://junpak.tistory.com/49)를 참고해주세요

### 참고 자료

- [실습으로 배우는 선착순 이벤트 시스템](https://www.inflearn.com/course/선착순-이벤트-시스템-실습)
- [자바 공식문서 - BlockingQueue](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/BlockingQueue.html)
