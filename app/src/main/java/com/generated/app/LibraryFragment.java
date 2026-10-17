package com.generated.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

public class LibraryFragment extends Fragment {

    private ListView listView;
    private TextView likedCountText;
    private LinearLayout likedCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        listView = view.findViewById(R.id.library_list_view);
        likedCountText = view.findViewById(R.id.library_liked_count);
        likedCard = view.findViewById(R.id.library_liked_card);

        likedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Song> likedSongs = MusicRepository.getInstance().getLikedSongs();
                if (!likedSongs.isEmpty()) {
                    MusicPlayerManager.getInstance().playSong(likedSongs, 0);
                }
            }
        });

        loadLikedSongs();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLikedSongs();
    }

    public void loadLikedSongs() {
        final List<Song> likedSongs = MusicRepository.getInstance().getLikedSongs();
        likedCountText.setText(likedSongs.size() + " שירים אהובים");

        SongAdapter adapter = new SongAdapter(getActivity(), likedSongs, new SongAdapter.OnSongActionCallback() {
            @Override
            public void onLikeClicked(Song song) {
                loadLikedSongs();
            }
        });
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicPlayerManager.getInstance().playSong(likedSongs, position);
            }
        });
    }
}
