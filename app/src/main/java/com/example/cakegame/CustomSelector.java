package com.example.cakegame;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;

public class CustomSelector extends LinearLayout {
    private int[] drawables = new int[0];
    private boolean isOpen = false;
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

    public void setSelectIcon(int[] drawables) {
        this.drawables = drawables;
        drawDialog();
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
        int imagesPerRow = 3;
        int imageRows = (int) Math.ceil((double) drawables.length / imagesPerRow);

        LinearLayout iconLinearLayout = findViewById(R.id.iconLinearLayout);
        iconLinearLayout.removeAllViews();

        for (int i = 0; i < imageRows; i++) {
            LinearLayout imageLayout = new LinearLayout(getContext());
            imageLayout.setOrientation(LinearLayout.HORIZONTAL);

            for (int j = 0; j < imagesPerRow; j++) {
                int index = i * imagesPerRow + j;
                if (index < drawables.length) {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setImageResource(drawables[index]);
                    imageView.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f));
                    imageView.setOnClickListener(v -> {
                        dismissDialog();
                        listener.onSelected(index);
                    });
                    imageLayout.addView(imageView);
                }
            }

            iconLinearLayout.addView(imageLayout);
        }

        int dialogWidth = 500;
        int dialogHeight = imageRows * ROW_HEIGHT + triangleHeight;

        Bitmap bitmap = Bitmap.createBitmap(dialogWidth, dialogHeight, Bitmap.Config.ARGB_8888);
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setShadowLayer(5f, 2f, 2f, Color.LTGRAY);
        p.setAntiAlias(true);
        Canvas canvas = new Canvas(bitmap);

        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(dialogWidth, 0);
        path.lineTo(dialogWidth, dialogHeight - triangleHeight);
        path.lineTo(dialogWidth / 2f + triangleWidth / 2f, dialogHeight - triangleHeight);
        path.lineTo(dialogWidth / 2f, dialogHeight);
        path.lineTo(dialogWidth / 2f - triangleWidth / 2f, dialogHeight - triangleHeight);
        path.lineTo(0, dialogHeight - triangleHeight);
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
}