package org.example.consumer.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final StockTradeWebSocketHandler stockTradeWebSocketHandler;

    public WebSocketConfig(StockTradeWebSocketHandler stockTradeWebSocketHandler) {
        this.stockTradeWebSocketHandler = stockTradeWebSocketHandler;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(stockTradeWebSocketHandler, "/ws/stock-trade")
                .setAllowedOrigins("*"); // 모든 도메인에서 접근 가능
    }
}