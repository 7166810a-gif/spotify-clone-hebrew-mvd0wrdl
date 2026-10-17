package com.generated.app;

import android.app.Fragment;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class HomeFragment extends Fragment {

    private GridLayout homeGrid;
    private LinearLayout horizontalRecs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homeGrid = view.findViewById(R.id.home_grid);
        horizontalRecs = view.findViewById(R.id.home_horizontal_recommendations);

        loadData();
        return view;
    }

    public void loadData() {
        if (homeGrid == null || horizontalRecs == null) return;
        homeGrid.removeAllViews();
        horizontalRecs.removeAllViews();

        List<Song> songs = MusicRepository.getInstance().getAllSongs();
        float density = getResources().getDisplayMetrics().density;
        int cardWidth = (int) (160 * density);

        // Populate 2x3 Grid with top songs (Quick access like Spotify)
        for (int i = 0; i < Math.min(6, songs.size()); i++) {
            final Song song = songs.get(i);
            View card = LayoutInflater.from(getActivity()).inflate(R.layout.item_song, homeGrid, false);
            
            // Customize layout parameters for grid cell
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = (int) (60 * density);
            params.columnSpec = GridLayout.spec(i % 2, 1f);
            params.rowSpec = GridLayout.spec(i / 2);
            params.setMargins((int)(4*density), (int)(4*density), (int)(4*density), (int)(4*density));
            card.setLayoutParams(params);

            ImageView cover = card.findViewById(R.id.item_cover);
            TextView title = card.findViewById(R.id.item_title);
            TextView artist = card.findViewById(R.id.item_artist);
            ImageButton btnLike = card.findViewById(R.id.item_btn_like);

            title.setText(song.getTitle());
            artist.setText(song.getArtist());

            GradientDrawable gd = new GradientDrawable();
            gd.setShape(GradientDrawable.RECTANGLE);
            gd.setColor(song.getCoverColor());
            gd.setCornerRadius(6 * density);
            cover.setBackground(gd);

            if (song.isLiked()) {
                btnLike.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                btnLike.setImageResource(android.R.drawable.btn_star_big_off);
            }

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    song.setLiked(!song.isLiked());
                    loadData();
                }
            });

            final int index = i;
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MusicPlayerManager.getInstance().playSong(MusicRepository.getInstance().getAllSongs(), index);
                }
            });

            homeGrid.addView(card);
        }

        // Populate Horizontal Recommendation Cards (AI smart mix)
        List<Song> smartList = MusicRepository.getInstance().getSmartRecommendations();
        for (int i = 0; i < smartList.size(); i++) {
            final Song song = smartList.get(i);
            LinearLayout itemLayout = new LinearLayout(getActivity());
            itemLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(cardWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, (int)(12 * density), 0);
            itemLayout.setLayoutParams(lp);
            itemLayout.setPadding(8, 8, 8, 8);
            itemLayout.setBackgroundResource(R.drawable.bg_rounded_card);

            ImageView img = new ImageView(getActivity());
            LinearLayout.LayoutParams imgLp = new LinearLayout.LayoutParams(cardWidth - 16, cardWidth - 16);
            img.setLayoutParams(imgLp);
            GradientDrawable gd = new GradientDrawable();
            gd.setShape(GradientDrawable.RECTANGLE);
            gd.setColor(song.getCoverColor());
            gd.setCornerRadius(8 * density);
            img.setBackground(gd);

            TextView tvTitle = new TextView(getActivity());
            tvTitle.setText(song.getTitle());
            tvTitle.setTextColor(0xFFFFFFFF);
            tvTitle.setTextSize(14sp);
            tvTitle.setSingleLine(true);
            tvTitle.setPadding(0, 8, 0, 2);

            TextView tvArtist = new TextView(getActivity());
            tvArtist.setText(song.getArtist());
            tvArtist.setTextColor(0xFFB3B3B3);
            tvArtist.setTextSize(12sp);
            tvArtist.setSingleLine(true);

            itemLayout.addView(img);
            itemLayout.addView(tvTitle);
            itemLayout.addView(tvArtist);

            final int finalIndex = i;
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MusicPlayerManager.getInstance().playSong(MusicRepository.getInstance().getSmartRecommendations(), finalIndex);
                }
            });

            horizontalRecs.addView(itemLayout);
        }
    }
}
