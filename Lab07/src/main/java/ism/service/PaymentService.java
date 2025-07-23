package ism.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ism.entity.Payment;
import ism.model.PaymentDTO;
import ism.repository.InvoiceRepository;
import ism.repository.PaymentRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository payRepo;
    private final InvoiceRepository invRepo;
    private final ModelMapper mapper;

    @Transactional
    public PaymentDTO registerPayment(PaymentDTO dto) {
        Payment pay = mapper.map(dto, Payment.class);
        Payment saved = payRepo.save(pay);
        double remain = dto.getAmount();
        for(var inv: invRepo.findByPaidFalse()) {
            if(!inv.getSubscription().getId().equals(dto.getSubscriptionId())) continue;
            if(remain>=inv.getAmount()){
                remain-=inv.getAmount();
                inv.setPaid(true);
                invRepo.save(inv);
            }
            if(remain<=0) break;
        }
        return mapper.map(saved, PaymentDTO.class);
    }

    @Transactional
    public List<PaymentDTO> getAllPayments() {
        return payRepo.findAll().stream()
                .map(p->mapper.map(p, PaymentDTO.class))
                .toList();
    }
}