package com.that2u.android.app.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by phama on 2016-07-03.
 */
public class SoundUtil {
    private static MediaPlayer sDefaultMediaPlayer = null;
    private static String sCurrentMediaPath = "";

    private synchronized static void playSound(MediaPlayer mediaPlayer, AssetFileDescriptor afd) throws IOException {
        if (mediaPlayer != null) {
            try {
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.reset();
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mediaPlayer.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } finally {
                afd.close();
            }
            mediaPlayer.start();
        }
    }

    public static void playSoundFromAsset(final Context context, final String fileName) {
        try {
            if (sDefaultMediaPlayer != null && sCurrentMediaPath.equals(fileName)) {
                if(sDefaultMediaPlayer.isPlaying()){
                    sDefaultMediaPlayer.stop();
                }
                sDefaultMediaPlayer.start();
            } else if (!TextUtils.isEmpty(fileName)) {
                if (sDefaultMediaPlayer == null) {
                    sDefaultMediaPlayer = new MediaPlayer();
                }

                sCurrentMediaPath = fileName;
                try {
                    AssetFileDescriptor afd = context.getAssets().openFd(fileName);
                    playSound(sDefaultMediaPlayer, afd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playSoundFromAssetWithRelease(final Context context, final String fileName) {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.release();
                }
            });
            AssetFileDescriptor afd = context.getAssets().openFd(fileName);
            playSound(mediaPlayer, afd);

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static MediaPlayer playSoundFromAssetWithoutRelease(final Context context, final String fileName) {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            AssetFileDescriptor afd = context.getAssets().openFd(fileName);
            playSound(mediaPlayer, afd);

            return mediaPlayer;
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static MediaPlayer playSoundFromRaw(final Context context, final String fileName) {

        int resID = context.getResources().getIdentifier(fileName, "raw", context.getPackageName());

        if (resID > 0) {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, resID);
            mediaPlayer.start();

            return mediaPlayer;
        }
        return null;
    }

    public static MediaPlayer playSoundFromAssetWithoutRelease(final Context context, final List<String> names, final OnFilePlayListener listener, final boolean isLoop) {
        if (names != null && !names.isEmpty()) {
            try {
                final int nameCnt = names.size();
                Uri file = Uri.parse(names.get(0));

                MediaPlayer mp = new MediaPlayer();
                mp.setDataSource(context, file);
                mp.prepare();

                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    int currentTrack = 0;

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (listener != null) {
                            listener.onFileEnd(currentTrack, names.get(currentTrack));
                        }

                        currentTrack = (currentTrack + 1) % nameCnt;

                        if (!isLoop && currentTrack == nameCnt) {
                            mp.stop();
                        }

                        Uri nextTrack = Uri.parse(names.get(currentTrack));
                        try {
                            mp.setDataSource(context, nextTrack);
                            mp.prepare();

                            if (listener != null) {
                                listener.onFileStart(currentTrack, names.get(currentTrack));
                            }

                            mp.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });

                return mp;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void stop(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public static int pause(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public static void resume(MediaPlayer mediaPlayer, int position) {
        if (mediaPlayer != null && position >= 0) {
            mediaPlayer.seekTo(position);
            mediaPlayer.start();
        }
    }

    public static void release() {
        if (sDefaultMediaPlayer != null) {
            sDefaultMediaPlayer.release();
        }
    }

    public interface OnFilePlayListener {
        void onFileStart(int index, String name);

        void onFileEnd(int index, String name);
    }
}
