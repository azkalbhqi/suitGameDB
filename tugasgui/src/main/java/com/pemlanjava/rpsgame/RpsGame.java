package com.pemlanjava.rpsgame;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class RpsGame extends JFrame implements ActionListener {
    private JButton batuBtn, kertasBtn, guntingBtn, saveBtn, leaderboardBtn;
    private JLabel playerLabel, komLabel, hasiLabel;
    private JLabel pScoreLabel, comScoreLabel;
    private int playerScore, computerScore;
    private int playerId;
    private DatabaseManager dbManager;

    public RpsGame(int playerId) {
        this.playerId = playerId;
        this.dbManager = new DatabaseManager();
        int[] scores = dbManager.getScores(playerId);
        playerScore = scores[1]; // win count
        computerScore = scores[0] - scores[1]; // play count - win count

        setTitle("Game Suitt");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        playerLabel = new JLabel("Kamu pilih: ");
        playerLabel.setHorizontalTextPosition(JLabel.CENTER);
        playerLabel.setVerticalTextPosition(JLabel.TOP);
        playerLabel.setOpaque(true);
        playerLabel.setBackground(Color.WHITE);

        komLabel = new JLabel("Komputer pilih: ");
        komLabel.setHorizontalTextPosition(JLabel.CENTER);
        komLabel.setVerticalTextPosition(JLabel.TOP);
        komLabel.setOpaque(true);
        komLabel.setBackground(Color.WHITE);

        hasiLabel = new JLabel("", SwingConstants.CENTER);
        hasiLabel.setOpaque(true);
        hasiLabel.setBackground(Color.WHITE);

        JPanel choicesPanel = new JPanel();
        choicesPanel.setLayout(new GridBagLayout());
        choicesPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        choicesPanel.add(hasiLabel, gbc);

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        choicesPanel.add(playerLabel, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.WEST;
        choicesPanel.add(komLabel, gbc);

        add(choicesPanel, BorderLayout.CENTER);

        // Buttons
        batuBtn = new JButton("Batu");
        kertasBtn = new JButton("Kertas");
        guntingBtn = new JButton("Gunting");
        saveBtn = new JButton("Save Score");
        leaderboardBtn = new JButton("Leaderboard");

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.setBorder(new EmptyBorder(0, 0, 50, 0));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.add(batuBtn);
        buttonsPanel.add(guntingBtn);
        buttonsPanel.add(kertasBtn);
        JPanel btnbawah = new JPanel();
        btnbawah.setLayout(new FlowLayout());
        btnbawah.add(saveBtn);
        buttonsPanel.add(btnbawah);
        btnbawah.add(leaderboardBtn);  

        
        add(buttonsPanel, BorderLayout.SOUTH);

        // Add action listeners
        batuBtn.addActionListener(this);
        kertasBtn.addActionListener(this);
        guntingBtn.addActionListener(this);
        saveBtn.addActionListener(e -> saveScores());
        leaderboardBtn.addActionListener(e -> openLeaderboard());  

        // Score labels
        pScoreLabel = new JLabel("Skor Kamu: " + playerScore);
        comScoreLabel = new JLabel("Skor Komputer: " + computerScore);

        JPanel scorePanel = new JPanel(new BorderLayout());
        scorePanel.setBackground(Color.WHITE);
        scorePanel.setBorder(new EmptyBorder(100, 100, 0, 100));
        scorePanel.add(pScoreLabel, BorderLayout.WEST);
        scorePanel.add(comScoreLabel, BorderLayout.EAST);

        add(scorePanel, BorderLayout.NORTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String userChoice = e.getActionCommand();

        switch (userChoice) {
            case "Batu":
                playerLabel.setIcon(new ImageIcon(getClass().getResource("/assets/urock.gif")));
                break;
            case "Kertas":
                playerLabel.setIcon(new ImageIcon(getClass().getResource("/assets/upaper.gif")));
                break;
            case "Gunting":
                playerLabel.setIcon(new ImageIcon(getClass().getResource("/assets/uscissors.gif")));
                break;
        }

        String computerChoice = getComputerChoice();

        switch (computerChoice) {
            case "Batu":
                komLabel.setIcon(new ImageIcon(getClass().getResource("/assets/crock.gif")));
                break;
            case "Kertas":
                komLabel.setIcon(new ImageIcon(getClass().getResource("/assets/cpaper.gif")));
                break;
            case "Gunting":
                komLabel.setIcon(new ImageIcon(getClass().getResource("/assets/cscissors.gif")));
                break;
        }

        String result = determineWinner(userChoice, computerChoice);
        hasiLabel.setText(result);

        // Update scores
        if (result.equals("Kamu Menang!!")) {
            playerScore++;
        } else if (result.equals("Wkwk Kalah")) {
            computerScore++;
        }
        pScoreLabel.setText("Skor Kamu: " + playerScore);
        comScoreLabel.setText("Skor Komputer: " + computerScore);
    }

    private void saveScores() {
        dbManager.saveScores(playerId, playerScore + computerScore, playerScore);
        JOptionPane.showMessageDialog(this, "Scores saved successfully!");
    }

    private void openLeaderboard() {
        Leaderboard leaderboardGUI = new Leaderboard();
        leaderboardGUI.setVisible(true);
    }

    private String getComputerChoice() {
        String[] choices = {"Batu", "Kertas", "Gunting"};
        Random random = new Random();
        int index = random.nextInt(choices.length);
        return choices[index];
    }

    private String determineWinner(String userChoice, String computerChoice) {
        if (userChoice.equals(computerChoice)) {
            return "Seri Brow!";
        } else if ((userChoice.equals("Batu") && computerChoice.equals("Gunting")) ||
                   (userChoice.equals("Kertas") && computerChoice.equals("Batu")) ||
                   (userChoice.equals("Gunting") && computerChoice.equals("Kertas"))) {
            return "Kamu Menang!!";
        } else {
            return "Wkwk Kalah";
        }
    }
}
