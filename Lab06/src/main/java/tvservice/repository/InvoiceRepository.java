package tvservice.repository;

import tvservice.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
    List<Invoice> findByDueDateBeforeAndPaid(LocalDate date, boolean paid);
    List<Invoice> findByPaidFalse();
}
