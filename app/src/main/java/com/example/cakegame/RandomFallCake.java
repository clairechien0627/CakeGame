package com.example.cakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RandomFallCake extends View {
    private Bitmap[] images = new Bitmap[6];
    private Bitmap[] scaledImage = new Bitmap[6];
    private Random random;
    private int viewWidth;  // 視圖的寬度
    private int viewHeight; // 視圖的高度
    private int imageWidth = 200; // 圖像的寬度
    private int imageHeight = 200; // 圖像的高度

    private List<BitmapPosition> bitmapPositions; // 存儲圖像及其位置

    public RandomFallCake(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RandomFallCake(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        // 獲取螢幕尺寸
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        viewWidth = displayMetrics.widthPixels;
        viewHeight = displayMetrics.heightPixels;

        // 加載圖像
        images[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.cake1);
        images[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.cake2);
        images[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.cake3);
        images[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.cake4);
        images[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.cake5);
        images[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.cake6);

        // 縮放圖像
        for(int i=0;i<6;i++) {
            scaledImage[i] = Bitmap.createScaledBitmap(images[i], imageWidth, imageHeight, true);
        }
        random = new Random();

        // 初始化列表
        bitmapPositions = new ArrayList<>();
    }

    private void addNewRandomImage() {
        // 生成隨機位置
        int x = random.nextInt(viewWidth - imageWidth);
        int y = 0; // 從頂部開始
        int randomIndex = random.nextInt(6);
        // 添加到列表
        bitmapPositions.add(new BitmapPosition(scaledImage[randomIndex], x, y, System.currentTimeMillis()));
        // 刷新視圖
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long currentTime = System.currentTimeMillis();
        Iterator<BitmapPosition> iterator = bitmapPositions.iterator();

        while (iterator.hasNext()) {
            BitmapPosition bp = iterator.next();
            long elapsedTime = currentTime - bp.startTime;
            int newY = (int) (bp.y + elapsedTime / 300.0); // 調整速度

            if (newY > viewHeight) {
                // 移出螢幕，移除圖像
                iterator.remove();
            } else {
                bp.y = newY;
                canvas.drawBitmap(bp.bitmap, bp.x, bp.y, null);
            }
        }

        // 再次調用onDraw以繼續動畫
        postInvalidateOnAnimation();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 設置視圖的尺寸
        setMeasuredDimension(viewWidth, viewHeight);
    }

    // 暴露一個公共方法以便外部調用
    public void generateNewRandomImage() {
        addNewRandomImage();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float touchX = event.getX();
            float touchY = event.getY();

            Iterator<BitmapPosition> iterator = bitmapPositions.iterator();
            while (iterator.hasNext()) {
                BitmapPosition bp = iterator.next();
                if (touchX >= bp.x && touchX <= bp.x + imageWidth && touchY >= bp.y && touchY <= bp.y + imageHeight) {
                    // 檢測到點擊在圖像區域內
                    iterator.remove();
                    invalidate();
                    break;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    // 內部類用於存儲圖像及其位置
    private static class BitmapPosition {
        Bitmap bitmap;
        int x, y;
        long startTime;

        BitmapPosition(Bitmap bitmap, int x, int y, long startTime) {
            this.bitmap = bitmap;
            this.x = x;
            this.y = y;
            this.startTime = startTime;
        }
    }
}
