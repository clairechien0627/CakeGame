package com.example.cakegame;

import android.annotation.SuppressLint;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity2 extends AppCompatActivity {
    public static CakePane[][] cakes = new CakePane[5][4];
    public static CakeView[][] cakeView = new CakeView[5][4];
    public static CakePane[] new_cakes = new CakePane[4];
    public static CakeView[] newCakeView = new CakeView[4];
    public static int[][] cakeID = {
            {R.id.cake1, R.id.cake2, R.id.cake3, R.id.cake4},
            {R.id.cake5, R.id.cake6, R.id.cake7, R.id.cake8},
            {R.id.cake9, R.id.cake10, R.id.cake11, R.id.cake12},
            {R.id.cake13, R.id.cake14, R.id.cake15, R.id.cake16},
            {R.id.cake17, R.id.cake18, R.id.cake19, R.id.cake20}
    };
    public int[] newCakeID = {R.id.cake21, R.id.cake22, R.id.cake23, R.id.cake24};
//    public int[] newCakeViewID = {R.id.cake_view21, R.id.cake_view22, R.id.cake_view23, R.id.cake_view24};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initializeGame(); // 初始化遊戲
    }

    // 初始化遊戲的設置
    private void initializeGame() {
        // 設置拖曳監聽器
        for (int[] cakes : cakeID) {
            for (int cake : cakes) {
                findViewById(cake).setOnDragListener(new MyDragListener());
            }
        }
        // 設置觸摸監聽器
        for (int cake : newCakeID) {
            findViewById(cake).setOnTouchListener(new MyTouchListener());
        }

        // 初始化蛋糕格子的陣列
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                cakes[i][j] = new CakePane();
                cakeView[i][j] = findViewById(cakeID[i][j]);
                cakeView[i][j].setCakePane(cakes[i][j]);
            }
        }
        // 初始化新的蛋糕格子的陣列
        for (int i = 0; i < 4; i++) {
            new_cakes[i] = new CakePane();
            new_cakes[i].refresh(); // 刷新蛋糕狀態
            newCakeView[i] = findViewById(newCakeID[i]);
            newCakeView[i].setCakePane(new_cakes[i]);
        }
        notEmpty(); // 檢查是否所有格子都不空
        getTable(); // 獲取當前的蛋糕格子狀態
    }

    // 重置可拖曳的蛋糕
    @SuppressLint("ClickableViewAccessibility")
    private void resetDraggableCakes() {
        ViewGroup parent = findViewById(R.id.newCakeContainer);
        parent.removeAllViews(); // 移除所有子視圖

        for (int i = 0; i < 4; i++) {
            // 创建新的 ConstraintLayout
            ConstraintLayout newConstraintLayout = new ConstraintLayout(this);
            newConstraintLayout.setId(View.generateViewId()); // 为新视图生成一个唯一的ID

            LinearLayout.LayoutParams constraintLayoutParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1
            );
            if (i != 0) {
                // 添加左边距
                constraintLayoutParams.setMarginStart(30);
            }
            newConstraintLayout.setLayoutParams(constraintLayoutParams);

            // 创建新的 CakeView
            newCakeView[i] = new CakeView(this);
            newCakeView[i].setId(newCakeID[i]);

            ConstraintLayout.LayoutParams cakeLayoutParams = new ConstraintLayout.LayoutParams(
                    0,
                    0
            );
            cakeLayoutParams.dimensionRatio = "1:1";
            cakeLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            cakeLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            cakeLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            cakeLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            newCakeView[i].setLayoutParams(cakeLayoutParams);

            newCakeView[i].setBackgroundResource(R.drawable.destination_circle);
            newCakeView[i].setContentDescription(getString(R.string.cake_description));
            newCakeView[i].setOnTouchListener(new MyTouchListener());

            newConstraintLayout.addView(newCakeView[i]);
            parent.addView(newConstraintLayout);

            new_cakes[i] = new CakePane();
            new_cakes[i].refresh();
            newCakeView[i].setCakePane(new_cakes[i]);
        }

        getTable();
    }

    // 自定義觸摸監聽器類別
    class MyTouchListener implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
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

        private ViewGroup originalParent;
