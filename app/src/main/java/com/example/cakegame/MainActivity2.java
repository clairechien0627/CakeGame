package com.example.cakegame;

import android.annotation.SuppressLint;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class MainActivity2 extends AppCompatActivity {
    public static CakePane[][] cakes = new CakePane[5][4];
    public static CakeView[][] cakeViews = new CakeView[5][4];
    public static CakePane[] new_cakes = new CakePane[4];
    public static CakeView[] newCakeView = new CakeView[4];
    public final int[][] cakeID = {
            {R.id.cake1, R.id.cake2, R.id.cake3, R.id.cake4},
            {R.id.cake5, R.id.cake6, R.id.cake7, R.id.cake8},
            {R.id.cake9, R.id.cake10, R.id.cake11, R.id.cake12},
            {R.id.cake13, R.id.cake14, R.id.cake15, R.id.cake16},
            {R.id.cake17, R.id.cake18, R.id.cake19, R.id.cake20}
    };
    public final int[] newCakeID = {R.id.cake21, R.id.cake22, R.id.cake23, R.id.cake24};

    public static ScoreBoard scoreBoard;
    public static int totalScore = 0;
    public static int totalFullCakeNum = 0;
    @SuppressLint("StaticFieldLeak")
    public static TextView score;
    @SuppressLint("StaticFieldLeak")
    public static TextView fullCake;
    private static int width;
    private static int height;
    private int originalIndex;
    public static SoundPlay soundPlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initializeGame(); // 初始化遊戲
    }

    // 初始化遊戲的設置
    private void initializeGame() {
        //設置音效
        soundPlay = new SoundPlay(this);
        //設置分數
        scoreBoard = new ScoreBoard();
        score = findViewById(R.id.totalScore);
        fullCake = findViewById(R.id.totalFullCakeNum);

        // 使用 View.post() 方法确保在布局绘制完成后执行初始化操作
        findViewById(R.id.cake1).post(() -> {
            // 获取控件的宽度和高度
            CakeView cakeSize = findViewById(R.id.cake1);
            width = cakeSize.getWidth();
            height = cakeSize.getHeight();
            Log.d("CakeSort", width + " " + height);

            // 初始化table陣列
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 4; j++) {
                    cakes[i][j] = new CakePane();
                    cakeViews[i][j] = findViewById(cakeID[i][j]);
                    cakeViews[i][j].setOnDragListener(new MyDragListener());
                    cakeViews[i][j].setCakePane(cakes[i][j]);
                    cakeViews[i][j].setSize(height, width);
                }
            }
            // 初始化newTable陣列
            for (int i = 0; i < 4; i++) {
                new_cakes[i] = new CakePane();
                new_cakes[i].refresh();
                newCakeView[i] = findViewById(newCakeID[i]);
                newCakeView[i].setOnTouchListener(new MyTouchListener());
                newCakeView[i].setCakePane(new_cakes[i]);
                newCakeView[i].setSize(height, width);
            }
            notEmpty();
            getTable();
        });
    }

    // 重置可拖曳的蛋糕
    @SuppressLint("ClickableViewAccessibility")
    private void resetDraggableCakes() {
        ViewGroup parent = findViewById(R.id.newCakeContainer);
        parent.removeAllViews();

        for (int i = 0; i < 4; i++) {
            // 创建新的 CakeView
            newCakeView[i] = new CakeView(this);
            newCakeView[i].setId(newCakeID[i]);

            newCakeView[i].setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1
            ));
            newCakeView[i].setImageResource(R.drawable.stroke_circle);
            newCakeView[i].setOnTouchListener(new MyTouchListener());

            parent.addView(newCakeView[i]);

            new_cakes[i] = new CakePane();
            new_cakes[i].refresh();
            newCakeView[i].setCakePane(new_cakes[i]);
            newCakeView[i].setSize(height, width);
            newCakeView[i].animateAddCake();
        }

        getTable();
    }

    // 自定義觸摸監聽器類別
    class MyTouchListener implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                originalIndex = ((ViewGroup) v.getParent()).indexOfChild(v);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);

                // 创建一个空的 ClipData 对象，避免 NullPointerException
                ClipData clipData = ClipData.newPlainText("", "");

                // 针对 API 24 以下版本使用 startDrag
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    v.startDragAndDrop(clipData, shadowBuilder, v, View.DRAG_FLAG_GLOBAL);
                } else {
                    v.startDrag(clipData, shadowBuilder, v, 0);
                }

                v.setVisibility(View.INVISIBLE); // 設置拖曳時不可見
                return true;
            } else {
                return false;
            }
        }
    }

    // 自定義拖曳監聽器類別
    class MyDragListener implements View.OnDragListener {

        private ViewGroup originalParent= findViewById(R.id.newCakeContainer);
        private boolean dropped = false;

        @SuppressLint({"UseCompatLoadingForDrawables", "ClickableViewAccessibility"})
        @Override
        public boolean onDrag(View v, DragEvent event) {
            CakeView cakeView = (CakeView) v;
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    cakeView.setImageResource(R.drawable.destination_circle);

                    soundPlay.getSound("choose");
                    notEmpty();
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    cakeView.setImageResource(R.drawable.stroke_circle);
                    notEmpty();
                    break;
                case DragEvent.ACTION_DROP:
                    CakeView draggedView = (CakeView) event.getLocalState();
                    ViewGroup draggedViewParent = (ViewGroup) draggedView.getParent();
                    int draggedViewIndex = 0;

                    for (int i = 0; i < 4; i++) {
                        if (draggedView.getId() == newCakeID[i]) {
                            draggedViewIndex = i;
                            break;
                        }
                    }

                    Log.d("CakeSort", String.valueOf(cakeView.getId()));
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (cakeView.getId() == cakeID[i][j] && cakes[i][j].getPieces().isEmpty() && !cakeViews[i][j].onAnimation()) {
                                new_cakes[draggedViewIndex].put_cake_to_table(cakes, cakeViews, i, j);
                                Log.d("CakeSort", "蛋糕放置在: (" + i + ", " + j + ")");
                                dropped = true;
                                updateScoreAndFullCake(cakes[i][j]);
                                notEmpty();
                                getTable();
                                break;
                            }
                        }
                        if (dropped) {
                            break;
                        }
                    }
                    draggedViewParent.removeView(draggedView);
                    if (!dropped) {
                        originalParent.addView(draggedView, originalIndex);
                        draggedView.setVisibility(View.VISIBLE);
                        draggedView.setOnTouchListener(new MyTouchListener());
                    }

                    boolean reset = true;
                    for(int i = 0; i < 4; i++){
                        if (!new_cakes[i].getPieces().isEmpty()) {
                            reset = false;
                            break;
                        }
                    }
                    if (reset) {
                        resetDraggableCakes();
                        Log.d("CakeSort", "resetDraggableCakes called");
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (!dropped) {
                        View draggedViewEnded = (View) event.getLocalState();
                        draggedViewEnded.setVisibility(View.VISIBLE);
                    }
                    cakeView.setImageResource(R.drawable.stroke_circle);
                    notEmpty();
                    dropped = false;
                    break;
            }
            return true;
        }
    }





    // 獲取當前的蛋糕格子狀態
    private void getTable() {
        StringBuilder tableString = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                tableString.append(cakes[i][j].getPieces()).append(" ");
            }
            tableString.append("\n");
        }
        StringBuilder newTableString = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            newTableString.append(new_cakes[i].getPieces()).append(" ");
        }
        // 記錄 table 字串
        Log.d("CakeSort", "Table:\n" + tableString);
        Log.d("CakeSort", "\nnewTable:\n" + newTableString);
    }

    // 檢查是否所有格子都不空
    private void notEmpty(){
        boolean allNotEmpty = true;
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 4; j++){
                if(cakes[i][j].getPieces().isEmpty()){
                    allNotEmpty = false;
                } else {
                    cakeViews[i][j].setImageResource(R.drawable.pieces_circle);
                }
            }
        }
        if (allNotEmpty) {
            soundPlay.getSound("end");
            Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
            startActivity(intent);
        }
    }

    private void updateScoreAndFullCake(CakePane currentCake) {
        scoreBoard.getCurrentScore().addScore(currentCake.getScore());
        totalScore = scoreBoard.getCurrentScore().getScore();
        if(currentCake.getFullCakeNum() > 0) {
            scoreBoard.getCurrentScore().addFullCake(currentCake.getFullCakeNum());
            totalFullCakeNum = scoreBoard.getCurrentScore().getFullCake();
        }
        score.setText("Total Score : " + totalScore);
        fullCake.setText("Full Cake : " + totalFullCakeNum);
    }
}
