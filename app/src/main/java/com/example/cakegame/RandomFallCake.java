package com.example.cakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RandomFallCake extends View {
    private Bitmap[] images = new Bitmap[6];
    private Bitmap[] scaledImage = new Bitmap[6];
    private Random random;
    private int viewWidth;  // 视图的宽度
    private int viewHeight; // 视图的高度
    private int imageWidth = 200; // 图像的宽度
    private int imageHeight = 200; // 图像的高度

    private List<BitmapPosition> bitmapPositions; // 存储图像及其位置

    public RandomFallCake(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RandomFallCake(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        // 获取屏幕尺寸
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        viewWidth = displayMetrics.widthPixels;
        viewHeight = displayMetrics.heightPixels;

        // 加载图像
        images[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.cake1);
        images[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.cake2);
        images[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.cake3);
        images[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.cake4);
        images[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.cake5);
        images[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.cake6);

        // 缩放图像
        for(int i=0;i<6;i++) {
            scaledImage[i] = Bitmap.createScaledBitmap(images[i], imageWidth, imageHeight, true);
        }
        random = new Random();

        // 初始化列表
        bitmapPositions = new ArrayList<>();
    }

    private void addNewRandomImage() {
        // 生成随机位置
        int x = random.nextInt(viewWidth - imageWidth);
        int y = 0; // 从顶部开始
        int randomIndex = random.nextInt(6);
        // 添加到列表
        bitmapPositions.add(new BitmapPosition(scaledImage[randomIndex], x, y, System.currentTimeMillis()));
        // 刷新视图
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
            int newY = (int) (bp.y + elapsedTime / 300.0); // 调整速度

            if (newY > viewHeight) {
                // 移出屏幕，移除图像
                iterator.remove();
            } else {
                bp.y = newY;
                canvas.drawBitmap(bp.bitmap, bp.x, bp.y, null);
            }
        }

        // 再次调用onDraw以继续动画
        postInvalidateOnAnimation();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 设置视图的尺寸
        setMeasuredDimension(viewWidth, viewHeight);
    }

    // 暴露一个公共方法以便外部调用
    public void generateNewRandomImage() {
        addNewRandomImage();
    }

    // 内部类用于存储图像及其位置
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
