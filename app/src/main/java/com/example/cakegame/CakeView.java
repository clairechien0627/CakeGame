package com.example.cakegame;

import android.animation.*;
import android.content.*;
import android.graphics.*;
import android.util.*;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import java.util.Random;

public class CakeView extends AppCompatImageView {

    private int height, width;
    private Paint[] paints;
    private float radius;
    private float outRadius;
    private float startAngle = 90;
    private CakePane cakepane;
    private RectF rectF;
    private int centerX;
    private int centerY;
    private boolean onAnimation = false;

    private static final int[] colors = new int[]{
            0xFFFF0000, // 红色
            0xFFFFA500, // 橙色
            0xFFFFFF00, // 黄色
            0xFF00FF00, // 绿色
            0xFF0000FF, // 蓝色
            0xFF800080  // 紫色
    };

    private static final int[] colors2 = new int[]{
            0xFFEA5959,
            0xFFFFA962,
            0xFFFFFA5B,
            0xFF81E75F,
            0xFF81FFFB,
            0xFFB679DB
    };

    private static final int[] colors3 = new int[]{
            0xFFE07A5F,
            0xFF7A5151,
            0xFF3D405B,
            0xFF5F797B,
            0xFF81B29A,
            0xFFF2CC8F
    };

    private static final int[] colors4 = new int[]{
            0xFF006D77,
            0xFF42999B,
            0xFF83C5BE,
            0xFFFFDDD2,
            0xFFF1B9A5,
            0xFFF69474
    };

    private static final int[] colors5 = new int[]{
            0xFF177E89,
            0xFF084C61,
            0xFFE4A6B1,
            0xFFDB3A34,
            0xFFFFC857,
            0xFF323031
    };

    private static final int[] colors6 = new int[]{
            0xFFF686DE,
            0xFFFF8709,
            0xFFFFE70A,
            0xFF4DFFED,
            0xFF3A86FF,
            0xFF8338EC
    };

    private static int[] currentcolors = colors;

    private static int[][] allColors = {colors, colors2, colors3, colors4, colors5, colors6};

    public static final int ARC_FULL_ROTATION_DEGREE = 360;
    public static final double PERCENTAGE_DIVIDER = 100.0;
    public static final String PERCENTAGE_VALUE_HOLDER = "percentage";

    private int currentPercentage = 0;

    private final RectF ovalSpace = new RectF();

    private int fullCakeColor;
    private int parentArcColor;
    private int fillArcColor;

    private final Paint parentArcPaint = new Paint();
    private final Paint fillArcPaint = new Paint();
    private Paint fullCakePaint = new Paint();

    public CakeView(Context context) {
        super(context);
        init(context);
    }

    public CakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {


        paints = new Paint[6];
        for (int i = 0; i < 6; i++) {
            paints[i] = new Paint();
            paints[i].setStyle(Paint.Style.FILL);
            paints[i].setColor(currentcolors[i]);
        }

        fullCakePaint.setStyle(Paint.Style.FILL);
        fullCakeColor = ContextCompat.getColor(context, R.color.white);
        fullCakePaint.setColor(fullCakeColor);
        fullCakePaint.setAlpha(0);

        radius = 50;
        rectF = new RectF(); // 初始化 RectF 对象

        // 设置外圆环的画笔
        parentArcPaint.setStyle(Paint.Style.STROKE);
        parentArcPaint.setAntiAlias(true);
        parentArcColor = ContextCompat.getColor(context, R.color.gray_light);
        parentArcPaint.setColor(parentArcColor);
        parentArcPaint.setStrokeWidth(10f);

        // 设置内圆环的画笔
        fillArcPaint.setStyle(Paint.Style.STROKE);
        fillArcPaint.setAntiAlias(true);
        fillArcColor = ContextCompat.getColor(context, R.color.gray);
        fillArcPaint.setColor(fillArcColor);
        fillArcPaint.setStrokeWidth(10f);
        fillArcPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    public void setCakePane(CakePane cakepane) {
        this.cakepane = cakepane;
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (cakepane == null) {
            return;
        }

        centerX = (getRight() - getLeft()) / 2;
        centerY = (getBottom() - getTop()) / 2;

        outRadius = (float) (Math.min(height, width) / 2 * 0.95);
        ovalSpace.set(centerX - outRadius, centerY - outRadius, centerX + outRadius, centerY + outRadius);

        radius = (float) (Math.min(height, width) / 2 * 0.8);
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        drawBackgroundArc(canvas);
        drawInnerArc(canvas);
        drawCake(canvas);
        drawFullCake(canvas);

    }

    public void setSize(int height, int width){
        this.height = height;
        this.width = width;
        invalidate();
    }

    private void drawBackgroundArc(Canvas canvas) {
        canvas.drawArc(ovalSpace, 0f, 360f, false, parentArcPaint);
    }

    private void drawInnerArc(Canvas canvas) {
        float percentageToFill = getCurrentPercentageToFill();
        canvas.drawArc(ovalSpace, 270f, percentageToFill, false, fillArcPaint);
    }

    private void drawCake(Canvas canvas) {
        startAngle = -90;
        for (int i = 0; i < 6; i++) {
            int pieces = cakepane.getPiecesNum(i);
            paints[i].setColor(currentcolors[i]);
            canvas.drawArc(rectF, startAngle, 45 * pieces, true, paints[i]);
            startAngle += 45 * pieces;
        }
//        Log.d("CakeView", "draw" + startAngle);
    }

    private void drawFullCake(Canvas canvas) {
        canvas.drawArc(rectF, 0f, 360f, true, fullCakePaint);
    }


    // 计算当前进度对应的角度
    private float getCurrentPercentageToFill() {
        return (float) (ARC_FULL_ROTATION_DEGREE * (currentPercentage / PERCENTAGE_DIVIDER));
    }

    // 动画进度
    public void animateProgress(int p) {
        PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofFloat("percentage", 0f, 100f);
        ValueAnimator animator = new ValueAnimator();
        animator.setValues(valuesHolder);
        animator.setDuration(1000);
        animator.addUpdateListener(animation -> {
            float percentage = (float) animation.getAnimatedValue(PERCENTAGE_VALUE_HOLDER);
            currentPercentage = (int) percentage;
            invalidate();
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // 动画開始后的操作
                SoundPlay.playSound("full");
                onAnimation = true;
                fullCakePaint.setColor(currentcolors[p]);
                fullCakePaint.setAlpha(255);
                fillArcPaint.setColor(fillArcColor);
                currentPercentage = 360;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束后的操作
                onAnimation = false;
                fullCakePaint.setColor(fullCakeColor);
                fullCakePaint.setAlpha(0);
                fillArcPaint.setColor(parentArcColor);
                currentPercentage = 0;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // 动画取消时的操作
                onAnimation = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // 动画重复时的操作
            }
        });
        animator.start();
    }


    public void animateAddCake() {
        ValueAnimator animator = ValueAnimator.ofInt(255, 0); // 从不透明到透明
        animator.setDuration(500); // 动画持续时间
        animator.addUpdateListener(animation -> {
            int alpha = (int) animation.getAnimatedValue();
            fullCakePaint.setAlpha(alpha); // 设置透明度
            invalidate();
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                SoundPlay.playSound("add");
                onAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onAnimation = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                onAnimation = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // Do nothing
            }
        });
        animator.start();
    }


    public boolean onAnimation() { return onAnimation;}

    public static void setCakePaint(int colorIndex) {
        currentcolors = allColors[colorIndex % allColors.length];
    }
}