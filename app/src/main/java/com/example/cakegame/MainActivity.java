package com.example.cakegame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.*;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static SoundPlay soundPlay;
    public static VibrationHelper vibrationHelper;
    @SuppressLint("StaticFieldLeak")
    public static ImageView rank;
    public static CustomSelector selectDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        soundPlay = new SoundPlay(this);
        vibrationHelper = VibrationHelper.getInstance(this);
        vibrationHelper.vibrate();

        Button[] difficulty = new Button[4];
        difficulty[0] = findViewById(R.id.difficulty0);
        difficulty[1] = findViewById(R.id.difficulty1);
        difficulty[2] = findViewById(R.id.difficulty2);
        difficulty[3] = findViewById(R.id.difficulty3);

        rank = findViewById(R.id.rank);
        rank.setOnClickListener(v -> {
            vibrationHelper.vibrate();
            soundPlay.playSound("click");
            startAnimationAndNavigate(rank, MainActivity3.class);
        });

        selectDialog = findViewById(R.id.selectDialog);

        setSelectDialogListener();


        for (int i = 0; i < 4; i++) {
            final int index = i; // 使用 final 變量保存索引
            difficulty[i].setOnClickListener(v -> {
                CakePane.setMode(index);
                vibrationHelper.vibrate();
                soundPlay.playSound("start");
                startAnimationAndNavigate(difficulty[index], MainActivity2.class);
            });
        }

        ImageView[] backgroundCake = new ImageView[6];
        boolean[] shake = {true, true, true, true, true, true};

        backgroundCake[0] = findViewById(R.id.backgroundCake1);
        backgroundCake[1] = findViewById(R.id.backgroundCake2);
        backgroundCake[2] = findViewById(R.id.backgroundCake3);
        backgroundCake[3] = findViewById(R.id.backgroundCake4);
        backgroundCake[4] = findViewById(R.id.backgroundCake5);
        backgroundCake[5] = findViewById(R.id.backgroundCake6);

        for(int i = 0;i < 6; i++) {
            Background.startShakeAnimation(backgroundCake[i]);
        }

        // 啟動搖晃動畫
        for (int i = 0; i < 6; i++) {
            final int index = i; // 使用 final 變量保存索引
            backgroundCake[i].setOnClickListener(v -> {
                shake[index] = !shake[index];
                if(shake[index]) {
                    Background.startShakeAnimation(backgroundCake[index]);
                }
                else {
                    Background.startRotateAnimation(backgroundCake[index]);
                }
            });
        }



    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        setSelectDialogListener();
    }

    private void setSelectDialogListener() {
        selectDialog.setListener(new CustomSelector.IconSelectListener() {
            @Override
            public void onOpen() {
                Log.d("CakeSort", "open");
                vibrationHelper.vibrate();
            }

            @Override
            public void onCancel() {
                vibrationHelper.vibrate();
            }
        });
    }



    private void startAnimationAndNavigate(View view, Class<?> destinationActivity) {
        // 新增一個動畫集合
        AnimationSet animSet = new AnimationSet(true);

        // 步驟2：放大1.2倍的動畫
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);

        // 步驟3：旋轉-20度的動畫
        RotateAnimation rotateAnimation = new RotateAnimation(
                0.0f, -20f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(200);

        // 將放大及旋轉的動畫放入動畫集合
        animSet.addAnimation(scaleAnimation);
        animSet.addAnimation(rotateAnimation);

        // 步驟4：開始動畫
        view.startAnimation(animSet);

        Intent intent = new Intent(MainActivity.this, destinationActivity);
        startActivity(intent);
    }
}