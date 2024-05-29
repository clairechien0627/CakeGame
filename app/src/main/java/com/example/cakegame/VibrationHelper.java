package com.example.cakegame;

import android.content.Context;
import android.os.Vibrator;

public class VibrationHelper {
    private static VibrationHelper instance;
    private static long vibrateTime = 15;
    private static Vibrator vibrator;
    private static boolean isVibrationEnabled = true;

    private VibrationHelper(Context context) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static VibrationHelper getInstance(Context context) {
        if (instance == null) {
            instance = new VibrationHelper(context);
        }
        return instance;
    }

    public static void vibrate() {
        if (isVibrationEnabled && vibrator != null) {
            vibrator.vibrate(vibrateTime);
        }
    }

    public static void setVibrate(boolean enabled) {
        isVibrationEnabled = enabled;
    }

    public static boolean getVibrate() {
        return isVibrationEnabled;
    }
}
