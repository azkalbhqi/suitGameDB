package com.pemlanjava.rpsgame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Leaderboard extends JFrame {
    private JTable leaderboardTable;
    private DatabaseManager dbManager;

    public Leaderboard() {
        dbManager = new DatabaseManager();

        setTitle("Leaderboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create table model with column names
        String[] columnNames = {"Rank", "Name", "Plays", "Wins"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        leaderboardTable = new JTable(tableModel);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        add(scrollPane, BorderLayout.CENTER);

        // Populate the leaderboard with data from the database
        populateLeaderboard(tableModel);

        setVisible(true);
    }

    private void populateLeaderboard(DefaultTableModel tableModel) {
        List<Object[]> leaderboardData = dbManager.getLeaderboard();
        for (Object[] row : leaderboardData) {
            tableModel.addRow(row);
        }
    }

    public static void main(String[] args) {
        // Run the GUI in the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> new Leaderboard());
    }
}
