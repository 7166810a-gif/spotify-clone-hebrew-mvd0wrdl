package com.generated.app;

import java.io.Serializable;

public class Song implements Serializable {
    private String id;
    private String title;
    private String artist;
    private String genre;
    private int durationSeconds;
    private String audioUrl;
    private int coverColor;
    private boolean isLiked;
    private int playCount;
    private long lastPlayedTimestamp;

    public Song(String id, String title, String artist, String genre, int durationSeconds, String audioUrl, int coverColor) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.durationSeconds = durationSeconds;
        this.audioUrl = audioUrl;
        this.coverColor = coverColor;
        this.isLiked = false;
        this.playCount = 0;
        this.lastPlayedTimestamp = 0;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getGenre() { return genre; }
    public int getDurationSeconds() { return durationSeconds; }
    public String getAudioUrl() { return audioUrl; }
    public int getCoverColor() { return coverColor; }
    public boolean isLiked() { return isLiked; }
    public void setLiked(boolean liked) { isLiked = liked; }
    public int getPlayCount() { return playCount; }
    public void incrementPlayCount() { 
        this.playCount++;
        this.lastPlayedTimestamp = System.currentTimeMillis();
    }
    public long getLastPlayedTimestamp() { return lastPlayedTimestamp; }
}
