// ConsoleRunner.java
package tvservice;

import tvservice.model.SubscriptionType;
import tvservice.service.*;
import tvservice.billing.BillingService;
import tvservice.util.SimulationClock;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.Scanner;

@Component @RequiredArgsConstructor
public class ConsoleRunner implements CommandLineRunner {
    private final ClientService clientService;
    private final PriceService priceService;
    private final PaymentService paymentService;
    private final BillingService billingService;
    private final SimulationClock clock;
    private Scanner sc = new Scanner(System.in);

    @Override
    public void run(String... args) {
        while (true) {
            System.out.println("\n=== MENU ===\n" +
                    "1) Dodaj klienta\n" +
                    "2) Dodaj subskrypcję\n" +
                    "3) Dodaj subkonto\n" +
                    "4) Dodaj cenę\n" +
                    "5) Generuj faktury\n" +
                    "6) Wyślij monity\n" +
                    "7) Eskalacja\n" +
                    "8) Zarejestruj wpłatę\n" +
                    "9) Pokaż klientów\n" +
                    "10) Pokaż ceny\n" +
                    "11) Pokaż faktury\n" +
                    "12) Pokaż wpłaty\n" +
                    "13) Przesuń czas o dni\n" +
                    "0) Wyjście"
            );
            System.out.print("Wybór: ");
            int opt = Integer.parseInt(sc.nextLine());
            switch(opt) {
                case 1: {
                    System.out.print("Imię: "); String fn=sc.nextLine();
                    System.out.print("Nazwisko: "); String ln=sc.nextLine();
                    System.out.print("Nr klienta: "); String num=sc.nextLine();
                    System.out.println("Utworzono: " + clientService.addClient(fn,ln,num));
                    break;
                }
                case 2: {
                    System.out.print("ID klienta: "); Long cid=Long.parseLong(sc.nextLine());
                    System.out.print("Typ (BASIC, PREMIUM...): ");
                    SubscriptionType t = SubscriptionType.valueOf(sc.nextLine());
                    System.out.print("Data startu (RRRR-MM-DD): ");
                    LocalDate d = LocalDate.parse(sc.nextLine());
                    System.out.println("Subskrypcja: " +
                            clientService.addSubscription(cid,t,d));
                    break;
                }
                case 3: {
                    System.out.print("ID subskrypcji: "); Long sid=Long.parseLong(sc.nextLine());
                    System.out.print("Login: "); String log=sc.nextLine();
                    System.out.print("Hasło: "); String pwd=sc.nextLine();
                    System.out.println("Subkonto: " +
                            clientService.addSubaccount(sid,log,pwd));
                    break;
                }
                case 4: {
                    System.out.print("Typ usługi: "); SubscriptionType t=SubscriptionType.valueOf(sc.nextLine());
                    System.out.print("Cena: "); double a=Double.parseDouble(sc.nextLine());
                    System.out.print("Od kiedy (RRRR-MM-DD): "); LocalDate d=LocalDate.parse(sc.nextLine());
                    System.out.println("Dodano cenę: " + priceService.addPrice(t,a,d));
                    break;
                }
                case 5:
                    billingService.generateMonthlyInvoices();
                    System.out.println("Faktury wygenerowane.");
                    break;
                case 6:
                    billingService.sendReminders();
                    System.out.println("Monity wysłane (zalogowane).");
                    break;
                case 7:
                    billingService.escalate();
                    System.out.println("Eskalacja wykonana (zalogowana).");
                    break;
                case 8: {
                    System.out.print("ID subskrypcji: "); Long sid=Long.parseLong(sc.nextLine());
                    System.out.print("Data wpłaty: "); LocalDate d=LocalDate.parse(sc.nextLine());
                    System.out.print("Kwota: "); double a=Double.parseDouble(sc.nextLine());
                    System.out.println("Wpłata: " + paymentService.registerPayment(sid,d,a));
                    break;
                }
                case 9:
                    clientService.allClients().forEach(System.out::println);
                    break;
                case 10:
                    priceService.allPrices().forEach(System.out::println);
                    break;
                case 11:
                    billingService.invRepo.findAll().forEach(System.out::println);
                    break;
                case 12:
                    paymentService.payRepo.findAll().forEach(System.out::println);
                    break;
                case 13:
                    System.out.print("Ile dni przesunąć? ");
                    int days=Integer.parseInt(sc.nextLine());
                    clock.plusDays(days);
                    System.out.println("Nowa data: " + clock.today());
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("Niepoprawny wybór.");
            }
        }
    }
}
