package kz.eospatial.thermalpointwebsocketservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateConfig.class);

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(logRequestInterceptor()));
        return restTemplate;
    }

    private ClientHttpRequestInterceptor logRequestInterceptor() {
        return (request, body, execution) -> {
            logger.debug("Request: {} {}", request.getMethod(), request.getURI());
            logger.debug("Request body: {}", new String(body));
            var response = execution.execute(request, body);
            logger.debug("Response: {}", response.getStatusCode());
            return response;
        };
    }
}