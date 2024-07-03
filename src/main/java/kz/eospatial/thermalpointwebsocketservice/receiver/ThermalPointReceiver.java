//package kz.eospatial.thermalpointwebsocketservice.receiver;
//
//import kz.eospatial.thermalpointwebsocketservice.model.ThermalPoint;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//Component
//public class ThermalPointReceiver {
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    @RabbitListener(queues = "thermalPointsQueue")
//    public void receiveMessage(List<ThermalPoint> thermalPoints) {
//        if (!thermalPoints.isEmpty()) {
//            Long forestryId = thermalPoints.get(0).getForestryId();
//            messagingTemplate.convertAndSend("/topic/thermal-points/" + forestryId, thermalPoints);
//        }
//    }
//}