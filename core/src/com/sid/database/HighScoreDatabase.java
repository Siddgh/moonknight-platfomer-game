package com.sid.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HighScoreDatabase {
    private Connection connection;

    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:highscores.db");
        createTableIfNotExists();
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS highscores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "score INTEGER)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.execute();
    }

    public void saveHighscore(HighScoreDataModel highscore) throws SQLException {
        String sql = "INSERT INTO highscores (username, score) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, highscore.getUsername());
        statement.setInt(2, highscore.getScore());
        statement.executeUpdate();
    }

    public List<HighScoreDataModel> getHighScores() throws SQLException {
        List<HighScoreDataModel> highscores = new ArrayList<>();

        String sql = "SELECT * FROM highscores ORDER BY score DESC LIMIT 10";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String username = resultSet.getString("username");
            int score = resultSet.getInt("score");
            highscores.add(new HighScoreDataModel(username, score));
        }

        return highscores;
    }
}