package com.example.cakegame;

import android.content.Context;
import android.os.Vibrator;

public class VibrationHelper {

    private static VibrationHelper instance;
    private static final long VIBRATE_TIME_MS = 15; // 定義震動時間
    private static Vibrator vibrator;
    private static boolean isVibrationEnabled = true; // 是否啟用震動

    // 私有構造函數，用於單例模式
    private VibrationHelper(Context context) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    // 獲取單例實例
    public static VibrationHelper getInstance(Context context) {
        if (instance == null) {
            instance = new VibrationHelper(context);
        }
        return instance;
    }

    // 震動方法
    public static void vibrate() {
        if (isVibrationEnabled && vibrator != null) {
            vibrator.vibrate(VIBRATE_TIME_MS);
        }
    }

    // 設置是否啟用震動
    public static void setVibrate(boolean enabled) {
        isVibrationEnabled = enabled;
    }

    // 獲取當前震動狀態
    public static boolean getVibrate() {
        return isVibrationEnabled;
    }
}
