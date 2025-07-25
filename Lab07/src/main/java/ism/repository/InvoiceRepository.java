package ism.repository;

import ism.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByDueDateBeforeAndPaid(LocalDate date, Boolean paid);
    List<Invoice> findByPaidFalse();
}