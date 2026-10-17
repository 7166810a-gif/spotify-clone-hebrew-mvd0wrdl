package com.generated.app;

import android.graphics.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MusicRepository {
    private static MusicRepository instance;
    private List<Song> allSongs;

    private MusicRepository() {
        allSongs = new ArrayList<>();
        // Free open public domain / sample audio loops for fully working playback
        String sampleUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
        String sampleUrl2 = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3";
        String sampleUrl3 = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3";
        String sampleUrl4 = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3";
        String sampleUrl8 = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3";

        // Rich catalog in Hebrew with diverse genres to power the Recommendation Engine
        allSongs.add(new Song("1", "תל אביב בלילה", "עומר אדם", "פופ", 210, sampleUrl, Color.parseColor("#E91E63")));
        allSongs.add(new Song("2", "רוקד בחושך", "אייל גולן", "מזרחי", 195, sampleUrl2, Color.parseColor("#9C27B0")));
        allSongs.add(new Song("3", "שיר השקט", "שלמה ארצי", "ישראלי", 240, sampleUrl3, Color.parseColor("#3F51B5")));
        allSongs.add(new Song("4", "מלכת הלבבות", "נועה קירל", "פופ", 180, sampleUrl4, Color.parseColor("#00BCD4")));
        allSongs.add(new Song("5", "רוקנרול ישראלי", "יהודה פוליקר", "רוק", 260, sampleUrl8, Color.parseColor("#4CAF50")));
        allSongs.add(new Song("6", "לילה לבן", "אביב גפן", "רוק", 215, sampleUrl, Color.parseColor("#FF9800")));
        allSongs.add(new Song("7", "שמש קורנת", "עידן רייכל", "עולמי", 200, sampleUrl2, Color.parseColor("#795548")));
        allSongs.add(new Song("8", "קצב מדבר", "פאר טסי", "מזרחי", 220, sampleUrl3, Color.parseColor("#607D8B")));
        allSongs.add(new Song("9", "חלומות פז", "סטטיק", "פופ", 190, sampleUrl4, Color.parseColor("#E53935")));
        allSongs.add(new Song("10", "נשימה עמוקה", "אריק איינשטיין", "ישראלי", 230, sampleUrl8, Color.parseColor("#8E24AA")));
    }

    public static synchronized MusicRepository getInstance() {
        if (instance == null) {
            instance = new MusicRepository();
        }
        return instance;
    }

    public List<Song> getAllSongs() {
        return allSongs;
    }

    public List<Song> getLikedSongs() {
        List<Song> liked = new ArrayList<>();
        for (Song s : allSongs) {
            if (s.isLiked()) {
                liked.add(s);
            }
        }
        return liked;
    }

    public List<Song> getSmartRecommendations() {
        // AI Logic: Rank songs by liked status, play count, and recent interactions
        List<Song> ranked = new ArrayList<>(allSongs);
        Collections.sort(ranked, new Comparator<Song>() {
            @Override
            public int compare(Song s1, Song s2) {
                int score1 = (s1.isLiked() ? 10 : 0) + (s1.getPlayCount() * 3) + (int)(s1.getLastPlayedTimestamp() / 100000);
                int score2 = (s2.isLiked() ? 10 : 0) + (s2.getPlayCount() * 3) + (int)(s2.getLastPlayedTimestamp() / 100000);
                return Integer.compare(score2, score1);
            }
        });
        return ranked;
    }

    public List<Song> search(String query, String genreFilter) {
        List<Song> result = new ArrayList<>();
        String q = query == null ? "" : query.toLowerCase().trim();
        for (Song s : allSongs) {
            boolean matchesQuery = q.isEmpty() || s.getTitle().toLowerCase().contains(q) || s.getArtist().toLowerCase().contains(q);
            boolean matchesGenre = genreFilter == null || genreFilter.isEmpty() || genreFilter.equals("הכל") || s.getGenre().equals(genreFilter);
            if (matchesQuery && matchesGenre) {
                result.add(s);
            }
        }
        return result;
    }
}
