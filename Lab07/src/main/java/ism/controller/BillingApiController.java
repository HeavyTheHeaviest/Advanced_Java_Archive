package ism.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ism.service.BillingService;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class BillingApiController {
    private final BillingService service;

    @PostMapping("/generate")
    public ResponseEntity<Void> generateInvoices() {
        service.generateMonthlyInvoices();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reminders")
    public ResponseEntity<Void> sendReminders() {
        service.sendReminders();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/escalate")
    public ResponseEntity<Void> escalate() {
        service.escalate();
        return ResponseEntity.ok().build();
    }
}