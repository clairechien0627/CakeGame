package com.example.cakegame;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.animation.*;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static SoundPlay soundPlay;
    public static ImageView rank;
    public static ImageView setting;
    public static Vibrator vibrator;
    public static int vibrateTime = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        soundPlay = new SoundPlay(this);

        Button[] difficulty = new Button[4];
        difficulty[0] = findViewById(R.id.difficulty0);
        difficulty[1] = findViewById(R.id.difficulty1);
        difficulty[2] = findViewById(R.id.difficulty2);
        difficulty[3] = findViewById(R.id.difficulty3);

        rank = findViewById(R.id.rank);
        rank.setOnClickListener(v -> {
            vibrator.vibrate(vibrateTime);
            startAnimationAndNavigate(rank, MainActivity3.class);
        });

        setting = findViewById(R.id.setting);
        setting.setOnClickListener(v -> {
            vibrator.vibrate(vibrateTime);
        });


        for (int i = 0; i < 4; i++) {
            final int index = i; // 使用 final 变量保存索引
            difficulty[i].setOnClickListener(v -> {
                CakePane cakepane = new CakePane(index);
                vibrator.vibrate(vibrateTime);
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

        for (int i = 0; i < 6; i++) {
            Background.startShakeAnimation(backgroundCake[i]);
        }

        // 啟動搖晃動畫
        for (int i = 0; i < 6; i++) {
            final int index = i; // 使用 final 变量保存索引
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

    private void startAnimationAndNavigate(View view, Class<?> destinationActivity) {
        // 新增一个动画集合
        AnimationSet animSet = new AnimationSet(true);

        // 步骤2：放大1.2倍的动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);

        // 步骤3：旋转-20度的动画
        RotateAnimation rotateAnimation = new RotateAnimation(
                0.0f, -20f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(200);

        // 将放大及旋转的动画放入动画集合
        animSet.addAnimation(scaleAnimation);
        animSet.addAnimation(rotateAnimation);

        // 步骤4：开始动画
        view.startAnimation(animSet);
        soundPlay.getSound("start");

        Intent intent = new Intent(MainActivity.this, destinationActivity);
        startActivity(intent);
    }
}
