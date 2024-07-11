package kz.eospatial.thermalpointwebsocketservice.controller;

import kz.eospatial.thermalpointwebsocketservice.dto.ForestryDto;
import kz.eospatial.thermalpointwebsocketservice.exceptions.InvalidTokenException;
import kz.eospatial.thermalpointwebsocketservice.service.ThermalPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class WebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private ThermalPointService thermalPointService;

    private final Map<String, ForestryDto> sessionForestryMap = new ConcurrentHashMap<>();

    @MessageMapping("/thermal-points")
    public void registerClient(@Payload String token, SimpMessageHeaderAccessor headerAccessor) {
        logger.debug("Received message to /thermal-points with token: {}", token);

        if (token == null || token.isEmpty()) {
            logger.error("Token is null or empty");
            throw new InvalidTokenException("Received empty or null token");
        }

        String sessionId = headerAccessor.getSessionId();
        logger.debug("Session ID: {}", sessionId);

        CompletableFuture<ForestryDto> future = thermalPointService.addTokenToCacheAsync(token);
        future.thenAccept(forestryDto -> {
            if (forestryDto != null) {
                sessionForestryMap.put(sessionId, forestryDto);
                logger.info("Received token: {} for session: {}. Forestry: {}", token, sessionId, forestryDto.getName());
            } else {
                logger.warn("Invalid token: {} for session: {}", token, sessionId);
                throw new InvalidTokenException("Invalid token: " + token);
            }
        }).exceptionally(ex -> {
            logger.error("Error while registering client with token: " + token, ex);
            return null;
        });
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        logger.debug("Handling disconnect for session ID: {}", sessionId);

        ForestryDto forestryDto = sessionForestryMap.remove(sessionId);
        if (forestryDto != null) {
            CompletableFuture<Void> future = thermalPointService.removeTokenFromCacheAsync(forestryDto.getToken());
            future.thenRun(() -> logger.info("Removed forestry: {} for session: {}", forestryDto.getName(), sessionId))
                    .exceptionally(ex -> {
                        logger.error("Error while removing forestry for session: " + sessionId, ex);
                        return null;
                    });
        } else {
            logger.warn("No forestry found for session: {}", sessionId);
        }
    }

    @ExceptionHandler(Exception.class)
    @SendToUser("/queue/errors")
    public String handleExceptions(Exception e) {
        logger.error("Error occurred: ", e);
        return "An error occurred: " + e.getMessage();
    }
}