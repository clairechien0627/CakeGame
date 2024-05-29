package com.example.cakegame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

public class SoundPlay {

    // SoundPool 用於播放音效
    private static SoundPool soundPool;
    // 保存音效文件的資源 ID 映射
    private static final HashMap<String, Integer> soundMap = new HashMap<>();
    // 設置音量
    private static float volume;
    // 靜音狀態
    private static boolean isMuted = false;
    // 上下文環境
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    // SoundPlayer 的建構子
    public SoundPlay(Context context) {
        SoundPlay.context = context;
        // 設置音效屬性
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        // 建立 SoundPool 實例
        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();

        // 載入音效文件並將其映射到相應的名稱
        loadSound(R.raw.choose, "choose");
        loadSound(R.raw.full, "full");
        loadSound(R.raw.add, "add");
        loadSound(R.raw.end, "end");
        loadSound(R.raw.start, "start");
        loadSound(R.raw.click, "click");

        // 初始化音量
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        } else {
            volume = 1.0f;
        }
    }

    // 載入音效文件並將其映射到相應的名稱
    private static void loadSound(int resourceId, String soundName) {
        soundMap.put(soundName, soundPool.load(context, resourceId, 1));
    }

    // 播放音效
    public static void playSound(String soundName) {
        if (!isMuted) {
            soundPool.play(soundMap.get(soundName), volume, volume, 0, 0, 1);
        }
    }

    // 設置靜音狀態
    public static void setMute(boolean mute) {
        isMuted = mute;
        // 如果是靜音，音量為 0，否則將音量設置為原始水平
        if (isMuted) {
            volume = 0f;
        } else {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            } else {
                volume = 1.0f;
            }
        }
    }

    // 獲取靜音狀態
    public static boolean isMuted() {
        return isMuted;
    }
}