//        private int originalIndex;
        private boolean dropped = false;

        @SuppressLint({"UseCompatLoadingForDrawables", "ClickableViewAccessibility"})
        @Override
        public boolean onDrag(View v, DragEvent event) {
            CakeView imageView = (CakeView) v;
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    originalParent = findViewById(R.id.newCakeContainer);

                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    // 當拖曳進入時改變背景
                    imageView.setBackgroundResource(R.drawable.destination_circle);
                    notEmpty();
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    // 當拖曳退出時改變背景
                    imageView.setBackgroundResource(R.drawable.stroke_circle);
                    notEmpty();
                    break;
                case DragEvent.ACTION_DROP:
                    // 當拖曳放置時處理
                    CakeView draggedView = (CakeView) event.getLocalState();
                    ViewGroup draggedViewParent = (ViewGroup) draggedView.getParent();
                    int draggedViewIndex = 0;

                    // 獲取拖曳視圖的索引
                    for (int i = 0; i < 4; i++) {
                        if (draggedView.getId() == newCakeID[i]) {
                            draggedViewIndex = i;
                            break;
                        }
                    }

                    Log.d("CakeSort", String.valueOf(imageView.getId()));
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (imageView.getId() == cakeID[i][j] && cakes[i][j].getPieces().isEmpty()) {
                                new_cakes[draggedViewIndex].put_cake_to_table(cakes, i, j);
                                Log.d("CakeSort", "蛋糕放置在: (" + i + ", " + j + ")");
                                dropped = true;
                                notEmpty();
                                getTable();
                                break;
                            }
                        }
                        if (dropped) {
                            break;
                        }
                    }
                    if (dropped) {
                        // 如果蛋糕成功放置，移除原视图
                        draggedViewParent.removeView(draggedView);
                    } else {
                        // 如果蛋糕未成功放置，还原至原始位置
                        ViewGroup parentLayout = (ViewGroup) draggedViewParent.getParent();
                        parentLayout.removeView(draggedViewParent);
                        originalParent.addView((View) draggedViewParent, draggedViewIndex); // 再添加回原始父视图
                        Log.d("CakeSort", "蛋糕還原在: " + draggedViewIndex);
                        draggedView.setVisibility(View.VISIBLE);
                        draggedView.setOnTouchListener(new MyTouchListener());
                    }

                    // 如果所有可拖曳的視圖已移除，重置蛋糕
                    if (allDraggableViewsRemoved()) {
                        resetDraggableCakes();
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    // 當拖曳結束時處理
                    if (!dropped) {
                        View draggedViewEnded = (View) event.getLocalState();
                        draggedViewEnded.setVisibility(View.VISIBLE);
                    }
                    imageView.setBackgroundResource(R.drawable.stroke_circle);
                    notEmpty();
                    dropped = false;
                    break;
            }
            return true;
        }

        // 檢查是否所有可拖曳的視圖都已移除
        private boolean allDraggableViewsRemoved() {
            ViewGroup parent = findViewById(R.id.newCakeContainer);
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                if (child instanceof ConstraintLayout && ((ConstraintLayout) child).getChildCount() != 0) {
                    return false;
                }
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
    private void notEmpty() {
        boolean allNotEmpty = true;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                if (cakes[i][j].getPieces().isEmpty()) {
                    allNotEmpty = false;
                } else {
                    cakeView[i][j].setBackgroundResource(R.drawable.pieces_circle);
                }
            }
        }
        if (allNotEmpty) {
            // 如果所有格子都不空，跳轉到下一個活動
            Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
            startActivity(intent);
        }
    }
}
