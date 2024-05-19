package com.example.cakegame;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;

import androidx.annotation.NonNull;

public class CakeView extends View {

    private int mHeight, mWidth;
    private Paint[] paints;
    private float radius;
    private float startAngle = 90;
    private CakePane cakepane;
    private RectF rectF;

    public CakeView(Context context) {
        super(context);
        init();
    }

    public CakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        int[] colors = new int[]{
                0xFFFF0000, // 红色
                0xFFFFA500, // 橙色
                0xFFFFFF00, // 黄色
                0xFF00FF00, // 绿色
                0xFF0000FF, // 蓝色
                0xFF800080  // 紫色
        };

        paints = new Paint[6];
        for (int i = 0; i < 6; i++) {
            paints[i] = new Paint();
            paints[i].setStyle(Paint.Style.FILL);
            paints[i].setColor(colors[i]);
        }

        radius = 50;
        rectF = new RectF(); // 初始化 RectF 对象
    }

    public void setCakePane(CakePane cakepane) {
        this.cakepane = cakepane;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (cakepane == null) {
            return;
        }

        int centerX = (getRight() - getLeft()) / 2;
        int centerY = (getBottom() - getTop()) / 2;
        radius = (float) (Math.min(mHeight, mWidth) / 2 * 0.8);
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        startAngle = -90;
        for (int i = 0; i < 6; i++) {
            int pieces = cakepane.getPiecesNum(i);
            canvas.drawArc(rectF, startAngle, 45 * pieces, true, paints[i]);
            startAngle += 45 * pieces;
        }
    }

}
