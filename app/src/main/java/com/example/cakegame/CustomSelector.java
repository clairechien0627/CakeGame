package com.example.cakegame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.*;
import android.widget.*;

import androidx.annotation.Nullable;

public class CustomSelector extends LinearLayout {
    private boolean isOpen = false; // 是否打開對話框的標誌
    private static final int ROW_HEIGHT = 150; // 行高

    public interface IconSelectListener {
        void onOpen(); // 當打開時的回調
        void onCancel(); // 當取消時的回調
    }

    private IconSelectListener listener; // 圖標選擇監聽器

    public CustomSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    // 初始化方法
    private void init(Context context) {
        inflate(context, R.layout.custom_selector, this); // 加載布局
        drawDialog(); // 繪製對話框
        displayDialog(false); // 初始時不顯示對話框

        // 點擊圖標時的事件
        findViewById(R.id.select_imageview).setOnClickListener(v -> {
            handleIconClick();
        });
    }

    // 處理圖標點擊事件
    private void handleIconClick() {
        drawDialog(); // 繪製對話框
        rotateSelectImageView(); // 旋轉圖標
        displayDialog(!isOpen); // 根據 isOpen 變量顯示/隱藏對話框
        if (isOpen) {
            listener.onCancel(); // 如果對話框已打開，則調用 onCancel 回調
        } else {
            listener.onOpen(); // 如果對話框未打開，則調用 onOpen 回調
        }
        isOpen = !isOpen; // 切換 isOpen 狀態
    }

    // 旋轉圖標方法
    private void rotateSelectImageView() {
        float fromDegree = isOpen ? -45.0f : 0.0f; // 開始角度
        float toDegree = isOpen ? 0.0f : -45.0f; // 結束角度

        RotateAnimation animRotate = new RotateAnimation(fromDegree, toDegree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        animRotate.setDuration(300); // 動畫持續時間
        animRotate.setFillAfter(true); // 動畫結束後保留最終狀態

        findViewById(R.id.select_imageview).startAnimation(animRotate); // 開始動畫
    }

    // 繪製對話框方法
    @SuppressLint("UseCompatLoadingForDrawables")
    private void drawDialog() {
        int triangleWidth = 50; // 三角形寬度
        int triangleHeight = 40; // 三角形高度

        LinearLayout iconLinearLayout = findViewById(R.id.iconLinearLayout); // 圖標佈局
        iconLinearLayout.removeAllViews(); // 清除所有子視圖

        LinearLayout imageLayout = createImageLayout(); // 創建圖片佈局
        addSoundControl(imageLayout); // 添加音效控制
        addShakeControl(imageLayout); // 添加搖晃控制

        iconLinearLayout.addView(imageLayout); // 添加圖片佈局到圖標佈局

        drawDialogBackground(triangleWidth, triangleHeight); // 繪製對話框背景
    }

    // 創建圖片佈局
    private LinearLayout createImageLayout() {
        LinearLayout imageLayout = new LinearLayout(getContext());
        imageLayout.setOrientation(LinearLayout.HORIZONTAL); // 設置水平方向
        return imageLayout;
    }

    // 添加音效控制
    private void addSoundControl(LinearLayout imageLayout) {
        FrameLayout soundFrameLayout = createControlFrameLayout();
        ImageView soundImageView = createControlImageView(R.drawable.volume_high_solid);
        ImageView soundOverlayImageView = createControlOverlayImageView(R.drawable.volume_xmark_solid);

        soundFrameLayout.addView(soundImageView); // 添加音效圖片視圖
        soundFrameLayout.addView(soundOverlayImageView); // 添加音效覆蓋視圖

        updateSoundIconVisibility(soundImageView, soundOverlayImageView); // 更新音效圖標可見性

        // 點擊事件
        soundImageView.setOnClickListener(v -> handleSoundClick(soundImageView, soundOverlayImageView));
        soundOverlayImageView.setOnClickListener(v -> handleSoundClick(soundImageView, soundOverlayImageView));

        imageLayout.addView(soundFrameLayout); // 添加音效控制到圖片佈局
    }

    // 更新音效圖標可見性
    private void updateSoundIconVisibility(ImageView soundImageView, ImageView soundOverlayImageView) {
        if (!SoundPlay.isMuted()) {
            soundOverlayImageView.setVisibility(View.INVISIBLE);
        } else {
            soundImageView.setVisibility(View.INVISIBLE);
        }
    }

    // 處理音效點擊事件
    private void handleSoundClick(ImageView soundImageView, ImageView soundOverlayImageView) {
        new Handler().postDelayed(this::dismissDialog, 500);
        if (soundOverlayImageView.getVisibility() == View.INVISIBLE) {
            soundOverlayImageView.setVisibility(View.VISIBLE);
            soundImageView.setVisibility(View.INVISIBLE);
            SoundPlay.setMute(true);
            Log.d("CustomSelector", "Muted");
        } else {
            soundOverlayImageView.setVisibility(View.INVISIBLE);
            soundImageView.setVisibility(View.VISIBLE);
            SoundPlay.setMute(false);
            SoundPlay.playSound("click");
            Log.d("CustomSelector", "unMuted");
        }
    }

    // 添加搖晃控制
    private void addShakeControl(LinearLayout imageLayout) {
        FrameLayout shakeFrameLayout = createControlFrameLayout();
        ImageView shakeImageView = createControlImageView(R.drawable.phone_shake_svgrepo_com);
        ImageView shakeOverlayImageView = createControlOverlayImageView(R.drawable.slash_solid);

        shakeFrameLayout.addView(shakeImageView); // 添加搖晃圖片視圖
        shakeFrameLayout.addView(shakeOverlayImageView); // 添加搖晃覆蓋視圖

        updateShakeIconVisibility(shakeOverlayImageView); // 更新搖晃圖標可見性

        // 點擊事件
        shakeImageView.setOnClickListener(v -> handleShakeClick(shakeImageView, shakeOverlayImageView));

        imageLayout.addView(shakeFrameLayout); // 添加搖晃控制到圖片佈局
    }

    // 更新搖晃圖標可見性
    private void updateShakeIconVisibility(ImageView shakeOverlayImageView) {
        if (VibrationHelper.getVibrate()) {
            shakeOverlayImageView.setVisibility(View.INVISIBLE);
        }
    }

    // 處理搖晃點擊事件
    private void handleShakeClick(ImageView shakeImageView, ImageView shakeOverlayImageView) {
        new Handler().postDelayed(this::dismissDialog, 500);
        if (shakeOverlayImageView.getVisibility() == View.INVISIBLE) {
            shakeOverlayImageView.setVisibility(View.VISIBLE);
            VibrationHelper.setVibrate(false);
        } else {
            shakeOverlayImageView.setVisibility(View.INVISIBLE);
            VibrationHelper.setVibrate(true);
            VibrationHelper.vibrate();
        }
    }

    // 創建控制FrameLayout
    private FrameLayout createControlFrameLayout() {
        FrameLayout frameLayout = new FrameLayout(getContext());
        LinearLayout.LayoutParams frameLayoutParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1 // 權重為1
        );
        frameLayoutParams.gravity = Gravity.CENTER; // 設置重心為中心
        frameLayout.setLayoutParams(frameLayoutParams); // 設置佈局參數
        return frameLayout;
    }

