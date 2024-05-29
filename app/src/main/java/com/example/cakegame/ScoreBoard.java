package com.example.cakegame;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ScoreBoard {

    private static ArrayList<Score> totalScoreBoard = new ArrayList<>(); // 儲存所有玩家的分數資料
    private static int countNum = 0; // 計數，用於分配每個分數資料的唯一編號

    private Score currentScore; // 目前的遊戲分數

    public ScoreBoard() {}

    // 建構函式，初始化新的遊戲分數
    public ScoreBoard(int mode){
        countNum++;
        currentScore = new Score(mode);
    }

    // 取得目前的遊戲分數
    public Score getCurrentScore() {
        return currentScore;
    }

    // 新增目前的遊戲分數到總分排行榜中
    public void addCurrentScore() {
        countNum++;
        totalScoreBoard.add(currentScore);
        currentScore.setNum(countNum);
        Log.d("ScoreBoard", "add score");
    }

    // 取得按編號排序的分數排行榜
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<Score> getScoreBoard_num() {
        Collections.sort(totalScoreBoard, Comparator.comparingInt(Score::getNum));

        // 更新排名
        for (int i = 0; i < totalScoreBoard.size(); i++) {
            totalScoreBoard.get(i).setRank(i + 1);
        }

        return totalScoreBoard;
    }

    // 取得按分數排序的分數排行榜
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<Score> getScoreBoard_score(int mode) {
        ArrayList<Score> scoreBoard = new ArrayList<>();

        // 篩選指定模式的分數資料
        for (Score s : totalScoreBoard) {
            if (s.getMode() == mode) {
                scoreBoard.add(s);
            }
        }

        // 根據分數和蛋糕數量排序
        scoreBoard.sort((o1, o2) -> {
            int result = Integer.compare(o2.getScore(), o1.getScore());
            if (result == 0) {
                result = Integer.compare(o2.getFullCake(), o1.getFullCake());
            }
            return result;
        });

        // 更新排名
        int count = 0;
        int countRank = 0;
        for (int i = 0; i < scoreBoard.size(); i++) {
            if (count > countRank) {
                continue;
            }

            scoreBoard.get(count).setRank(countRank + 1);
            count++;

            while (count < scoreBoard.size() && scoreBoard.get(count).getScore() == scoreBoard.get(count - 1).getScore()) {
                scoreBoard.get(count).setRank(countRank + 1);
                count++;
            }

            countRank++;
        }

        return scoreBoard;
    }

    // 取得按蛋糕數量排序的分數排行榜
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<Score> getScoreBoard_cake(int mode) {
        ArrayList<Score> scoreBoard = new ArrayList<>();

        // 篩選指定模式的分數資料
        for (Score s : totalScoreBoard) {
            if (s.getMode() == mode) {
                scoreBoard.add(s);
            }
        }

        // 根據蛋糕數量和分數排序
        scoreBoard.sort((o1, o2) -> {
            int result = Integer.compare(o2.getFullCake(), o1.getFullCake());
            if (result == 0) {
                result = Integer.compare(o2.getScore(), o1.getScore());
            }
            return result;
        });

        // 更新排名
        int count = 0;
        int countRank = 0;
        for (int i = 0; i < scoreBoard.size(); i++) {
            if (count > countRank) {
                continue;
            }

            scoreBoard.get(count).setRank(countRank + 1);
            count++;

            while (count < scoreBoard.size() && scoreBoard.get(count).getFullCake() == scoreBoard.get(count - 1).getFullCake()) {
                scoreBoard.get(count).setRank(countRank + 1);
                count++;
            }

            countRank++;
        }

        return scoreBoard;
    }

    // 定義遊戲分數的類別
    public static class Score {

        private final int mode; // 分數對應的遊戲模式
        private int num; // 分數的唯一編號
        private int score; // 分數
        private int fullCake; // 蛋糕數量
        private int rank; // 排名

        // 建構函式，初始化遊戲分數的屬性
        public Score(int mode) {
            this.mode = mode;
            score = 0;
            fullCake = 0;
        }

        // 增加分數
        public void addScore(int score) {
            this.score += score;
        }

        // 增加蛋糕數量
        public void addFullCake(int fullCake) {
            this.fullCake += fullCake;
        }

        // 設定排名
        public void setRank(int rank) {
            this.rank = rank;
        }

        // 設定分數的唯一編號
        public void setNum(int num) {
            this.num = num;
        }

        // 取得遊戲模式
        public int getMode() {
            return mode;
        }

        // 取得分數的唯一編號
        public int getNum() {
            return num;
        }

        // 取得分數
        public int getScore() {
            return score;
        }

        // 取得蛋糕
        // 取得蛋糕數量
        public int getFullCake() {
            return fullCake;
        }

        // 取得排名
        public int getRank() {
            return rank;
        }
    }
}

