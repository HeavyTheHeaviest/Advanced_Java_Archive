package com.example.ticketing;

import com.example.ticketing.mbean.TicketingSystemAgent;
import com.example.ticketing.service.TicketService;
import com.example.ticketing.ui.InfoBoardFrame;
import com.example.ticketing.ui.ServiceDeskFrame;
import com.example.ticketing.ui.TicketMachineFrame;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class App {
    public static void main(String[] args) throws Exception {
        // args[0]: lista kategorii w formacie SYMBOL:PRIORYTET,SYMBOL:PRIORYTET,...
        if (args.length < 1) {
            System.err.println("Usage: java -jar ticketing-system.jar SYMBOL:PRIORITY,...");
            System.exit(1);
        }
        TicketService service = new TicketService();
        // utworzenie agenta JMX
        TicketingSystemAgent agent = new TicketingSystemAgent(service);
        // zarejestruj MBean
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("com.example.ticketing:type=TicketingSystemAgent");
        mbs.registerMBean(agent, name);
        // ustaw początkowe kategorie (bez powiadomień)
        String[] defs = args[0].split(",");
        agent.updateCategoriesInternal(defs);
        // domyślnie pozwól na wszystkie kategorie na stanowiskach 1 i 2
        agent.updateDeskCategoriesInternal(1, defs);
        agent.updateDeskCategoriesInternal(2, defs);

        // uruchom GUI
        javax.swing.SwingUtilities.invokeLater(() -> {
            new TicketMachineFrame(service);
            new ServiceDeskFrame(service, 1);
            new ServiceDeskFrame(service, 2);
            new InfoBoardFrame(service, new int[]{1, 2});
        });
    }
}
