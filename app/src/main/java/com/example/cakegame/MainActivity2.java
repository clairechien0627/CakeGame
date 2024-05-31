package com.example.cakegame;

import android.annotation.SuppressLint;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    // 常量: 行數和列數
    private final int NUM_ROWS = 5;
    private final int NUM_COLS = 4;

    // 2D數組保存蛋糕信息
    private CakePane[][] cakes = new CakePane[NUM_ROWS][NUM_COLS];
    private CakeView[][] cakeViews = new CakeView[NUM_ROWS][NUM_COLS];

    // 保存新的蛋糕信息
    private CakePane[] newCakes = new CakePane[NUM_COLS];
    private CakeView[] newCakeViews = new CakeView[NUM_COLS];

    // 各個蛋糕視圖的ID
    private final int[][] CAKE_ID = {
            {R.id.cake1, R.id.cake2, R.id.cake3, R.id.cake4},
            {R.id.cake5, R.id.cake6, R.id.cake7, R.id.cake8},
            {R.id.cake9, R.id.cake10, R.id.cake11, R.id.cake12},
            {R.id.cake13, R.id.cake14, R.id.cake15, R.id.cake16},
            {R.id.cake17, R.id.cake18, R.id.cake19, R.id.cake20}
    };

    // 新蛋糕的ID
    private final int[] NEW_CAKE_ID = {R.id.cake21, R.id.cake22, R.id.cake23, R.id.cake24};

    // 記分板和分數相關變量
    private ScoreBoard scoreBoard;

    //難度TextView
    @SuppressLint("StaticFieldLeak")
    private TextView difficultyTextView;
    private final String[] difficulty = {"Easy", "Normal", "Hard", "Devil"};

    // 記分板顯示的TextView
    @SuppressLint("StaticFieldLeak")
    private TextView scoreTextView;
    @SuppressLint("StaticFieldLeak")
    private TextView fullCakeTextView;

    // 隨機掉落蛋糕的視圖
    private RandomFallCake randomFallCakeView;

    // 保存視圖的寬高
    private int width;
    private int height;

    // 原始拖動蛋糕的索引
    private static int originalIndex;

    // 遊戲結束標誌
    private boolean endGame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // 初始化遊戲和背景
        initializeGame();
        initializeBackground();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializeGame() {
        // 等待視圖佈局完成後獲取寬高
        findViewById(R.id.cake1).post(() -> {
            CakeView cakeSize = findViewById(R.id.cake1);
            width = cakeSize.getWidth();
            height = cakeSize.getHeight();
            Log.d("CakeSort", width + " " + height);

            // 初始化蛋糕數組和視圖
            initializeCakeArrays();
            // 初始化新蛋糕
            initializeNewCakes();
            // 設置記分板
            setupScoreBoard();
            // 更新表格狀態
            updateTableStatus();
        });
    }

    // 初始化蛋糕數組和視圖
    private void initializeCakeArrays() {
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                cakes[i][j] = new CakePane();
                cakeViews[i][j] = findViewById(CAKE_ID[i][j]);
                cakeViews[i][j].setOnDragListener(new MyDragListener());
                cakeViews[i][j].setCakePane(cakes[i][j]);
                cakeViews[i][j].setSize(height, width);
            }
        }
    }

    // 初始化新的蛋糕
    @SuppressLint("ClickableViewAccessibility")
    private void initializeNewCakes() {
        for (int i = 0; i < NUM_COLS; i++) {
            newCakes[i] = new CakePane();
            newCakes[i].refresh();
            newCakeViews[i] = findViewById(NEW_CAKE_ID[i]);
            newCakeViews[i].setOnTouchListener(new MyTouchListener());
            newCakeViews[i].setCakePane(newCakes[i]);
            newCakeViews[i].setSize(height, width);
        }
    }

    // 設置記分板
    private void setupScoreBoard() {
        scoreBoard = new ScoreBoard(CakePane.getMode());
        scoreTextView = findViewById(R.id.totalScore);
        fullCakeTextView = findViewById(R.id.totalFullCakeNum);
    }

    // 初始化背景相關視圖和選擇器
    private void initializeBackground() {
        randomFallCakeView = findViewById(R.id.random_fall_cake_view);
        //遊戲難度顯示4
        difficultyTextView = findViewById(R.id.difficultyMode);
        difficultyTextView.setText(difficulty[CakePane.getMode()]);

        // 自定義選擇器
        CustomSelector selectDialog1 = findViewById(R.id.selectDialog1);

        selectDialog1.setListener(new CustomSelector.IconSelectListener() {
            @Override
            public void onOpen() {
                Log.d("CakeSort", "open");
                VibrationHelper.vibrate();
            }

            @Override
            public void onCancel() {
                VibrationHelper.vibrate();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void resetDraggableCakes() {
        // 重置可拖動的蛋糕視圖
        ViewGroup parent = findViewById(R.id.newCakeContainer);
        parent.removeAllViews();

        for (int i = 0; i < NUM_COLS; i++) {
            newCakeViews[i] = createNewCakeView(i);
            parent.addView(newCakeViews[i]);
        }

        // 更新表格狀態
        updateTableStatus();
    }

    // 創建新的蛋糕視圖
    @SuppressLint("ClickableViewAccessibility")
    private CakeView createNewCakeView(int index) {
        CakeView newCakeView = new CakeView(this);
        newCakeView.setId(NEW_CAKE_ID[index]);
        newCakeView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        newCakeView.setImageResource(R.drawable.stroke_circle);
        newCakeView.setOnTouchListener(new MyTouchListener());

        newCakes[index] = new CakePane();
        newCakes[index].refresh();
        newCakeView.setCakePane(newCakes[index]);
        newCakeView.setSize(height, width);
        newCakeView.animateAddCake();

        return newCakeView;
    }

    // 觸摸監聽器，處理蛋糕的拖動
    private static class MyTouchListener implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                originalIndex = ((ViewGroup) v.getParent()).indexOfChild(v);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                ClipData clipData = ClipData.newPlainText("", "");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    v.startDragAndDrop(clipData, shadowBuilder, v, View.DRAG_FLAG_GLOBAL);
                } else {
                    v.startDrag(clipData, shadowBuilder, v, 0);
                }
                return true;
            }
            return false;
        }
    }

    // 拖動監聽器，處理蛋糕的放置
    private class MyDragListener implements View.OnDragListener {
        private final ViewGroup originalParent = findViewById(R.id.newCakeContainer);
        private boolean dropped = false;

        @SuppressLint({"UseCompatLoadingForDrawables", "ClickableViewAccessibility"})
        @Override
        public boolean onDrag(View v, DragEvent event) {
            CakeView cakeView = (CakeView) v;
            CakeView draggedView = (CakeView) event.getLocalState();
            if (draggedView == null) return false;

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    handleDragStarted(draggedView);
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    handleDragEntered(cakeView);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    handleDragExited(cakeView);
                    break;
                case DragEvent.ACTION_DROP:
                    handleDrop(cakeView, draggedView);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    handleDragEnded(cakeView, draggedView);
                    break;
            }
            return true;
        }

        // 處理拖動開始
        private void handleDragStarted(CakeView draggedView) {
            draggedView.setVisibility(View.INVISIBLE);
            VibrationHelper.vibrate();
        }

        // 處理拖動進入
        private void handleDragEntered(CakeView cakeView) {
            if (!cakeView.onAnimation()) {
                cakeView.setImageResource(R.drawable.destination_circle);
            }
            VibrationHelper.vibrate();
            SoundPlay.playSound("choose");
        }

        // 處理拖動退出
        private void handleDragExited(CakeView cakeView) {
            cakeView.setImageResource(R.drawable.stroke_circle);
        }

        // 處理蛋糕放置
        private void handleDrop(CakeView cakeView, CakeView draggedView) {
            VibrationHelper.vibrate();
            ViewGroup draggedViewParent = (ViewGroup) draggedView.getParent();
            int draggedViewIndex = getDraggedViewIndex(draggedView);

            for (int i = 0; i < NUM_ROWS; i++) {
                for (int j = 0; j < NUM_COLS; j++) {
                    if (isCakeDropValid(cakeView, i, j)) {
                        performCakeDrop(draggedViewIndex, i, j);
                        dropped = true;
                        break;
                    }
                }
                if (dropped) break;
            }

            finalizeDrop(draggedViewParent, draggedView);
        }

        // 獲取拖動視圖的索引
        private int getDraggedViewIndex(CakeView draggedView) {
            for (int i = 0; i < NUM_COLS; i++) {
                if (draggedView.getId() == NEW_CAKE_ID[i]) {
                    return i;
                }
            }
            return -1;
        }

        // 判斷蛋糕是否可以放置
        private boolean isCakeDropValid(CakeView cakeView, int i, int j) {
            return cakeView.getId() == CAKE_ID[i][j] && cakes[i][j].getPieces().isEmpty() && !cakeViews[i][j].onAnimation();
        }

        // 執行蛋糕放置
        private void performCakeDrop(int draggedViewIndex, int i, int j) {
            newCakes[draggedViewIndex].putCakeToTable(cakes, cakeViews, i, j);
            updateScoreAndFullCake(cakes[i][j]);
            updateTableStatus();
        }

        // 最終處理放置後的操作
        @SuppressLint("ClickableViewAccessibility")
        private void finalizeDrop(ViewGroup draggedViewParent, CakeView draggedView) {
            draggedViewParent.removeView(draggedView);
            if (!dropped) {
                rebuildDragView(draggedView);
            }
            if (draggableCakesAllEmpty()) {
                resetDraggableCakes();
            }
        }

        private void rebuildDragView(CakeView draggedView) {
            originalParent.addView(draggedView, originalIndex);
            draggedView.setVisibility(View.VISIBLE);
            draggedView.setOnTouchListener(new MyTouchListener());
        }

        // 檢查是否需要重置可拖動蛋糕
        private boolean draggableCakesAllEmpty() {
            for (int i = 0; i < NUM_COLS; i++) {
                if (!newCakes[i].getPieces().isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        // 處理拖動結束
        private void handleDragEnded(CakeView cakeView, CakeView draggedView) {
            if (!dropped) {
                draggedView.setVisibility(View.VISIBLE);
            }
            cakeView.setImageResource(R.drawable.stroke_circle);
            checkAllNotEmpty();
            dropped = false;
        }
    }

    // 檢查所有格子是否都不為空
    private void checkAllNotEmpty() {
        if (endGame) return;

        boolean allNotEmpty = true;
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if (cakes[i][j].getPieces().isEmpty()) {
                    allNotEmpty = false;
                    break;
                }
            }
        }

        if (allNotEmpty) {
            endGame = true;
            SoundPlay.playSound("end");
            scoreBoard.addCurrentScore();
            Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
            intent.putExtra("from", "MainActivity2");
            startActivity(intent);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateScoreAndFullCake(CakePane currentCake) {
        // 更新記分板和蛋糕數量
        scoreBoard.getCurrentScore().addScore(currentCake.getScore());
        int totalScore = scoreBoard.getCurrentScore().getScore();
        scoreBoard.getCurrentScore().addFullCake(currentCake.getFullCakeNum());
        int totalFullCakeNum = scoreBoard.getCurrentScore().getFullCake();

        scoreTextView.setText("Total Score : " + totalScore);
        fullCakeTextView.setText("Full Cake : " + totalFullCakeNum);

        for (int i = 0; i < currentCake.getFullCakeNum(); i++) {
            randomFallCakeView.generateNewRandomImage();
        }
    }


    // 更新表格狀態
    private void updateTableStatus() {
        StringBuilder tableString = new StringBuilder();
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                tableString.append(cakes[i][j].getPieces()).append(" ");
            }
            tableString.append("\n");
        }

        StringBuilder newTableString = new StringBuilder();
        for (int i = 0; i < NUM_COLS; i++) {
            newTableString.append(newCakes[i].getPieces()).append(" ");
        }

        Log.d("CakeSort", "Table:\n" + tableString);
        Log.d("CakeSort", "\nnewTable:\n" + newTableString);
    }
}
