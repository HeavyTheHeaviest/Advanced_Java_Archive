package ism.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ism.model.SubaccountDTO;
import ism.service.SubaccountService;

import java.util.List;

@RestController
@RequestMapping("/api/subaccounts")
@RequiredArgsConstructor
public class SubaccountsApiController {
    private final SubaccountService service;

    @GetMapping
    public ResponseEntity<List<SubaccountDTO>> getAll() {
        return ResponseEntity.ok(service.getAllSubaccounts());
    }

    @PostMapping
    public ResponseEntity<SubaccountDTO> create(@RequestBody SubaccountDTO dto) {
        return new ResponseEntity<>(service.createSubaccount(dto), HttpStatus.CREATED);
    }
}