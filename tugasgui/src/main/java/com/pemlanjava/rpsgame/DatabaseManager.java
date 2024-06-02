package com.pemlanjava.rpsgame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://rpsgame_directlymy:516ef5dd603183e673090a827363399c98a4bfb2@dwm.h.filess.io:3307/rpsgame_directlymy";
    private static final String USER = "rpsgame_directlymy";
    private static final String PASS = "516ef5dd603183e673090a827363399c98a4bfb2";

    public DatabaseManager() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public int authenticateUser(String username, String password) {
        String sql = "SELECT id FROM players WHERE name = ? AND password = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int[] getScores(int playerId) {
        int[] scores = {0, 0};
        String sql = "SELECT play, win FROM scores WHERE player_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, playerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    scores[0] = rs.getInt("play");
                    scores[1] = rs.getInt("win");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }

    public void saveScores(int playerId, int play, int win) {
        String sql = "INSERT INTO scores (player_id, play, win) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE play = ?, win = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, playerId);
            pstmt.setInt(2, play);
            pstmt.setInt(3, win);
            pstmt.setInt(4, play);
            pstmt.setInt(5, win);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Object[]> getLeaderboard() {
        List<Object[]> leaderboard = new ArrayList<>();
        String sql = "SELECT players.name, scores.play, scores.win FROM players JOIN scores ON players.id = scores.player_id ORDER BY scores.win DESC";
        String sq = "select distinct p.name as name, s.play as play, s.win as win FROM players p JOIN scores s ON p.id = s.player_id group by name";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            int rank = 1;
            while (rs.next()) {
                leaderboard.add(new Object[]{rank++, rs.getString("name"), rs.getInt("play"), rs.getInt("win")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaderboard;
    }
}
