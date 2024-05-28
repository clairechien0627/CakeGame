package com.example.cakegame;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;

public class CustomSelector extends LinearLayout {
    private int[] drawables = {R.drawable.volume_high_solid, R.drawable.phone_shake_svgrepo_com}; // 圖標資源的ID陣列
    private boolean isOpen = false; // 是否打開對話框的標誌
    private static final int ROW_HEIGHT = 150;

    public interface IconSelectListener {
        void onOpen();
        void onSelected(int iconIndex);
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

    private void drawDialog() {
        int triangleWidth = 50;
        int triangleHeight = 40;

        LinearLayout iconLinearLayout = findViewById(R.id.iconLinearLayout);
        iconLinearLayout.removeAllViews();
        LinearLayout imageLayout = new LinearLayout(getContext());
        imageLayout.setOrientation(LinearLayout.HORIZONTAL);

        for (int j = 0; j < 2; j++) {
            int index = j;
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(drawables[index]);
            // 設置圖標的寬度和高度限制為30dp
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    dpToPx(getContext(), 30), // 寬度為30dp
                    dpToPx(getContext(), 30), // 高度為30dp
                    1
            ));
            imageView.setOnClickListener(v -> {
                dismissDialog();
                listener.onSelected(index);
            });
            imageLayout.addView(imageView);
        }

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
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
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