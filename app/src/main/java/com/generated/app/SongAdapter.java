package com.generated.app;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class SongAdapter extends BaseAdapter {
    private Context context;
    private List<Song> songs;
    private OnSongActionCallback callback;

    public interface OnSongActionCallback {
        void onLikeClicked(Song song);
    }

    public SongAdapter(Context context, List<Song> songs, OnSongActionCallback callback) {
        this.context = context;
        this.songs = songs;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        }

        final Song song = songs.get(position);

        ImageView coverImage = convertView.findViewById(R.id.item_cover);
        TextView titleText = convertView.findViewById(R.id.item_title);
        TextView artistText = convertView.findViewById(R.id.item_artist);
        ImageButton btnLike = convertView.findViewById(R.id.item_btn_like);

        titleText.setText(song.getTitle());
        artistText.setText(song.getArtist());

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(song.getCoverColor());
        drawable.setCornerRadius(8 * context.getResources().getDisplayMetrics().density);
        coverImage.setBackground(drawable);

        if (song.isLiked()) {
            btnLike.setImageResource(android.R.drawable.btn_star_big_on);
            btnLike.setColorFilter(context.getResources().getColor(android.R.color.holo_green_light));
        } else {
            btnLike.setImageResource(android.R.drawable.btn_star_big_off);
            btnLike.clearColorFilter();
        }

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                song.setLiked(!song.isLiked());
                if (callback != null) {
                    callback.onLikeClicked(song);
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
