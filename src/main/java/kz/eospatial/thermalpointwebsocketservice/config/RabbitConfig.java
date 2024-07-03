package kz.eospatial.thermalpointwebsocketservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue thermalPointQueue() {
        return new Queue("thermalPointsQueue", true);
    }
}