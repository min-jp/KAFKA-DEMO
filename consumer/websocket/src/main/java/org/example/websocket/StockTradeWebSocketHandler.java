package org.example.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class StockTradeWebSocketHandler extends TextWebSocketHandler {

    private static final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        broadcast(message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    public void sendMessageToAllClients(String message) {
        broadcast(new TextMessage(message));
    }

    private void broadcast(TextMessage message) {
        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                try {
                    session.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
