package ism.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ism.entity.Subscription;
import ism.model.SubscriptionDTO;
import ism.repository.ClientRepository;
import ism.repository.SubscriptionRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subRepo;
    private final ClientRepository clientRepo;
    private final ModelMapper mapper;

    @Transactional
    public SubscriptionDTO createSubscription(SubscriptionDTO dto) {
        var client = clientRepo.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        Subscription s = new Subscription();
        s.setClient(client);
        s.setType(dto.getType());
        s.setStartDate(dto.getStartDate());
        s.setEndDate(dto.getEndDate());
        Subscription saved = subRepo.save(s);
        return mapper.map(saved, SubscriptionDTO.class);
    }

    @Transactional
    public List<SubscriptionDTO> getAllSubscriptions() {
        return subRepo.findAll().stream()
                .map(s -> mapper.map(s, SubscriptionDTO.class))
                .collect(Collectors.toList());
    }
}