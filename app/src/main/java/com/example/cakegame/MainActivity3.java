package com.example.cakegame;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity implements OrderFragment.OnDialogButtonFragmentListener, ModeFragment.OnDialogButtonFragmentListener {

    public static ScoreBoard scoreBoard = new ScoreBoard();
    List<Ranking> rankingList = new ArrayList<>();
    RankingAdapter adapter = new RankingAdapter(this, rankingList);
    private int mode;
    private int order;
    public static VibrationHelper vibrationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        vibrationHelper = VibrationHelper.getInstance(this);

        mode = 0;
        order = 0;

        ListView listView = findViewById(R.id.ranking_list_view);
        ArrayList<ScoreBoard.Score> scoreArrayList = scoreBoard.getScoreBoard_score(mode);
        for (ScoreBoard.Score score : scoreArrayList) {
            rankingList.add(new Ranking(score.getRank(), score.getScore(), score.getFullCake()));
            Log.d("CakeSort", score.getRank() + " " + score.getScore() + " " + score.getFullCake());
        }

        // Add more rankings as needed

        listView.setAdapter(adapter);

        ImageView closeBottom = findViewById(R.id.window_close);
        closeBottom.setOnClickListener(v -> {
            vibrationHelper.vibrate();
            Intent intent = new Intent(MainActivity3.this, MainActivity.class);
            startActivity(intent);
        });

        Button orderButton = findViewById(R.id.order);
        orderButton.setOnClickListener(v -> {
            vibrationHelper.vibrate();
            OrderFragment orderFragment = new OrderFragment();
            orderFragment.setOrderListener(MainActivity3.this);
            orderFragment.show(getSupportFragmentManager(), orderFragment.getTag());
        });

        Button modeButton = findViewById(R.id.mode);
        modeButton.setOnClickListener(v -> {
            vibrationHelper.vibrate();
            ModeFragment modeFragment = new ModeFragment();
            modeFragment.setModeListener(MainActivity3.this);
            modeFragment.show(getSupportFragmentManager(), modeFragment.getTag());
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onSelectDialog(String select) {
        Log.d("CakeSort", select);
        Toast.makeText(this, "Select " + select, Toast.LENGTH_SHORT).show();
        TextView orderText = findViewById(R.id.orderText);
        TextView modeText = findViewById(R.id.modeText);
        rankingList.clear();

        if (select.equals("Easy")) {
            mode = 0;
            modeText.setText("Easy");
        } else if (select.equals("Normal")) {
            mode = 1;
            modeText.setText("Normal");
        } else if (select.equals("Hard")) {
            mode = 2;
            modeText.setText("Hard");
        } else if (select.equals("Devil")) {
            mode = 3;
            modeText.setText("Devil");
        } else if (select.equals("Score")) {
            order = 0;
            orderText.setText("Score");
        } else if (select.equals("Cake")) {
            order = 1;
            orderText.setText("Cake");
        }

        if (order == 0) {
            ArrayList<ScoreBoard.Score> scoreArrayList = scoreBoard.getScoreBoard_score(mode);
            for (ScoreBoard.Score score : scoreArrayList) {
                rankingList.add(new Ranking(score.getRank(), score.getScore(), score.getFullCake()));
            }
        } else if (order == 1) {
            ArrayList<ScoreBoard.Score> scoreArrayList = scoreBoard.getScoreBoard_cake(mode);
            for (ScoreBoard.Score score : scoreArrayList) {
                rankingList.add(new Ranking(score.getRank(), score.getScore(), score.getFullCake()));
            }
        }
        adapter.notifyDataSetChanged();
    }
}
