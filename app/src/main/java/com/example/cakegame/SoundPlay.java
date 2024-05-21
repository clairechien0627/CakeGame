package com.example.cakegame;

import android.content.*;
import android.media.*;
import java.util.*;

public class SoundPlay {

    private SoundPool soundPool;
    private HashMap<String, Integer> soundMap = new HashMap<>();

    public SoundPlay(Context context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();

        soundMap.put("choose", soundPool.load(context, R.raw.choose, 1));


    }

    public void getSound(String name) {
        soundPool.play(soundMap.get(name), 1, 1, 0, 0, 1);
    }

}
