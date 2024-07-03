package kz.eospatial.thermalpointwebsocketservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.concurrent.ConcurrentHashMap;


@Component
public class StompSessionListener {

    private static final Logger logger = LoggerFactory.getLogger(StompSessionListener.class);

    private final SimpMessageSendingOperations messagingTemplate;
    private final ConcurrentHashMap<String, String> sessionIdTokenMap = new ConcurrentHashMap<>();

    public StompSessionListener(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String token = headerAccessor.getFirstNativeHeader("token");
        if (token != null) {
            String sessionId = headerAccessor.getSessionId();
            sessionIdTokenMap.put(sessionId, token);
            logger.info("Received a new web socket connection with token: " + token + " and session ID: " + sessionId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String token = sessionIdTokenMap.remove(sessionId);
        if (token != null) {
            logger.info("Web socket connection closed with session ID: " + sessionId + " and token: " + token);
        }
    }

    public ConcurrentHashMap<String, String> getSessionIdTokenMap() {
        return sessionIdTokenMap;
    }
}