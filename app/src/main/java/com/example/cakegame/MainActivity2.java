package com.example.cakegame;

import android.annotation.SuppressLint;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    public static ArrayList<Integer>[][] table = new ArrayList[5][4];
    public static ArrayList<Integer>[] newTable = new ArrayList[4];
    public static View[][] cakeView = new View[5][4];
    public static View[] newCakeView = new View[4];
    public static int[][] cakeID = {{R.id.cake1, R.id.cake2, R.id.cake3, R.id.cake4},
            {R.id.cake5, R.id.cake6, R.id.cake7, R.id.cake8},
            {R.id.cake9, R.id.cake10, R.id.cake11, R.id.cake12},
            {R.id.cake13, R.id.cake14, R.id.cake15, R.id.cake16},
            {R.id.cake17, R.id.cake18, R.id.cake19, R.id.cake20}};
    public static int[] newCakeID = {R.id.cake21, R.id.cake22, R.id.cake23, R.id.cake24};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        for(int[] cakes: cakeID){
            for(int cake: cakes){
                findViewById(cake).setOnDragListener(new MyDragListener());
            }
        }
        for(int cake: newCakeID){
            findViewById(cake).setOnTouchListener(new MyTouchListener());
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                table[i][j] = new ArrayList<>();
            }
        }
        for(int i = 0; i < 4; i++){
            newTable[i] = new ArrayList<>();
            newTable[i].add(i);
        }
    }

    class MyTouchListener implements View.OnTouchListener{
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    v.startDragAndDrop(null, shadowBuilder, v, View.DRAG_FLAG_GLOBAL);
                } else {
                    v.startDrag(null, shadowBuilder, v, 0);
                }
                v.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }


    class MyDragListener implements View.OnDragListener {

        private ViewGroup originalParent; // 原始的父视图
        private int originalIndex; // 原始的索引
        private boolean dropped = false; // 用于标记是否成功放置

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public boolean onDrag(View v, DragEvent event) {
            ImageView imageView = (ImageView) v;
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // 在拖动开始时保存原始位置
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

                    for (int i = 0; i < 4; i++) {
                        if (draggedView.getId() == newCakeID[i]) {
                            draggedViewIndex = i;
                            break;
                        }
                    }

                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (imageView.getId() == cakeID[i][j] && table[i][j].isEmpty()) {
                                table[i][j].addAll(newTable[draggedViewIndex]);
                                System.out.println(table[i][j]);
                                System.out.println(i + " " + j);
                                draggedViewParent.removeView(draggedView);
                                dropped = true;
                                break;
                            }
                        }
                        if (dropped) {
                            break;
                        }
                    }
                    if (!dropped) {
                        if (imageView.getParent() instanceof ViewGroup) {
                            ((ViewGroup) imageView.getParent()).removeView(imageView);
                        }
                        originalParent.addView(imageView, originalIndex);
                        imageView.setVisibility(View.VISIBLE); // 使其可见
                        imageView.setOnTouchListener(new MyTouchListener()); // 重新设置触摸监听器
                    }
                    break;
            }
            return true;
        }
    }
}