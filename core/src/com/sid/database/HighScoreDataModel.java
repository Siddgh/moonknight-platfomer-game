package com.sid.database;

/**
 * This class is responsible to create a basic Data Model for HighScore's which is used to save and load data from SQLite
 */
public class HighScoreDataModel {
    private String username;
    private int score;

    public HighScoreDataModel(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }
}
