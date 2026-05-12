package com.ASD.Billing.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "Application Running";
    }
}
