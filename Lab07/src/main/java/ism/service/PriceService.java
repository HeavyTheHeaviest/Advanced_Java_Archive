package ism.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ism.entity.Price;
import ism.model.PriceDTO;
import ism.model.SubscriptionType;
import ism.repository.PriceRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class PriceService {
    private final PriceRepository priceRepo;
    private final ModelMapper mapper;

    @Transactional
    public PriceDTO addPrice(PriceDTO dto) {
        // domknięcie poprzedniej aktywnej ceny
        priceRepo.findAll().stream()
                .filter(p -> p.getType()==dto.getType() && p.getValidTo()==null)
                .forEach(p -> {
                    p.setValidTo(dto.getValidFrom().minusDays(1));
                    priceRepo.save(p);
                });
        Price p = mapper.map(dto, Price.class);
        Price saved = priceRepo.save(p);
        return mapper.map(saved, PriceDTO.class);
    }

    @Transactional
    public List<PriceDTO> getAllPrices() {
        return priceRepo.findAll().stream()
                .map(p -> mapper.map(p, PriceDTO.class))
                .collect(Collectors.toList());
    }

    public double currentPrice(SubscriptionType type, LocalDate date) {
        return priceRepo.findCurrentPrices(type,date).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No price"))
                .getAmount();
    }
}