package org.example.consumer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Slf4j
@Service
public class StockTradeConsumer {
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper 생성
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String WEBSOCKET_SERVER_URL = "http://websocket-server:8080/api/trade/send"; // WebSocket 서버 REST API

    @Value("${KAFKA_CONSUMER_GROUP:default-group}")
    private String kafkaConsumerGroup;

    @KafkaListener(topics = "stock_trade", groupId = "#{T(java.lang.System).getenv('KAFKA_CONSUMER_GROUP') ?: 'default-group'}")
    public void consumeStockTrade(ConsumerRecord<String, String> record) {
        log.info("Kafka 메시지 수신: {}", record.value());

        //웹소켓 서버로 통신
        restTemplate.postForObject(WEBSOCKET_SERVER_URL, record.value(), String.class);
    }
}
