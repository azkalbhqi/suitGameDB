package com.pemlanjava.rpsgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private DatabaseManager dbManager;

    public LoginWindow() {
        dbManager = new DatabaseManager();

        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(60, 63, 65));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 1, 10, 10));
        inputPanel.setBackground(new Color(60, 63, 65));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        inputPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));
        inputPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        inputPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        inputPanel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        loginButton.setPreferredSize(new Dimension(100, 40));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(60, 63, 65));
        buttonPanel.add(loginButton);

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        int playerId = dbManager.authenticateUser(username, password);

        if (playerId != -1) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            // Proceed to the game window
            RpsGame game = new RpsGame(playerId);
            game.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginWindow().setVisible(true);
        });
    }
}
