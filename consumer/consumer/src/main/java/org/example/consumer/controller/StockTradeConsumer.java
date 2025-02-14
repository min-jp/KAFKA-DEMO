package org.example.consumer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.consumer.websocket.StockTradeWebSocketHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class StockTradeConsumer {
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper 생성
    private final StockTradeWebSocketHandler webSocketHandler;

    public StockTradeConsumer(StockTradeWebSocketHandler webSocketHandler){
        this.webSocketHandler = webSocketHandler;
    }
    @KafkaListener(topics = "stock_trade", groupId = "stock-trade-group")
    public void consumeStockTrade(ConsumerRecord<String, String> record) {
        try {
            // JSON 문자열을 JsonNode 객체로 변환
            JsonNode jsonNode = objectMapper.readTree(record.value());

            // price와 timestamp 값 추출
            BigDecimal price = jsonNode.get("price").decimalValue();
            String timestamp = jsonNode.get("timestamp").asText();

            // JSON 형식으로 웹소켓 클라이언트에 전송할 메시지 생성
            String jsonMessage = String.format("{\"price\": \"%s\", \"timestamp\": \"%s\"}",
                    price.toPlainString(), timestamp);

            // 웹소켓을 통해 데이터 전송
            webSocketHandler.sendMessageToAllClients(jsonMessage);

            // 로그 출력
            log.info("체결가={}, 체결시간={}", price.toPlainString(), timestamp);

        } catch (Exception e) {
            log.error("JSON 파싱 중 오류 발생: {}", e.getMessage());
        }
    }
}
