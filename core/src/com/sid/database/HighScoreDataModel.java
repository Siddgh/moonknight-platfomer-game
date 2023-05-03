package com.sid.database;

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
