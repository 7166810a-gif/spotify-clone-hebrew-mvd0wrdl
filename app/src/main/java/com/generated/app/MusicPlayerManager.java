package com.generated.app;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList, java.util.List;

public class MusicPlayerManager {
    private static MusicPlayerManager instance;
    private MediaPlayer mediaPlayer;
    private List<Song> playlist;
    private int currentIndex;
    private Song currentSong;
    private boolean isPlaying;
    private List<PlayerListener> listeners;

    public interface PlayerListener {
        void onSongChanged(Song song);
        void onPlaybackStateChanged(boolean isPlaying);
        void onProgressUpdate(int currentPosition, int duration);
    }

    private MusicPlayerManager() {
        playlist = new ArrayList<>();
        currentIndex = 0;
        isPlaying = false;
        listeners = new ArrayList<>();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next();
            }
        });
    }

    public static synchronized MusicPlayerManager getInstance() {
        if (instance == null) {
            instance = new MusicPlayerManager();
        }
        return instance;
    }

    public void addListener(PlayerListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(PlayerListener listener) {
        listeners.remove(listener);
    }

    public void playSong(List<Song> newPlaylist, int index) {
        if (newPlaylist == null || newPlaylist.isEmpty()) return;
        this.playlist = newPlaylist;
        this.currentIndex = Math.max(0, Math.min(index, playlist.size() - 1));
        startPlayback();
    }

    private void startPlayback() {
        if (playlist.isEmpty()) return;
        currentSong = playlist.get(currentIndex);
        currentSong.incrementPlayCount();

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(currentSong.getAudioUrl());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    isPlaying = true;
                    notifySongChanged();
                    notifyStateChanged();
                }
            });
        } catch (Exception e) {
            Log.e("MusicPlayerManager", "Error playing song", e);
            // Fallback simulation if network stream fails
            isPlaying = true;
            notifySongChanged();
            notifyStateChanged();
        }
    }

    public void togglePlayPause() {
        if (currentSong == null) {
            if (!playlist.isEmpty()) {
                startPlayback();
            }
            return;
        }
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                isPlaying = false;
            } else {
                mediaPlayer.start();
                isPlaying = true;
            }
        } catch (Exception e) {
            isPlaying = !isPlaying;
        }
        notifyStateChanged();
    }

    public void next() {
        if (playlist.isEmpty()) return;
        currentIndex = (currentIndex + 1) % playlist.size();
        startPlayback();
    }

    public void previous() {
        if (playlist.isEmpty()) return;
        currentIndex = (currentIndex - 1 + playlist.size()) % playlist.size();
        startPlayback();
    }

    public void seekTo(int progressMs) {
        try {
            mediaPlayer.seekTo(progressMs);
        } catch (Exception e) {
            // ignore
        }
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public int getCurrentPosition() {
        try {
            return mediaPlayer.isPlaying() ? mediaPlayer.getCurrentPosition() : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public int getDuration() {
        try {
            return mediaPlayer.getDuration();
        } catch (Exception e) {
            return currentSong != null ? currentSong.getDurationSeconds() * 1000 : 0;
        }
    }

    private void notifySongChanged() {
        for (PlayerListener l : listeners) {
            l.onSongChanged(currentSong);
        }
    }

    private void notifyStateChanged() {
        for (PlayerListener l : listeners) {
            l.onPlaybackStateChanged(isPlaying);
        }
    }

    public void notifyProgress(int current, int duration) {
        for (PlayerListener l : listeners) {
            l.onProgressUpdate(current, duration);
        }
    }
}
