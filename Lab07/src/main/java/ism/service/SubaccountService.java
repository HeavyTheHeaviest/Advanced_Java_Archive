package ism.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ism.entity.Subaccount;
import ism.entity.Subscription;
import ism.model.SubaccountDTO;
import ism.repository.SubaccountRepository;
import ism.repository.SubscriptionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubaccountService {
    private final SubaccountRepository subaccountRepo;
    private final SubscriptionRepository subscriptionRepo;
    private final ModelMapper mapper;

    @Transactional
    public SubaccountDTO createSubaccount(SubaccountDTO dto) {
        // znajdź subskrypcję
        Subscription sub = subscriptionRepo.findById(dto.getSubscriptionId())
                .orElseThrow(() -> new RuntimeException("Subscription not found: " + dto.getSubscriptionId()));
        // zmapuj DTO na encję
        Subaccount entity = mapper.map(dto, Subaccount.class);
        entity.setSubscription(sub);
        // zapisz i zwróć zmapowany DTO
        Subaccount saved = subaccountRepo.save(entity);
        return mapper.map(saved, SubaccountDTO.class);
    }

    @Transactional(readOnly = true)
    public List<SubaccountDTO> getAllSubaccounts() {
        return subaccountRepo.findAll().stream()
                .map(sa -> {
                    SubaccountDTO dto = mapper.map(sa, SubaccountDTO.class);
                    dto.setSubscriptionId(sa.getSubscription().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}