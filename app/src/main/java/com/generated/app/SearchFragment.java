package com.generated.app;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText searchEditText;
    private ListView searchListView;
    private LinearLayout chipsLayout;
    private String currentGenre = "הכל";
    private String currentQuery = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchEditText = view.findViewById(R.id.search_edit_text);
        searchListView = view.findViewById(R.id.search_list_view);
        chipsLayout = view.findViewById(R.id.search_chips_layout);

        setupChips();
        setupSearch();
        updateResults();

        return view;
    }

    private void setupChips() {
        String[] genres = {"הכל", "פופ", "מזרחי", "ישראלי", "רוק", "עולמי"};
        float density = getResources().getDisplayMetrics().density;

        for (final String genre : genres) {
            final TextView chip = new TextView(getActivity());
            chip.setText(genre);
            chip.setTextColor(0xFFFFFFFF);
            chip.setTextSize(14sp);
            chip.setPadding((int)(16*density), (int)(8*density), (int)(16*density), (int)(8*density));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, (int)(8*density), 0);
            chip.setLayoutParams(lp);

            if (genre.equals(currentGenre)) {
                chip.setBackgroundResource(R.drawable.bg_pill_active);
            } else {
                chip.setBackgroundResource(R.drawable.bg_pill);
            }

            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentGenre = genre;
                    setupChips(); // refresh styling
                    updateResults();
                }
            });

            chipsLayout.addView(chip);
        }
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentQuery = s.toString();
                updateResults();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void updateResults() {
        final List<Song> results = MusicRepository.getInstance().search(currentQuery, currentGenre);
        SongAdapter adapter = new SongAdapter(getActivity(), results, new SongAdapter.OnSongActionCallback() {
            @Override
            public void onLikeClicked(Song song) {
                // update liked
            }
        });
        searchListView.setAdapter(adapter);

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicPlayerManager.getInstance().playSong(results, position);
            }
        });
    }
}
