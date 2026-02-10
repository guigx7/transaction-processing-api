package com.example.transactionprocessing.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class ExternalProcessorClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean processTransaction() {

        Map response = restTemplate.postForObject(
                "http://localhost:8080/external-processor/process",
                null,
                Map.class
        );

        return Boolean.TRUE.equals(response.get("approved"));
    }
}
