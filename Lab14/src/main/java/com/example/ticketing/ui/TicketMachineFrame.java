package com.example.ticketing.ui;

import com.example.ticketing.model.Ticket;
import com.example.ticketing.service.TicketService;

import javax.swing.*;
import java.awt.*;

public class TicketMachineFrame extends JFrame {
    public TicketMachineFrame(TicketService service) {
        super("Biletomat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        var model = new DefaultComboBoxModel<String>();
        service.getCategories().forEach(c -> model.addElement(c.getSymbol()));
        var combo = new JComboBox<>(model);

        var btn = new JButton("Pobierz bilet");
        JLabel label = new JLabel("Twój bilet: ---");

        btn.addActionListener(e -> {
            String sym = (String) combo.getSelectedItem();
            Ticket t = service.issueTicket(sym);
            label.setText("Twój bilet: " + t.id());
        });

        panel.add(new JLabel("Kategoria:"));
        panel.add(combo);
        panel.add(btn);
        panel.add(label);

        add(panel);

        pack();
        setSize(350, getHeight());
        setResizable(true);
        setLocation(100, 100);
        setVisible(true);
    }
}
