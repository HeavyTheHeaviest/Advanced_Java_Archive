package ism.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ism.model.SubscriptionDTO;
import ism.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionsApiController {
    private final SubscriptionService service;

    @GetMapping
    public ResponseEntity<List<SubscriptionDTO>> getAll() {
        return ResponseEntity.ok(service.getAllSubscriptions());
    }

    @PostMapping
    public ResponseEntity<SubscriptionDTO> create(@RequestBody SubscriptionDTO dto) {
        return new ResponseEntity<>(service.createSubscription(dto), HttpStatus.CREATED);
    }
}