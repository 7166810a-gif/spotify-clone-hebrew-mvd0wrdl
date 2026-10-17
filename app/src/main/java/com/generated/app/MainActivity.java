package com.generated.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity implements MusicPlayerManager.PlayerListener {

    private LinearLayout navHome, navSearch, navLibrary;
    private LinearLayout miniPlayerLayout;
    private ImageView miniCover;
    private TextView miniTitle, miniArtist;
    private ImageButton miniBtnPlay, miniBtnLike;

    private RelativeLayout fullscreenPlayer;
    private ImageButton playerBtnDown, playerBtnPlay, playerBtnPrev, playerBtnNext, playerBtnLike;
    private ImageView playerCover;
    private TextView playerTitle, playerArtist, playerCurrentTime, playerTotalTime;
    private SeekBar playerSeekBar;

    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private LibraryFragment libraryFragment;

    private Handler progressHandler = new Handler(Looper.getMainLooper());
    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            MusicPlayerManager player = MusicPlayerManager.getInstance();
            if (player.isPlaying()) {
                int current = player.getCurrentPosition();
                int duration = player.getDuration();
                if (duration > 0) {
                    playerSeekBar.setMax(duration);
                    playerSeekBar.setProgress(current);
                    playerCurrentTime.setText(formatTime(current));
                    playerTotalTime.setText(formatTime(duration));
                }
            }
            progressHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupNavigation();
        setupPlayerControls();

        MusicPlayerManager.getInstance().addListener(this);

        // Load default Home fragment
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        libraryFragment = new LibraryFragment();

        switchFragment(homeFragment);
        progressHandler.post(progressRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicPlayerManager.getInstance().removeListener(this);
        progressHandler.removeCallbacks(progressRunnable);
    }

    private void initViews() {
        navHome = findViewById(R.id.nav_home);
        navSearch = findViewById(R.id.nav_search);
        navLibrary = findViewById(R.id.nav_library);

        miniPlayerLayout = findViewById(R.id.mini_player_layout);
        miniCover = findViewById(R.id.mini_cover);
        miniTitle = findViewById(R.id.mini_title);
        miniArtist = findViewById(R.id.mini_artist);
        miniBtnPlay = findViewById(R.id.mini_btn_play);
        miniBtnLike = findViewById(R.id.mini_btn_like);

        fullscreenPlayer = findViewById(R.id.fullscreen_player);
        playerBtnDown = findViewById(R.id.player_btn_down);
        playerBtnPlay = findViewById(R.id.player_btn_play);
        playerBtnPrev = findViewById(R.id.player_btn_prev);
        playerBtnNext = findViewById(R.id.player_btn_next);
        playerBtnLike = findViewById(R.id.player_btn_like);
        playerCover = findViewById(R.id.player_cover);
        playerTitle = findViewById(R.id.player_title);
        playerArtist = findViewById(R.id.player_artist);
        playerCurrentTime = findViewById(R.id.player_current_time);
        playerTotalTime = findViewById(R.id.player_total_time);
        playerSeekBar = findViewById(R.id.player_seekbar);
    }

    private void setupNavigation() {
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(homeFragment);
                updateNavTabs(0);
            }
        });

        navSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(searchFragment);
                updateNavTabs(1);
            }
        });

        navLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(libraryFragment);
                updateNavTabs(2);
            }
        });
    }

    private void updateNavTabs(int tabIndex) {
        TextView tHome = (TextView) navHome.getChildAt(0);
        TextView tSearch = (TextView) navSearch.getChildAt(0);
        TextView tLibrary = (TextView) navLibrary.getChildAt(0);

        int activeColor = 0xFF1DB954;
        int inactiveColor = 0xFFB3B3B3;

        tHome.setTextColor(tabIndex == 0 ? activeColor : inactiveColor);
        tSearch.setTextColor(tabIndex == 1 ? activeColor : inactiveColor);
        tLibrary.setTextColor(tabIndex == 2 ? activeColor : inactiveColor);
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void setupPlayerControls() {
        View.OnClickListener playPauseListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayerManager.getInstance().togglePlayPause();
            }
        };

        miniBtnPlay.setOnClickListener(playPauseListener);
        playerBtnPlay.setOnClickListener(playPauseListener);

        miniPlayerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullscreenPlayer.setVisibility(View.VISIBLE);
            }
        });

        playerBtnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullscreenPlayer.setVisibility(View.GONE);
            }
        });

        playerBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayerManager.getInstance().previous();
            }
        });

        playerBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayerManager.getInstance().next();
            }
        });

        View.OnClickListener likeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Song current = MusicPlayerManager.getInstance().getCurrentSong();
                if (current != null) {
                    current.setLiked(!current.isLiked());
                    updateLikeUI(current);
                }
            }
        };

        miniBtnLike.setOnClickListener(likeListener);
        playerBtnLike.setOnClickListener(likeListener);

        playerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MusicPlayerManager.getInstance().seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateLikeUI(Song song) {
        if (song.isLiked()) {
            miniBtnLike.setImageResource(android.R.drawable.btn_star_big_on);
            playerBtnLike.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            miniBtnLike.setImageResource(android.R.drawable.btn_star_big_off);
            playerBtnLike.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }

    private String formatTime(int ms) {
        int seconds = (ms / 1000) % 60;
        int minutes = (ms / (1000 * 60)) % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public void onSongChanged(Song song) {
        if (song == null) return;
        miniPlayerLayout.setVisibility(View.VISIBLE);
        miniTitle.setText(song.getTitle());
        miniArtist.setText(song.getArtist());

        playerTitle.setText(song.getTitle());
        playerArtist.setText(song.getArtist());

        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setColor(song.getCoverColor());
        float density = getResources().getDisplayMetrics().density;
        gd.setCornerRadius(8 * density);
        miniCover.setBackground(gd);

        GradientDrawable gdLarge = new GradientDrawable();
        gdLarge.setShape(GradientDrawable.RECTANGLE);
        gdLarge.setColor(song.getCoverColor());
        gdLarge.setCornerRadius(16 * density);
        playerCover.setBackground(gdLarge);

        updateLikeUI(song);
    }

    @Override
    public void onPlaybackStateChanged(boolean isPlaying) {
        if (isPlaying) {
            miniBtnPlay.setImageResource(android.R.drawable.ic_media_pause);
            playerBtnPlay.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            miniBtnPlay.setImageResource(android.R.drawable.ic_media_play);
            playerBtnPlay.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    @Override
    public void onProgressUpdate(int currentPosition, int duration) {
        // Handled by Handler runnable
    }
}
