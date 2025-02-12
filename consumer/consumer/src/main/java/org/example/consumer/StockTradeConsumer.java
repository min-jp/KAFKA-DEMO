package org.example.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StockTradeConsumer {

    @KafkaListener(topics = "stock_trade", groupId = "stock-trade-group")
    public void consumeStockTrad(ConsumerRecord<String, String> record){
        log.info("체결된 데이터 : Key={}, Value={}", record.key());
    }
}
