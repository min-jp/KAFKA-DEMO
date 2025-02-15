package org.example.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

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
                .setAllowedOrigins("*"); // CORS 설정 (모든 도메인 허용)
    }
}

