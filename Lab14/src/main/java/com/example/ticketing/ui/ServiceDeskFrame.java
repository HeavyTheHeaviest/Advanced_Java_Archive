package com.example.ticketing.ui;

import com.example.ticketing.model.Ticket;
import com.example.ticketing.service.TicketService;
import com.example.ticketing.service.TicketServiceListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServiceDeskFrame extends JFrame implements TicketServiceListener {
    private final TicketService service;
    private final int deskId;
    private final JLabel lbl;
    private Ticket currentTicket = null;

    public ServiceDeskFrame(TicketService service, int deskId) {
        super("Stanowisko " + deskId);
        this.service = service;
        this.deskId = deskId;

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JButton btn = new JButton("Zakończ i weź następny");
        lbl = new JLabel("Brak w kolejce");

        btn.addActionListener(e -> {
            Ticket next = service.finishAndFetchNext(deskId);
            currentTicket = next;
            lbl.setText(next != null
                    ? "Obsługuję: " + next.id()
                    : "Brak w kolejce");
        });

        panel.add(btn);
        panel.add(lbl);
        add(panel);

        pack();
        setSize(300, getHeight());
        setResizable(true);
        setLocation(100, 200 + deskId * 150);
        setVisible(true);

        service.addListener(this);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                service.removeListener(ServiceDeskFrame.this);
            }
        });

        SwingUtilities.invokeLater(() -> {
            Ticket next = service.fetchNextWithoutNotify(deskId);
            if (next != null) {
                currentTicket = next;
                lbl.setText("Obsługuję: " + next.id());
            }
        });
    }

    @Override
    public void onQueueChanged() {
        if (currentTicket == null) {
            Ticket next = service.fetchNextWithoutNotify(deskId);
            if (next != null) {
                currentTicket = next;
                SwingUtilities.invokeLater(() ->
                        lbl.setText("Obsługuję: " + next.id())
                );
            }
        }
    }
}
