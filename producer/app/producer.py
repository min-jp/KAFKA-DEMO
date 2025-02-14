from confluent_kafka import Producer
from dotenv import load_dotenv
import os

class ProdFactory :
    load_dotenv()
    # Kafka 브로커 설정
    conf = {
        'bootstrap.servers': f'{os.getenv("SPRING_KAFKA_BOOTSTRAP_SERVERS")}',  # Kafka 브로커 주소
        'client.id': 'python-producer',
    }

    # 메시지 전송 콜백 함수
    def delivery_report(err, msg):
        if err is not None:
            print(f'❌ produce err: {err}')
        else:
            print(f'✅ produce succ: {msg.topic()} [{msg.partition()}] {msg.value().decode("utf-8")}')

    @classmethod
    def publish(cls, topic, message) :
        # Kafka 프로듀서 생성
        producer = Producer(cls.conf)
        # 메시지 전송
        producer.produce(topic, message.encode('utf-8'), callback=cls.delivery_report, )
        producer.flush()  # 메시지 전송 보장