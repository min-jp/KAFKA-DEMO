package org.example.websocket;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trade")
public class StockTradeController {

    private final StockTradeWebSocketHandler webSocketHandler;

    public StockTradeController(StockTradeWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @PostMapping("/send")
    public void sendStockTrade(@RequestBody String message) {
        webSocketHandler.sendMessageToAllClients(message);
    }
}