package com.example.cakegame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // 靜態變量，用於播放聲音
    @SuppressLint("StaticFieldLeak")
    public SoundPlay soundPlay;
    // 靜態變量，用於控制震動
    public VibrationHelper vibrationHelper;
    // 靜態變量，用於顯示排行榜
    @SuppressLint("StaticFieldLeak")
    public ImageView rank;
    // 自定義選擇對話框
    public CustomSelector selectDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化界面元素和相關功能
        initialize();

        // 初始化難度按鈕並設置點擊監聽器
        Button[] difficultyButtons = {
                findViewById(R.id.difficulty0),
                findViewById(R.id.difficulty1),
                findViewById(R.id.difficulty2),
                findViewById(R.id.difficulty3)
        };
        setupDifficultyButtons(difficultyButtons);

        // 初始化背景蛋糕圖片並設置點擊監聽器
        ImageView[] backgroundCakes = {
                findViewById(R.id.backgroundCake1),
                findViewById(R.id.backgroundCake2),
                findViewById(R.id.backgroundCake3),
                findViewById(R.id.backgroundCake4),
                findViewById(R.id.backgroundCake5),
                findViewById(R.id.backgroundCake6)
        };
        setupBackgroundCakes(backgroundCakes);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 停止動畫或釋放資源可以放在這裡
    }

    // 初始化相關元素和功能
    private void initialize() {
        // 初始化聲音播放器和震動幫助類
        soundPlay = new SoundPlay(this);
        vibrationHelper = VibrationHelper.getInstance(this);
        VibrationHelper.vibrate();

        // 獲取並設置排行榜圖片的點擊監聽器
        rank = findViewById(R.id.rank);
        rank.setOnClickListener(v -> {
            VibrationHelper.vibrate();
            SoundPlay.playSound("click");
            startAnimationAndNavigate(rank, MainActivity3.class);
        });

        // 獲取並設置自定義選擇對話框
        selectDialog = findViewById(R.id.selectDialog);
        setSelectDialogListener();
    }

    // 初始化難度按鈕並設置點擊監聽器
    private void setupDifficultyButtons(Button[] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            final int index = i; // 使用 final 變量保存索引
            buttons[i].setOnClickListener(v -> {
                CakePane.setMode(index);
                VibrationHelper.vibrate();
                SoundPlay.playSound("start");
                startAnimationAndNavigate(buttons[index], MainActivity2.class);
            });
        }
    }

    // 初始化背景蛋糕圖片並設置點擊監聽器
    private void setupBackgroundCakes(ImageView[] cakes) {

        // 開始所有背景蛋糕的搖晃動畫
        for (ImageView cake : cakes) {
            Background.startShakeAnimation(cake);
        }

        // 為每個背景蛋糕設置點擊監聽器，切換搖晃或旋轉動畫
        for (int i = 0; i < cakes.length; i++) {
            final int index = i; // 使用 final 變量保存索引
            cakes[i].setOnClickListener(v -> {
                CakeView.setCakePaint(index);
                for(int j=0;j<6;j++) {
                    if(j == index) {
                        Background.startRotateAnimation(cakes[j]);
                    } else {
                        Background.startShakeAnimation(cakes[j]);
                    }
                }
            });
        }
    }

    // 設置選擇對話框的監聽器
    private void setSelectDialogListener() {
        selectDialog.setListener(new CustomSelector.IconSelectListener() {
            @Override
            public void onOpen() {
                Log.d("CakeSort", "open");
                VibrationHelper.vibrate();
            }

            @Override
            public void onCancel() {
                VibrationHelper.vibrate();
            }
        });
    }

    // 開始動畫並導航到指定的 Activity
    private void startAnimationAndNavigate(View view, Class<?> destinationActivity) {
        // 新增一個動畫集合
        AnimationSet animSet = new AnimationSet(true);

        // 放大 1.2 倍的動畫
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);

        // 旋轉 -20 度的動畫
        RotateAnimation rotateAnimation = new RotateAnimation(
                0.0f, -20f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(200);

        // 將放大及旋轉的動畫放入動畫集合
        animSet.addAnimation(scaleAnimation);
        animSet.addAnimation(rotateAnimation);

        // 設置動畫監聽器，當動畫結束後導航到指定的 Activity
        animSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // 動畫開始時的操作
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 動畫結束時的操作，進行跳轉
                Intent intent = new Intent(MainActivity.this, destinationActivity);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // 動畫重複時的操作
            }
        });

        // 開始動畫
        view.startAnimation(animSet);
    }
}