    // 創建控制ImageView
    private ImageView createControlImageView(int resId) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(resId); // 設置圖片
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                dpToPx(getContext(), 25),
                dpToPx(getContext(), 25)
        );
        layoutParams.gravity = Gravity.CENTER; // 設置重心為中心
        imageView.setLayoutParams(layoutParams); // 設置圖片佈局參數
        return imageView;
    }

    // 創建控制覆蓋ImageView
    private ImageView createControlOverlayImageView(int resId) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(resId); // 設置覆蓋圖片
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                dpToPx(getContext(), 25),
                dpToPx(getContext(), 25)
        );
        layoutParams.gravity = Gravity.CENTER; // 設置重心為中心
        imageView.setLayoutParams(layoutParams); // 設置覆蓋圖片佈局參數
        return imageView;
    }

    // 繪製對話框背景
    private void drawDialogBackground(int triangleWidth, int triangleHeight) {
        int dialogWidth = 500; // 對話框寬度
        int dialogHeight = ROW_HEIGHT + triangleHeight; // 對話框高度

        Bitmap bitmap = Bitmap.createBitmap(dialogWidth, dialogHeight, Bitmap.Config.ARGB_8888); // 創建位圖
        Paint p = new Paint(); // 繪製畫筆
        p.setColor(Color.WHITE); // 設置顏色
        p.setShadowLayer(5f, 2f, 2f, Color.LTGRAY); // 設置陰影
        p.setAntiAlias(true); // 設置抗鋸齒
        Canvas canvas = new Canvas(bitmap); // 創建畫布

        Path path = createDialogPath(dialogWidth, dialogHeight, triangleWidth, triangleHeight); // 創建對話框路徑
        canvas.drawPath(path, p); // 繪製路徑

        findViewById(R.id.dialog_select_linearlayout).setBackground(new BitmapDrawable(getResources(), bitmap)); // 設置對話框背景
    }

    // 創建對話框路徑
    private Path createDialogPath(int dialogWidth, int dialogHeight, int triangleWidth, int triangleHeight) {
        Path path = new Path(); // 路徑
        RectF rectF = new RectF(0, triangleHeight, dialogWidth, dialogHeight); // 矩形範圍
        float cornerRadius = 20; // 圓角的半徑
        path.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW); // 添加圓角矩形路徑
        path.moveTo(dialogWidth - triangleWidth - 60, triangleHeight); // 移動到三角形左側點
        path.lineTo(dialogWidth - triangleWidth / 2f - 60, 0); // 添加三角形頂點
        path.lineTo(dialogWidth - 60, triangleHeight); // 添加三角形右側點
        path.close(); // 關閉路徑
        return path;
    }

    // 顯示/隱藏對話框
    private void displayDialog(boolean display) {
        findViewById(R.id.dialog_select_linearlayout).setVisibility(display ? View.VISIBLE : View.GONE);
        if (display) {
            ScaleAnimation anim = new ScaleAnimation(0, 1, 0, 1,
                    Animation.RELATIVE_TO_SELF, 0.8f, Animation.RELATIVE_TO_SELF, 0f);
            anim.setDuration(300);
            findViewById(R.id.dialog_select_linearlayout).startAnimation(anim);
        }
    }

    // 關閉對話框
    private void dismissDialog() {
        isOpen = false;
        rotateSelectImageView();
        displayDialog(false);
    }

    // 設置圖標選擇監聽器
    public void setListener(IconSelectListener listener) {
        this.listener = listener;
    }

    // dp轉px
    private int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f); // 四捨五入
    }
}
