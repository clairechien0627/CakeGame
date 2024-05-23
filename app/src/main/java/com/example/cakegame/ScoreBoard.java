package com.example.cakegame;

import java.util.*;

public class ScoreBoard {

    private static ArrayList<Score> totalScoreBoard = new ArrayList<>();

    private static int count_num = 0;
    private Score currentScore;


    public ScoreBoard(){
        count_num++;
        currentScore = new Score(count_num);
    }

    public Score getCurrentScore() {
        return currentScore;
    }

    public void addCurrentScore() {
        totalScoreBoard.add(currentScore);
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



    public ArrayList<Score> getScoreBoard_score() { //按分數排序

        Collections.sort(totalScoreBoard, new Comparator<Score>() {
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
        for(int i=0;i<totalScoreBoard.size();i++){
            if(count > count_rank){
                continue;
            }

            totalScoreBoard.get(count).setRank(count_rank + 1);
            count++;

            while(count < totalScoreBoard.size() && totalScoreBoard.get(count).getScore() == totalScoreBoard.get(count - 1).getScore()) {
                totalScoreBoard.get(count).setRank(count_rank + 1);
                count++;
            }

            count_rank++;
        }


        return totalScoreBoard;
    }



    public ArrayList<Score> getScoreBoard_cake() { //按蛋糕數量排序

        Collections.sort(totalScoreBoard, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                int result = Integer.compare(o1.getFullCake(), o2.getFullCake());
                if (result == 0) {
                    result = Integer.compare(o1.getNum(), o2.getNum());
                }
                return result;
            }
        });

        int count = 0;
        int count_rank = 0;
        for(int i=0;i<totalScoreBoard.size();i++){
            if(count > count_rank){
                continue;
            }

            totalScoreBoard.get(count).setRank(count_rank + 1);
            count++;

            while(count < totalScoreBoard.size() && totalScoreBoard.get(count).getFullCake() == totalScoreBoard.get(count - 1).getFullCake()) {
                totalScoreBoard.get(count).setRank(count_rank + 1);
                count++;
            }

            count_rank++;
        }

        return totalScoreBoard;
    }






    public class Score {


        private int score;
        private int num;
        private int full_cake;
        private int rank;


        public Score(int n) {
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


        public int getScore() { return score;}

        public int getNum() { return num;}

        public int getFullCake() { return full_cake;}


    }


}
