package kz.eospatial.thermalpointwebsocketservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kz.eospatial.thermalpointwebsocketservice.dto.ApiResponse;
import kz.eospatial.thermalpointwebsocketservice.dto.ForestryDto;
import kz.eospatial.thermalpointwebsocketservice.exceptions.ForestryNotFoundException;
import kz.eospatial.thermalpointwebsocketservice.exceptions.TokenExpiredException;
import kz.eospatial.thermalpointwebsocketservice.model.ThermalPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ThermalPointService {

    private static final Logger logger = LoggerFactory.getLogger(ThermalPointService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final Map<String, String> tokenCache = new ConcurrentHashMap<>();

    public void addTokenToCache(String token) {
        tokenCache.put(token, token);
    }

    public void removeTokenFromCache(String token) {
        tokenCache.remove(token);
    }

    @Scheduled(fixedRate = 3000)
    public void sendThermalPointsToClients() {
        tokenCache.keySet().forEach(token -> {
            try {
                ForestryDto forestryDto = getForestryByToken(token);

                if (forestryDto != null) {
                    logger.info("Forestry found for token {}: {}", token, forestryDto.getName());

                    if ("Казахстан".equals(forestryDto.getName())) {
                        // Если лесничество с именем "Казахстан", вернуть все термальные точки в бд
                        List<ThermalPoint> thermalPoints = getAllThermalPoints();
                        if (!thermalPoints.isEmpty()) {
                            logger.info("Sending {} thermal points for Kazakhstan", thermalPoints.size());
                            messagingTemplate.convertAndSend("/topic/thermal-points/" + token, thermalPoints);
                        } else {
                            logger.info("No thermal points found for Kazakhstan");
                            messagingTemplate.convertAndSend("/topic/thermal-points/" + token, "No thermal points found for Kazakhstan.");
                        }
                    } else {
                        Long forestryId = forestryDto.getId();
                        logger.info("Forestry ID for token {}: {}", token, forestryId);

                        List<ThermalPoint> thermalPoints = getThermalPointsByForestryId(forestryId);
                        if (!thermalPoints.isEmpty()) {
                            logger.info("Sending {} thermal points for token: {}", thermalPoints.size(), token);
                            messagingTemplate.convertAndSend("/topic/thermal-points/" + token, thermalPoints);
                        } else {
                            logger.info("No thermal points found for token: {}", token);
                            messagingTemplate.convertAndSend("/topic/thermal-points/" + token, "No thermal points found for this forestry.");
                        }
                    }
                } else {
                    logger.info("No forestry found for token: {}", token);
                    messagingTemplate.convertAndSend("/topic/thermal-points/" + token, "No forestry found for this token.");
                }
            } catch (TokenExpiredException e) {
                logger.warn("Token has expired for token: {}", token);
                messagingTemplate.convertAndSend("/topic/thermal-points/" + token, "Token has expired.");
            } catch (ForestryNotFoundException e) {
                logger.warn("Forestry not found for token: {}", token);
                messagingTemplate.convertAndSend("/topic/thermal-points/" + token, "No forestry found for this token.");
            } catch (Exception e) {
                logger.error("Error while sending thermal points for token: {}", token, e);
                messagingTemplate.convertAndSend("/topic/thermal-points/" + token, "Error while sending thermal points: " + e.getMessage());
            }
        });
    }

    public ForestryDto getForestryByToken(String token) {
        String url = "http://geo-forestry-app:8083/api/forestry/" + token;
        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            logger.debug("JSON Response for token {}: {}", token, jsonResponse);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            ApiResponse apiResponse = objectMapper.readValue(jsonResponse, ApiResponse.class);
            return apiResponse.getForestry();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                logger.warn("Token validation failed for token: {}", token);
                throw new TokenExpiredException("Token validation failed: " + e.getResponseBodyAsString());
            } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.warn("No forestry found for token: {}", token);
                throw new ForestryNotFoundException("No forestry found: " + e.getResponseBodyAsString());
            } else {
                logger.error("Error retrieving forestry for token: {}", token, e);
                throw new RuntimeException("Error retrieving forestry", e);
            }
        } catch (Exception e) {
            logger.error("Error retrieving forestry for token: {}", token, e);
            throw new RuntimeException("Error retrieving forestry", e);
        }
    }

    public List<ThermalPoint> getAllThermalPoints() {
        String sql = "SELECT id, latitude, longitude, brightness, scan, track, acq_date, acq_time, local_time, satellite, confidence, version, bright_t31, frp, daynight " +
                "FROM fires";
        List<ThermalPoint> thermalPoints = jdbcTemplate.query(sql, (rs, rowNum) -> new ThermalPoint(
                rs.getLong("id"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude"),
                rs.getDouble("brightness"),
                rs.getDouble("scan"),
                rs.getDouble("track"),
                rs.getDate("acq_date").toLocalDate(),
                rs.getTime("acq_time") != null ? rs.getTime("acq_time").toLocalTime() : null,
                rs.getTime("local_time") != null ? rs.getTime("local_time").toLocalTime() : null,
                rs.getString("satellite"),
                rs.getString("confidence"),
                rs.getString("version"),
                rs.getDouble("bright_t31"),
                rs.getDouble("frp"),
                rs.getString("daynight"),
                null // forestry_id is not relevant in this case
        ));
        logger.info("Total thermal points: {}", thermalPoints.size());
        return thermalPoints;
    }

    public List<ThermalPoint> getThermalPointsByForestryId(Long forestryId) {
        String sql = "SELECT f.id, f.latitude, f.longitude, f.brightness, f.scan, f.track, f.acq_date, f.acq_time, f.local_time, f.satellite, f.confidence, f.version, f.bright_t31, f.frp, daynight, r.forestry_id " +
                "FROM fires f " +
                "JOIN fire_forest_relations r ON f.id = r.fire_id " +
                "WHERE r.forestry_id = ?";
        List<ThermalPoint> thermalPoints = jdbcTemplate.query(sql, new Object[]{forestryId}, (rs, rowNum) -> new ThermalPoint(
                rs.getLong("id"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude"),
                rs.getDouble("brightness"),
                rs.getDouble("scan"),
                rs.getDouble("track"),
                rs.getDate("acq_date").toLocalDate(),
                rs.getTime("acq_time") != null ? rs.getTime("acq_time").toLocalTime() : null,
                rs.getTime("local_time") != null ? rs.getTime("local_time").toLocalTime() : null,
                rs.getString("satellite"),
                rs.getString("confidence"),
                rs.getString("version"),
                rs.getDouble("bright_t31"),
                rs.getDouble("frp"),
                rs.getString("daynight"),
                rs.getLong("forestry_id")
        ));
        logger.info("Thermal points for forestry ID {}: {}", forestryId, thermalPoints.size());
        return thermalPoints;
    }
}