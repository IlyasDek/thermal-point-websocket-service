package kz.eospatial.thermalpointwebsocketservice.controller;

import kz.eospatial.thermalpointwebsocketservice.dto.ForestryDto;
import kz.eospatial.thermalpointwebsocketservice.model.ThermalPoint;
import kz.eospatial.thermalpointwebsocketservice.service.ThermalPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class WebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private ThermalPointService thermalPointService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final Map<String, String> sessionTokenMap = new ConcurrentHashMap<>();

    @MessageMapping("/thermal-points")
    public void registerClient(String token, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        sessionTokenMap.put(sessionId, token);
        thermalPointService.addTokenToCache(token);
        logger.info("Received token: {} for session: {}", token, sessionId);
        messagingTemplate.convertAndSend("/topic/thermal-points/" + token, "Registered for updates.");
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String token = sessionTokenMap.remove(sessionId);
        if (token != null) {
            thermalPointService.removeTokenFromCache(token);
            logger.info("Removed token: {} for session: {}", token, sessionId);
        }
    }

    @ExceptionHandler(Exception.class)
    @SendToUser("/queue/errors")
    public String handleExceptions(Exception e) {
        logger.error("Error occurred: ", e);
        return "An error occurred: " + e.getMessage();
    }
}