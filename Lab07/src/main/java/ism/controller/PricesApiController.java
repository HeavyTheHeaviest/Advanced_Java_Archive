package ism.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ism.model.PriceDTO;
import ism.service.PriceService;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PricesApiController {
    private final PriceService service;

    @GetMapping
    public ResponseEntity<List<PriceDTO>> getAll() {
        return ResponseEntity.ok(service.getAllPrices());
    }

    @PostMapping
    public ResponseEntity<PriceDTO> create(@RequestBody PriceDTO dto) {
        return new ResponseEntity<>(service.addPrice(dto), HttpStatus.CREATED);
    }
}