package ism.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ism.util.SimulationClock;

@RestController
@RequestMapping("/api/simulation")
@RequiredArgsConstructor
public class SimulationApiController {
    private final SimulationClock clock;

    @PostMapping
    public ResponseEntity<Void> advance(@RequestParam int days) {
        clock.plusDays(days);
        return ResponseEntity.ok().build();
    }
}