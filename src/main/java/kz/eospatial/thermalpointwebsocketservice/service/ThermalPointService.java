package kz.eospatial.thermalpointwebsocketservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kz.eospatial.thermalpointwebsocketservice.dto.ApiResponse;
import kz.eospatial.thermalpointwebsocketservice.dto.ForestryDto;
import kz.eospatial.thermalpointwebsocketservice.exceptions.ForestryNotFoundException;
import kz.eospatial.thermalpointwebsocketservice.exceptions.InvalidTokenException;
import kz.eospatial.thermalpointwebsocketservice.exceptions.TokenExpiredException;
import kz.eospatial.thermalpointwebsocketservice.geo.GeoJsonFeature;
import kz.eospatial.thermalpointwebsocketservice.geo.GeoJsonFeatureCollection;
import kz.eospatial.thermalpointwebsocketservice.geo.GeoJsonGeometry;
import kz.eospatial.thermalpointwebsocketservice.model.ThermalPoint;
import kz.eospatial.thermalpointwebsocketservice.repo.ThermalPointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThermalPointService {

    private static final Logger logger = LoggerFactory.getLogger(ThermalPointService.class);

    @Autowired
    private ThermalPointRepository thermalPointRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final Map<String, ForestryDto> tokenCache = new ConcurrentHashMap<>();

    @Async
    public CompletableFuture<ForestryDto> addTokenToCacheAsync(String token) {
        if (token == null || token.isEmpty()) {
            throw new InvalidTokenException("Received empty or null token");
        }

        ForestryDto forestryDto = getForestryByToken(token);
        if (forestryDto != null) {
            forestryDto.setToken(token);
            tokenCache.put(token, forestryDto);
            logger.info("Token added to cache asynchronously: {}", token);

            // Немедленная отправка термальных точек при валидном подключении
            sendThermalPointsForToken(token, forestryDto);
        } else {
            logger.warn("Invalid token: {}", token);
        }
        return CompletableFuture.completedFuture(forestryDto);
    }

    @Async
    public CompletableFuture<Void> removeTokenFromCacheAsync(String token) {
        tokenCache.remove(token);
        logger.info("Token removed from cache asynchronously: {}", token);
        return CompletableFuture.completedFuture(null);
    }

    @Scheduled(fixedRate = 600000)
    public void sendThermalPointsToClients() {
        tokenCache.forEach(this::sendThermalPointsForToken);
    }

    private void sendThermalPointsForToken(String token, ForestryDto forestryDto) {
        try {
            List<ThermalPoint> thermalPoints;
            if ("Казахстан".equals(forestryDto.getName())) {
                thermalPoints = thermalPointRepository.findAll();
            } else {
                Long forestryId = forestryDto.getId();
                logger.info("Querying thermal points for forestry ID: {}", forestryId);
                thermalPoints = thermalPointRepository.findByForestryId(forestryId);
            }

            if (!thermalPoints.isEmpty()) {
                GeoJsonFeatureCollection featureCollection = convertToGeoJson(thermalPoints);
                logger.info("Sending {} thermal points for token: {}", thermalPoints.size(), token);

                try {
                    messagingTemplate.convertAndSend("/topic/thermal-points/" + token, featureCollection);
                } catch (IllegalStateException e) {
                    logger.warn("Failed to send message for token {}: session is closed.", token);
                }
            } else {
                logger.info("No thermal points found for token: {}", token);
                try {
                    messagingTemplate.convertAndSend("/topic/thermal-points/" + token, "No thermal points found.");
                } catch (IllegalStateException e) {
                    logger.warn("Failed to send message for token {}: session is closed.", token);
                }
            }
        } catch (Exception e) {
            logger.error("Error while sending thermal points for token: {}", token, e);
            try {
                messagingTemplate.convertAndSend("/topic/thermal-points/" + token, "Error while sending thermal points: " + e.getMessage());
            } catch (IllegalStateException ex) {
                logger.warn("Failed to send error message for token {}: session is closed.", token);
            }
        }
    }

    private GeoJsonFeatureCollection convertToGeoJson(List<ThermalPoint> thermalPoints) {
        List<GeoJsonFeature> features = thermalPoints.stream()
                .map(tp -> {
                    GeoJsonFeature feature = new GeoJsonFeature();
                    feature.setType("Feature");

                    GeoJsonGeometry geometry = new GeoJsonGeometry();
                    geometry.setType("Point");
                    geometry.setCoordinates(new double[]{tp.getLongitude(), tp.getLatitude()});
                    feature.setGeometry(geometry);

                    Map<String, Object> properties = new HashMap<>();
                    properties.put("brightness", tp.getBrightness());
                    properties.put("scan", tp.getScan());
                    properties.put("track", tp.getTrack());
                    properties.put("acq_date", tp.getAcqDate().toString());
                    properties.put("acq_time", tp.getAcqTime().toString());
                    properties.put("satellite", tp.getSatellite());
                    properties.put("confidence", tp.getConfidence());
                    properties.put("version", tp.getVersion());
                    properties.put("bright_t31", tp.getBrightT31());
                    properties.put("frp", tp.getFrp());
                    properties.put("daynight", tp.getDaynight());

                    // Проверка на null для localTime
                    if (tp.getLocalTime() != null) {
                        properties.put("local_time", tp.getLocalTime().toString());
                    } else {
                        properties.put("local_time", "N/A"); // Или оставьте это поле пустым, или укажите другое значение по умолчанию
                    }

                    feature.setProperties(properties);

                    return feature;
                })
                .collect(Collectors.toList());

        GeoJsonFeatureCollection featureCollection = new GeoJsonFeatureCollection();
        featureCollection.setType("FeatureCollection");
        featureCollection.setFeatures(features);

        return featureCollection;
    }


    public ForestryDto getForestryByToken(String token) {
        String url = "http://geo-forestry-app:8083/api/forestry/" + token;
//        String url = "http://localhost:8083/api/forestry/" + token;

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            logger.debug("JSON Response for token {}: {}", token, jsonResponse);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            ApiResponse apiResponse = objectMapper.readValue(jsonResponse, ApiResponse.class);
            return apiResponse.getForestry();
        } catch (HttpClientErrorException.BadRequest e) {
            logger.warn("Token validation failed for token: {}", token);
            throw new TokenExpiredException("Token validation failed: " + e.getResponseBodyAsString());
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("No forestry found for token: {}", token);
            throw new ForestryNotFoundException("No forestry found: " + e.getResponseBodyAsString());
        } catch (HttpClientErrorException e) {
            logger.error("Error retrieving forestry for token: {}", token, e);
            throw new RuntimeException("Error retrieving forestry", e);
        } catch (Exception e) {
            logger.error("Error retrieving forestry for token: {}", token, e);
            throw new RuntimeException("Error retrieving forestry", e);
        }
    }
}
