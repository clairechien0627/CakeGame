package com.example.cakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class CakeView extends View {

    private Paint[] paints;
    private float radius;
    private CakePane cakepane = new CakePane();
    private ArrayList<Integer> pieces = cakepane.getPieces();


    public CakeView(Context context) {
        super(context);
        init();
    }

    public CakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CakeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int[] colors = new int[]{
                0xFFFF0000, // Red
                0xFFFFA500, // Orange
                0xFFFFFF00, // Yellow
                0xFF00FF00, // Green
                0xFF0000FF, // Blue
                0xFF800080  // Purple
        };
        for (int i = 0 ; i < pieces.size();i++){
            int color = pieces.get(i);
            paints = new Paint[5];
            for (int j = 0; j < 5; j++) {
                paints[i] = new Paint();
                paints[i].setStyle(Paint.Style.STROKE);
                paints[i].setColor(colors[color]);
            }
        }

        radius = 100; // Set the radius here
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        float startAngle = 0;
        for (int i = 0; i < 5; i++) {
            // Draw 1/8 circle
            canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius,
                    startAngle, 45, false, paints[i]);
            startAngle += 45;
        }
    }


}