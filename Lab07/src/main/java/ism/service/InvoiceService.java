package ism.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ism.model.InvoiceDTO;
import ism.repository.InvoiceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepo;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public List<InvoiceDTO> getAllInvoices() {
        return invoiceRepo.findAll().stream()
                .map(inv -> {
                    InvoiceDTO dto = mapper.map(inv, InvoiceDTO.class);
                    dto.setSubscriptionId(inv.getSubscription().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}