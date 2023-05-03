package com.sid.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * This class takes care of
 * 1. Connecting to SQLite Database
 * 2. Creating a HighScore table
 * 3. Creating methods that would read and write data from the dataed
 */
public class HighScoreDatabase {
    private Connection connection;

    // JDBC to SQLite Connection
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

    // This method would save new highscores and username to the SQLite Database
    public void saveHighscore(HighScoreDataModel highscore) throws SQLException {
        String sql = "INSERT INTO highscores (username, score) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, highscore.getUsername());
        statement.setInt(2, highscore.getScore());
        statement.executeUpdate();
    }

    // This method will read top 5 high scores with the usernames and return a List<> of all the highscores.
    public List<HighScoreDataModel> getHighScores() throws SQLException {
        List<HighScoreDataModel> highscores = new ArrayList<>();

        String sql = "SELECT * FROM highscores ORDER BY score DESC LIMIT 5";
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