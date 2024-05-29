package com.example.cakegame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.*;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;

import android.os.Handler;

public class CustomSelector extends LinearLayout {
    private boolean isOpen = false; // 是否打開對話框的標誌
    private static final int ROW_HEIGHT = 150;

    public interface IconSelectListener {
        void onOpen();
        void onCancel();
    }

    private IconSelectListener listener;

    public CustomSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.custom_selector, this);
        drawDialog();
        displayDialog(false);

        findViewById(R.id.select_imageview).setOnClickListener(v -> {
            Log.d("CakeSort", "listener");
            rotateSelectImageView();
            displayDialog(!isOpen);
            if (isOpen) {
                listener.onCancel();
            } else {
                listener.onOpen();
            }
            isOpen = !isOpen;
        });
    }

    private void rotateSelectImageView() {
        float fromDegree = isOpen ? -45.0f : 0.0f;
        float toDegree = isOpen ? 0.0f : -45.0f;

        RotateAnimation animRotate = new RotateAnimation(fromDegree, toDegree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        animRotate.setDuration(300);
        animRotate.setFillAfter(true);

        findViewById(R.id.select_imageview).startAnimation(animRotate);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void drawDialog() {
        int triangleWidth = 50;
        int triangleHeight = 40;

        LinearLayout iconLinearLayout = findViewById(R.id.iconLinearLayout);
        iconLinearLayout.removeAllViews();
        LinearLayout imageLayout = new LinearLayout(getContext());
        imageLayout.setOrientation(LinearLayout.HORIZONTAL);
        imageLayout.setWeightSum(2);

        FrameLayout soundFrameLayout = new FrameLayout(getContext());
        LinearLayout.LayoutParams frameLayoutParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1 // Weight as 1
        );
        frameLayoutParams.gravity = Gravity.CENTER; // Set gravity to center
        soundFrameLayout.setLayoutParams(frameLayoutParams);

        ImageView soundImageView = new ImageView(getContext());
        soundImageView.setImageResource(R.drawable.volume_high_solid);
        FrameLayout.LayoutParams soundImageLayoutParams = new FrameLayout.LayoutParams(
                dpToPx(getContext(), 25),
                dpToPx(getContext(), 25)
        );
        soundImageLayoutParams.gravity = Gravity.CENTER;
        soundImageView.setLayoutParams(soundImageLayoutParams);
        soundFrameLayout.addView(soundImageView);

        ImageView soundOverlayImageView = new ImageView(getContext());
        soundOverlayImageView.setImageResource(R.drawable.volume_xmark_solid);
        soundOverlayImageView.setLayoutParams(soundImageLayoutParams);
        soundOverlayImageView.setVisibility(View.INVISIBLE);
        soundFrameLayout.addView(soundOverlayImageView);

        soundImageView.setOnClickListener(v -> {
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
        });
        soundOverlayImageView.setOnClickListener(v -> {
            new Handler().postDelayed(this::dismissDialog, 500);
            if (soundOverlayImageView.getVisibility() == View.INVISIBLE) {
                soundImageView.setVisibility(View.INVISIBLE);
                soundOverlayImageView.setVisibility(View.VISIBLE);
                SoundPlay.setMute(true);
                Log.d("CustomSelector", "Muted");
            } else {
                soundImageView.setVisibility(View.VISIBLE);
                soundOverlayImageView.setVisibility(View.INVISIBLE);
                SoundPlay.setMute(false);
                SoundPlay.playSound("click");
                Log.d("CustomSelector", "unMuted");
            }
        });
        imageLayout.addView(soundFrameLayout);

        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayoutParams.gravity = Gravity.CENTER; // Set gravity to center
        frameLayout.setLayoutParams(frameLayoutParams);

        ImageView shakeImageView = new ImageView(getContext());
        shakeImageView.setImageResource(R.drawable.phone_shake_svgrepo_com);
        FrameLayout.LayoutParams shakeImageLayoutParams = new FrameLayout.LayoutParams(
                dpToPx(getContext(), 30),
                dpToPx(getContext(), 30)
        );
        shakeImageLayoutParams.gravity = Gravity.CENTER;
        shakeImageView.setLayoutParams(shakeImageLayoutParams);
        frameLayout.addView(shakeImageView);

        ImageView overlayImageView = new ImageView(getContext());
        overlayImageView.setImageResource(R.drawable.slash_solid);
        overlayImageView.setLayoutParams(shakeImageLayoutParams);
        overlayImageView.setVisibility(View.INVISIBLE);
        frameLayout.addView(overlayImageView);

        shakeImageView.setOnClickListener(v -> {
            new Handler().postDelayed(this::dismissDialog, 500);
            if (overlayImageView.getVisibility() == View.INVISIBLE) {
                overlayImageView.setVisibility(View.VISIBLE);
                VibrationHelper.setVibrate(false);
            } else {
                overlayImageView.setVisibility(View.INVISIBLE);
                VibrationHelper.setVibrate(true);
                VibrationHelper.vibrate();
            }
        });
        imageLayout.addView(frameLayout);

        iconLinearLayout.addView(imageLayout);


        int dialogWidth = 500;
        int dialogHeight = ROW_HEIGHT + triangleHeight;

        Bitmap bitmap = Bitmap.createBitmap(dialogWidth, dialogHeight, Bitmap.Config.ARGB_8888);
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setShadowLayer(5f, 2f, 2f, Color.LTGRAY);
        p.setAntiAlias(true);
        Canvas canvas = new Canvas(bitmap);

        Path path = new Path();
        RectF rectF = new RectF(0, triangleHeight, dialogWidth, dialogHeight);
        float cornerRadius = 20; // 圓角的半徑
        path.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW);
        path.moveTo(dialogWidth - triangleWidth - 60, triangleHeight);
        path.lineTo(dialogWidth - triangleWidth/2f - 60, 0);
        path.lineTo(dialogWidth - 60, triangleHeight);
        path.close();

        canvas.drawPath(path, p);
        findViewById(R.id.dialog_select_linearlayout).setBackground(new BitmapDrawable(getResources(), bitmap));
    }

    private void displayDialog(boolean display) {
        findViewById(R.id.dialog_select_linearlayout).setVisibility(display ? View.VISIBLE : View.GONE);
        if (display) {
            ScaleAnimation anim = new ScaleAnimation(0, 1, 0, 1,
                    Animation.RELATIVE_TO_SELF, 0.8f, Animation.RELATIVE_TO_SELF, 0f);
            anim.setDuration(300);
            findViewById(R.id.dialog_select_linearlayout).startAnimation(anim);
        }
    }

    private void dismissDialog() {
        isOpen = false;
        rotateSelectImageView();
        displayDialog(false);
    }

    public void setListener(IconSelectListener listener) {
        this.listener = listener;
    }

    private int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f); // 四捨五入
    }
}
