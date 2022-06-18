package com.that2u.android.app.utils;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseSoundPool {
    private static final int MAX_STREAM = 8;
    private static final int STREAM_AUDIO_TYPE = AudioManager.STREAM_SYSTEM;

    private SoundPool soundPool;
    private Map<Integer, Boolean> loadedStatusMap = new HashMap<>();
    private Map<Integer, Integer> loadedIdMap = new HashMap<>();

    public void initialize(Activity context, Map<Integer, Integer> rawResIdPriorityMap){
        try {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }

        if(soundPool != null){
            soundPool.setOnLoadCompleteListener(
                    (soundPool, sampleId, status) -> loadedStatusMap.put(sampleId, true));

            if(rawResIdPriorityMap != null && !rawResIdPriorityMap.isEmpty()){
                for(int resId : rawResIdPriorityMap.keySet()){
                    int priority = rawResIdPriorityMap.get(resId);
                    if(priority <= 0){
                        priority = 1;
                    }

                    int sampleId = soundPool.load(context, resId, priority);
                    if(sampleId > 0){
                        loadedIdMap.put(resId, sampleId);
                    }
                }
            }
        }
    }

    public void destroy(){
        if(soundPool != null){
            soundPool.release();
            soundPool = null;
        }
    }

    public void play(int resId){
        if(soundPool != null) {
            if (loadedIdMap.containsKey(resId)) {
                int sampleId = loadedIdMap.get(resId);
                if (sampleId > 0 && loadedStatusMap.containsKey(sampleId)) {
                    soundPool.play(sampleId, 1f, 1f, 1, 0, 1f);
                }
            }
        }
    }
}
