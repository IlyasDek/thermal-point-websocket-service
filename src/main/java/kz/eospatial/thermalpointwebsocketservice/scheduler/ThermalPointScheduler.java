//package kz.eospatial.thermalpointwebsocketservice.scheduler;
//
//import kz.eospatial.thermalpointwebsocketservice.model.ThermalPoint;
//import kz.eospatial.thermalpointwebsocketservice.service.ThermalPointService;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class ThermalPointScheduler {
//
//    @Autowired
//    private ThermalPointService thermalPointService;
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    @Scheduled(fixedRate = 5000)
//    public void sendThermalPoints() {
//        // Получаем список всех токенов лесничеств
//        List<String> forestryTokens = thermalPointService.getAllForestryTokens();
//
//        for (String token : forestryTokens) {
//            List<ThermalPoint> thermalPoints = thermalPointService.getThermalPointsByToken(token);
//            messagingTemplate.convertAndSend("/topic/thermal-points/" + token, thermalPoints);
//        }
//    }
//}