package com.example.cakegame;

import android.annotation.SuppressLint;
import android.content.*;
import android.media.*;
import java.util.*;

public class SoundPlay {

    private static SoundPool soundPool;
    private static final HashMap<String, Integer> soundMap = new HashMap<>();
    private static float volume;
    private static boolean isMuted = false;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public SoundPlay(Context context) {
        SoundPlay.context = context;
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();

        soundMap.put("choose", soundPool.load(context, R.raw.choose, 1));
        soundMap.put("full", soundPool.load(context, R.raw.full, 1));
        soundMap.put("add", soundPool.load(context, R.raw.add, 1));
        soundMap.put("end", soundPool.load(context, R.raw.end, 1));
        soundMap.put("start", soundPool.load(context, R.raw.start, 1));
        soundMap.put("click", soundPool.load(context, R.raw.click, 1));

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (audioManager != null) {
            volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        } else {
            volume = 1.0f;
        }
    }

    public static void playSound(String soundName) {
        soundPool.play(soundMap.get(soundName), volume, volume, 0, 0, 1);
    }

    public static void setMute(boolean mute) {
        isMuted = mute;
        if (isMuted) {
            volume = 0f;
        } else {
            // Reset the volume to the original level
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            } else {
                volume = 1.0f;
            }
        }
    }

}