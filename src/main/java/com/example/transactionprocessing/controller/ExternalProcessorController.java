package com.example.transactionprocessing.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/external-processor")
public class ExternalProcessorController {

    private final Random random = new Random();

    @PostMapping("/process")
    public Map<String, Object> process() {

        boolean approved = random.nextBoolean();

        return Map.of(
                "approved", approved,
                "message", approved ? "Processed successfully" : "Processing failed"
        );
    }
}
