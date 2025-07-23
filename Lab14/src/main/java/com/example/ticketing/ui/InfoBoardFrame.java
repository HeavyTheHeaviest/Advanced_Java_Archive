package com.example.ticketing.ui;

import com.example.ticketing.model.Ticket;
import com.example.ticketing.service.TicketService;
import com.example.ticketing.service.TicketServiceListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class InfoBoardFrame extends JFrame implements TicketServiceListener {
    private final TicketService service;
    private final int[] desks;
    private final JTextArea area;

    public InfoBoardFrame(TicketService service, int[] desks) {
        super("Tablica Informacyjna");
        this.service = service;
        this.desks = desks;

        area = new JTextArea(15, 40);
        area.setEditable(false);
        add(new JScrollPane(area), BorderLayout.CENTER);

        pack();
        setLocation(400, 100);
        setVisible(true);

        service.addListener(this);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                service.removeListener(InfoBoardFrame.this);
            }
        });

        refresh();
    }

    private void refresh() {
        StringBuilder sb = new StringBuilder();
        for (int d : desks) {
            Ticket current = service.getCurrentTicket(d);
            sb.append("Stanowisko ").append(d).append(": ")
                    .append(current != null ? current.id() : "BRAK")
                    .append("\n");
        }
        sb.append("\n");
        List<String> all = service.getAllWaitingTickets();
        sb.append("W kolejce: ")
                .append(all.isEmpty() ? "BRAK" : String.join(", ", all));
        area.setText(sb.toString());
    }

    @Override
    public void onQueueChanged() {
        SwingUtilities.invokeLater(this::refresh);
    }
}
