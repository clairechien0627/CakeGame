package com.example.cakegame;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class Background {
    // 創建一個搖晃動畫
    public static void startShakeAnimation(Context context, ImageView imageView) {
        RotateAnimation shake = new RotateAnimation(-5, 5, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shake.setDuration(1000); // 持續時間 1 秒
        shake.setRepeatCount(Animation.INFINITE); // 無限循環
        shake.setRepeatMode(Animation.REVERSE); // 反向重複
        imageView.startAnimation(shake);
    }
}
