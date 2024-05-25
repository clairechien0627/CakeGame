package com.example.cakegame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RankingAdapter extends BaseAdapter {
    private Context context;
    private List<Ranking> rankingList;

    public RankingAdapter(Context context, List<Ranking> rankingList) {
        this.context = context;
        this.rankingList = rankingList;
    }

    @Override
    public int getCount() {
        return rankingList.size();
    }

    @Override
    public Object getItem(int position) {
        return rankingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ranking_item, parent, false);
        }

        TextView rankTextView = convertView.findViewById(R.id.rank_text_view);
        TextView scoreTextView = convertView.findViewById(R.id.score_text_view);
        TextView fullcakeTextView = convertView.findViewById(R.id.fullCake_text_view);

        Ranking ranking = rankingList.get(position);

        rankTextView.setText(String.valueOf(ranking.getRank()));
        scoreTextView.setText(String.valueOf(ranking.getScore()));
        fullcakeTextView.setText(String.valueOf(ranking.getFullcake()));

        return convertView;
    }
}


