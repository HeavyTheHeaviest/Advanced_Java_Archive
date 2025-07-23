package tvservice.service;

import tvservice.model.*;
import tvservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service @RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepo;
    private final SubscriptionRepository subRepo;
    private final SubaccountRepository subAccRepo;

    public Client addClient(String fn, String ln, String num) {
        Client c = new Client();
        c.setFirstName(fn); c.setLastName(ln); c.setClientNumber(num);
        return clientRepo.save(c);
    }

    public List<Client> allClients() {
        return clientRepo.findAll();
    }

    public Subscription addSubscription(Long clientId, SubscriptionType type, LocalDate from) {
        Client c = clientRepo.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono klienta"));
        Subscription s = new Subscription();
        s.setClient(c);
        s.setType(type);
        s.setStartDate(from);
        s.setEndDate(null);
        return subRepo.save(s);
    }

    public Subaccount addSubaccount(Long subId, String login, String pwd) {
        Subscription s = subRepo.findById(subId)
                .orElseThrow(() -> new RuntimeException("Brak subskrypcji"));
        Subaccount sa = new Subaccount();
        sa.setSubscription(s);
        sa.setLogin(login);
        sa.setPassword(pwd);
        return subAccRepo.save(sa);
    }
}
