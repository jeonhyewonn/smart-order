# smart-order

## 실행 환경 설정
### 1. Docker로 Kafka 실행
#### kafka 띄우기
1. kafka-docker 다운 받기
```
    $ git clone https://github.com/wurstmeister/kafka-docker
```
2. docker-compose-single-broker.yml의 environment 수정
```
    kafka:
        environment:
          KAFKA_ADVERTISED_HOST_NAME: 127.0.0.1 // localhost로 수정
          KAFKA_CREATE_TOPICS: "test:1:1,test2:1:1" // 기본으로 생성 원하는 토픽명
```
3. docker-compose 실행
```
    $ docker-compose -f docker-compose-single-broker.yml up -d
```
#### kafka 명령어 실행
1. kafka container 접속
```
    $ docker exec -it ${kafka 컨테이너 ID} bash // zookeeper가 아닌 kafka 컨테이너 접속
```
2. topic 생성
```
    $ kafka-topics.sh --bootstrap-server localhost:9092 --create --topic test --replication-factor 1 --partitions 1
```
3. topic 리스트 확인
```
    $ kafka-topics.sh --bootstrap-server localhost:9092 --list
```
4. topic 개별 확인
```
    $ kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic test
```
5. consumer console 확인
```
    $ kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic test
```