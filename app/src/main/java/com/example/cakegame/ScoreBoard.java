package com.example.cakegame;

import java.util.*;

public class ScoreBoard {

    private static ArrayList<Score> scoreBoard = new ArrayList<>();

    private static int count_num = 0;
    private Score currentScore;


    public ScoreBoard(int score, int full_cake){

        count_num++;

        currentScore = new Score(count_num, score, full_cake);

        scoreBoard.add(currentScore);


    }

    public ArrayList<Score> getScoreBoard_num() {

        Collections.sort(scoreBoard, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                return Integer.compare(o1.getNum(), o2.getNum());
            }
        });

        for(int i=0;i<scoreBoard.size();i++){
            scoreBoard.get(i).setRank(i+1);
        }

        return scoreBoard;
    }



    public ArrayList<Score> getScoreBoard_score() {

        Collections.sort(scoreBoard, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                int result = Integer.compare(o1.getScore(), o2.getScore());
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

            while(scoreBoard.get(count).getScore() == scoreBoard.get(count - 1).getScore()) {
                scoreBoard.get(count).setRank(count_rank + 1);
                count++;
            }

            count_rank++;
        }


        return scoreBoard;
    }



    public ArrayList<Score> getScoreBoard_cake() {

        Collections.sort(scoreBoard, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                int result = Integer.compare(o1.getFull_cake(), o2.getFull_cake());
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

            while(scoreBoard.get(count).getFull_cake() == scoreBoard.get(count - 1).getFull_cake()) {
                scoreBoard.get(count).setRank(count_rank + 1);
                count++;
            }

            count_rank++;
        }

        return scoreBoard;
    }






    public class Score {


        private int score;
        private int num;
        private int full_cake;
        private int rank;


        public Score(int n, int s, int f) {

            num = n;
            score = s;
            full_cake = f;

        }

        public void setRank(int r) {
            rank = r;
        }


        public int getScore() { return score;}

        public int getNum() { return num;}

        public int getFull_cake() { return full_cake;}


    }


}
