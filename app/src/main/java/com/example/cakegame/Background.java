package com.example.cakegame;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.util.Random;

public class Background {
    // 創建一個搖晃動畫
    public static void startShakeAnimation(ImageView imageView) {
        RotateAnimation shake = new RotateAnimation((float) Math.random()*360, (float) Math.random()*360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shake.setDuration(1000); // 持續時間 1 秒
        shake.setRepeatCount(Animation.INFINITE); // 無限循環
        shake.setRepeatMode(Animation.REVERSE); // 反向重複
        imageView.startAnimation(shake);
    }

    public static void startRotateAnimation(ImageView imageView) {
        RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000); // 旋轉一圈的時間，單位為毫秒
        rotate.setRepeatCount(Animation.INFINITE); // 重複無限次
        imageView.startAnimation(rotate);
    }

}
