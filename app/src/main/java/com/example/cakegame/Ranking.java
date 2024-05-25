package com.example.cakegame;

public class Ranking {
    private int rank;
    private int score;
    private int fullCake;

    public Ranking(int rank, int score, int fullCake) {
        this.rank = rank;
        this.score = score;
        this.fullCake = fullCake;
    }

    public int getRank() {
        return rank;
    }

    public int getScore() {
        return score;
    }

    public int getFullcake() {
        return fullCake;
    }
}