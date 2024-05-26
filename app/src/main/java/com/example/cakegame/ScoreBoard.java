package com.example.cakegame;

import android.util.Log;

import java.util.*;

public class ScoreBoard {

    private static ArrayList<Score> totalScoreBoard = new ArrayList<>();

    private static int count_num = 0;
    private Score currentScore;


    public ScoreBoard() {}

    public ScoreBoard(int mode){
        count_num++;
        currentScore = new Score(mode, count_num);

    }


    public Score getCurrentScore() {
        return currentScore;
    }

    public void addCurrentScore() {
        totalScoreBoard.add(currentScore);
        Log.d("ScoreBoard", "add score");
    }

    public ArrayList<Score> getScoreBoard_num() { //按編號排序

        Collections.sort(totalScoreBoard, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                return Integer.compare(o1.getNum(), o2.getNum());
            }
        });

        for(int i=0;i<totalScoreBoard.size();i++){
            totalScoreBoard.get(i).setRank(i+1);
        }

        return totalScoreBoard;
    }



    public ArrayList<Score> getScoreBoard_score(int mode) { //按分數排序

        ArrayList<Score> scoreBoard = new ArrayList<>();

        for(Score s : totalScoreBoard) {
            if(s.getMode() == mode) {
                scoreBoard.add(s);
            }
        }

        Collections.sort(scoreBoard, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                int result = Integer.compare(o2.getScore(), o1.getScore());
                if (result == 0) {
                    result = Integer.compare(o1.getNum(), o2.getNum());
                }
                return result;
            }
        });


        int count = 0;
        int count_rank = 0;
        for(int i=0;i<scoreBoard.size();i++){
            if(count > count_rank){
                continue;
            }

            scoreBoard.get(count).setRank(count_rank + 1);
            count++;

            while(count < scoreBoard.size() && scoreBoard.get(count).getScore() == scoreBoard.get(count - 1).getScore()) {
                scoreBoard.get(count).setRank(count_rank + 1);
                count++;
            }

            count_rank++;
        }


        return scoreBoard;
    }



    public ArrayList<Score> getScoreBoard_cake(int mode) { //按蛋糕數量排序

        ArrayList<Score> scoreBoard = new ArrayList<>();

        for(Score s : totalScoreBoard) {
            if(s.getMode() == mode) {
                scoreBoard.add(s);
            }
        }

        Collections.sort(totalScoreBoard, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                int result = Integer.compare(o2.getFullCake(), o1.getFullCake());
                if (result == 0) {
                    result = Integer.compare(o1.getNum(), o2.getNum());
                }
                return result;
            }
        });

        int count = 0;
        int count_rank = 0;
        for(int i=0;i<scoreBoard.size();i++){
            if(count > count_rank){
                continue;
            }

            scoreBoard.get(count).setRank(count_rank + 1);
            count++;

            while(count < scoreBoard.size() && scoreBoard.get(count).getScore() == scoreBoard.get(count - 1).getScore()) {
                scoreBoard.get(count).setRank(count_rank + 1);
                count++;
            }

            count_rank++;
        }


        return scoreBoard;
    }






    public class Score {

        private int mode;
        private int num;
        private int score;
        private int full_cake;
        private int rank;


        public Score(int m, int n) {
            mode = m;
            num = n;
            score = 0;
            full_cake = 0;
        }

        public void addScore(int s) {
            score += s;
        }

        public void addFullCake(int f) {
            full_cake += f;
        }

        public void setRank(int r) {
            rank = r;
        }

        public int getMode() { return mode;}

        public int getNum() { return num;}

        public int getScore() { return score;}

        public int getFullCake() { return full_cake;}

        public int getRank() { return rank;}



    }


}
