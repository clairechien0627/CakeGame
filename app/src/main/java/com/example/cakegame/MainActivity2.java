package com.example.cakegame;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    public static final int x_max = 5;
    public static final int y_max = 4;
    public static final int newCakeNum = 4;

    // 建立用於存放蛋糕的桌子
    public static ArrayList<Integer>[][] table = new ArrayList[x_max][y_max];
    public static ArrayList<Integer>[] newTable = new ArrayList[newCakeNum];
    // 建立用於顯示蛋糕的視圖
    public static View[][] cakeView = new View[x_max][y_max];
    public static View[] newCakeView = new View[newCakeNum];
    // 儲存蛋糕的 ID
    public static final int[][] cakeID = {
            {R.id.cake1, R.id.cake2, R.id.cake3, R.id.cake4},
            {R.id.cake5, R.id.cake6, R.id.cake7, R.id.cake8},
            {R.id.cake9, R.id.cake10, R.id.cake11, R.id.cake12},
            {R.id.cake13, R.id.cake14, R.id.cake15, R.id.cake16},
            {R.id.cake17, R.id.cake18, R.id.cake19, R.id.cake20}
    };
    public static final int[] newCakeID = {R.id.cake21, R.id.cake22, R.id.cake23, R.id.cake24};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // 設置蛋糕的觸摸和拖放事件監聽器
        setupListeners();
        // 初始化蛋糕桌面和新蛋糕區域
        initializeTables();
    }

    // 初始化蛋糕桌面和新蛋糕區域
    private void initializeTables() {
        // 初始化蛋糕桌面
        for (int i = 0; i < x_max; i++) {
            for (int j = 0; j < y_max; j++) {
                table[i][j] = new ArrayList<>();
            }
        }
        // 初始化新蛋糕區域
        for(int i = 0; i < 4; i++){
            newTable[i] = new ArrayList<>();
            newTable[i].add(i);
        }
    }

    // 設置蛋糕的觸摸和拖放事件監聽器
    private void setupListeners() {
        // 為每個蛋糕設置拖放監聽器
        for(int[] cakes: cakeID){
            for(int cake: cakes){
                findViewById(cake).setOnDragListener(new MyDragListener());
            }
        }
        // 為每個新蛋糕設置觸摸監聽器
        for(int cake: newCakeID){
            findViewById(cake).setOnTouchListener(new MyTouchListener());
        }
    }

    // 觸摸監聽器，處理蛋糕的觸摸事件
    class MyTouchListener implements View.OnTouchListener{
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // 開始拖放操作
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    v.startDragAndDrop(null, shadowBuilder, v, View.DRAG_FLAG_GLOBAL);
                } else {
                    v.startDrag(null, shadowBuilder, v, 0);
                }
                v.setVisibility(View.INVISIBLE); // 設置蛋糕視圖不可見
                return true;
            } else {
                return false;
            }
        }
    }

    // 拖放監聽器，處理蛋糕的拖放事件
    class MyDragListener implements View.OnDragListener {
        private ViewGroup originalParent; // 原始的父視圖
        private int originalIndex; // 原始的索引
        private boolean dropped = false; // 用於標記是否成功放置

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public boolean onDrag(View v, DragEvent event) {
            ImageView imageView = (ImageView) v;
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // 在拖動開始時保存原始位置
                    if (imageView.getParent() instanceof ViewGroup) {
                        originalParent = (ViewGroup) imageView.getParent();
                        originalIndex = originalParent.indexOfChild(imageView);
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    imageView.setImageResource(R.drawable.destination_circle);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                case DragEvent.ACTION_DRAG_ENDED:
                    imageView.setImageResource(R.drawable.stroke_circle);
                    break;
                case DragEvent.ACTION_DROP:
                    ImageView draggedView = (ImageView) event.getLocalState();
                    ViewGroup draggedViewParent = (ViewGroup) draggedView.getParent();
                    int draggedViewIndex = 0;

                    // 查找被拖曳視圖在新蛋糕區域中的索引
                    for (int i = 0; i < 4; i++) {
                        if (draggedView.getId() == newCakeID[i]) {
                            draggedViewIndex = i;
                            break;
                        }
                    }

                    // 尋找放置蛋糕的位置並執行放置操作
                    for (int i = 0; i < x_max; i++) {
                        for (int j = 0; j < y_max; j++) {
                            if (imageView.getId() == cakeID[i][j] && table[i][j].isEmpty()) {
                                table[i][j].addAll(newTable[draggedViewIndex]);
                                draggedViewParent.removeView(draggedView);
                                dropped = true;
                                break;
                            }
                        }
                        if (dropped) {
                            break;
                        }
                    }

                    // 如果沒有成功放置，將被拖曳的視圖返回原位置
                    if (!dropped) {
                        if (imageView.getParent() instanceof ViewGroup) {
                            ((ViewGroup) imageView.getParent()).removeView(imageView);
                        }
                        originalParent.addView(imageView, originalIndex);
                        imageView.setVisibility(View.VISIBLE); // 使其可見
                        imageView.setOnTouchListener(new MyTouchListener()); // 重新設置觸摸監聽器
                    }
                    break;
            }
            return true;
        }
    }
}
