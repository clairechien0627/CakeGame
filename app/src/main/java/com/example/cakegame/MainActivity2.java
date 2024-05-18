package com.example.cakegame;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.content.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.*;



public class MainActivity2 extends AppCompatActivity {

    public static ArrayList<Integer>[][] table = new ArrayList[5][4];
    public static CakePane[][] cakes = new CakePane[5][4];
    public static ArrayList<Integer>[] newTable = new ArrayList[4];
    public static CakePane[] new_cakes = new CakePane[4];
    public static int[][] cakeID = {
            {R.id.cake1, R.id.cake2, R.id.cake3, R.id.cake4},
            {R.id.cake5, R.id.cake6, R.id.cake7, R.id.cake8},
            {R.id.cake9, R.id.cake10, R.id.cake11, R.id.cake12},
            {R.id.cake13, R.id.cake14, R.id.cake15, R.id.cake16},
            {R.id.cake17, R.id.cake18, R.id.cake19, R.id.cake20}
    };
    public int[] newCakeID = {R.id.cake21, R.id.cake22, R.id.cake23, R.id.cake24};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initializeGame();
    }

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
            Random random = new Random();
            int randnum = random.nextInt(6)+1;
            ImageView plane = findViewById(cake);
            int drawableResource = 0;
            switch (randnum){
                case 1 : drawableResource = R.drawable.cake_1;break;
                case 2 : drawableResource = R.drawable.cake_2;break;
                case 3 : drawableResource = R.drawable.cake_3;break;
                case 4 : drawableResource = R.drawable.cake_4;break;
                case 5 : drawableResource = R.drawable.cake_5;break;
                case 6 : drawableResource = R.drawable.cake_6;break;
                default: throw new IllegalStateException("l");
            }
            Drawable drawable = getBaseContext().getResources().getDrawable(drawableResource);
            plane.setImageDrawable(drawable);
        }

        // 初始化table陣列
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                table[i][j] = new ArrayList<>();
                cakes[i][j] = new CakePane();
                table[i][j] = cakes[i][j].getPieces();
            }
        }
        // 初始化newTable陣列
        for (int i = 0; i < 4; i++) {
            new_cakes[i] = new CakePane();
            new_cakes[i].refresh();
            newTable[i] = new_cakes[i].getPieces();
        }
    }

    private void resetDraggableCakes() {
        // 清空 newTable 並重新添加元素
        for (int i = 0; i < 4; i++) {
            new_cakes[i] = new CakePane();
            new_cakes[i].refresh();
            newTable[i]= new_cakes[i].getPieces();
        }

        // 找到放置新蛋糕的父視圖
        ViewGroup parent = findViewById(R.id.newCakeContainer);

        // 清除現有的蛋糕視圖
        parent.removeAllViews();

        // 重新創建可拖曳的蛋糕
        for (int i = 0; i < 4; i++) {
            ImageView newCake = new ImageView(this);
            newCake.setId(newCakeID[i]); // 使用舊的ID
            newCake.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1
            ));
            newCake.setPadding(0, 0, 10, 0); // 添加右邊距，使得蛋糕之間有間隔
            newCake.setImageResource(R.drawable.destination_circle);
            newCake.setContentDescription(getString(R.string.cake_description));
            newCake.setOnTouchListener(new MyTouchListener());
            parent.addView(newCake);
        }
    }

    class MyTouchListener implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                getTable();
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);

                // 创建一个空的 ClipData 对象，避免 NullPointerException
                ClipData clipData = ClipData.newPlainText("", "");

                // 针对 API 24 以下版本使用 startDrag
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    v.startDragAndDrop(clipData, shadowBuilder, v, View.DRAG_FLAG_GLOBAL);
                } else {
                    v.startDrag(clipData, shadowBuilder, v, 0);
                }

                v.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener {

        private ViewGroup originalParent;
        private int originalIndex;
        private boolean dropped = false;

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public boolean onDrag(View v, DragEvent event) {
            ImageView imageView = (ImageView) v;
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (originalParent == null) {
                        originalParent = (ViewGroup) v.getParent();
                        originalIndex = originalParent.indexOfChild(v);
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    imageView.setImageResource(R.drawable.destination_circle);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
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
                                new_cakes[draggedViewIndex].put_cake_to_table(cakes, i, j);
                                Log.d("CakeSort", "蛋糕放置在: (" + i + ", " + j + ")");
                                draggedViewParent.removeView(draggedView);
                                dropped = true;
                                for(int a = 0; a < 5; a++){
                                    for(int b = 0; b <4; b++){
                                        table[a][b] = cakes[a][b].getPieces();
                                    }
                                }
                                getTable();
                                break;
                            }
                        }
                        if (dropped) {
                            break;
                        }
                    }
                    if (!dropped) {
                        originalParent.addView(draggedView, originalIndex);
                        draggedView.setVisibility(View.VISIBLE);
                        draggedView.setOnTouchListener(new MyTouchListener());
                    }

                    // 检查是否四个蛋糕都已放置
                    if (originalParent.getChildCount() == 0) {
                        resetDraggableCakes();
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (!dropped) {
                        View draggedViewEnded = (View) event.getLocalState();
                        draggedViewEnded.setVisibility(View.VISIBLE);
                    }
                    imageView.setImageResource(R.drawable.stroke_circle);
                    // 重置变量
                    originalParent = null;
                    originalIndex = 0;
                    dropped = false;
                    break;
            }
            return true;
        }
    }



    private void getTable(){
        StringBuilder tableString = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                tableString.append(table[i][j]).append(" ");
            }
            tableString.append("\n");
        }
        StringBuilder newTableString = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            tableString.append(newTable[i]).append(" ");
        }
        // 記錄 table 字串
        Log.d("CakeSort", "Table:\n" + tableString.toString());

        Log.d("CakeSort", "\n" + newTableString.toString());
    }
}
