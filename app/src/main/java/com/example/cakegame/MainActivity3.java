package com.example.cakegame;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
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
    public static Vibrator vibrator;
    public static int vibrateTime = 15;
    private String from;
    private int currentIndex;
    private int currentMode;
    private int currentNum = 0;
    private ListView listView;
    private View itemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        listView = findViewById(R.id.ranking_list_view);

        Intent intentFrom = getIntent();
        from = intentFrom.getStringExtra("from");
        order = 0;
        mode = 0;
        if (from != null && from.equals("MainActivity2")) {
            ArrayList<ScoreBoard.Score> numArrayList = scoreBoard.getScoreBoard_num();
            mode = numArrayList.get(numArrayList.size() - 1).getMode();
            currentMode = mode;
            currentNum = numArrayList.get(numArrayList.size() - 1).getNum();
        }


        TextView modeText = findViewById(R.id.modeText);
        if (mode == 0) {
            modeText.setText("Easy");
        } else if (mode == 1) {
            modeText.setText("Normal");
        } else if (mode == 2) {
            modeText.setText("Hard");
        } else if (mode == 3) {
            modeText.setText("Devil");
        }

        ArrayList<ScoreBoard.Score> scoreArrayList = scoreBoard.getScoreBoard_score(mode);
        for (int i = 0; i < scoreArrayList.size(); i++) {
            ScoreBoard.Score score = scoreArrayList.get(i);
            rankingList.add(new Ranking(score.getRank(), score.getScore(), score.getFullCake()));
            Log.d("CakeSort", score.getRank() + " " + score.getScore() + " " + score.getFullCake());
            if(score.getNum() == currentNum){
                currentIndex = i;
            }
        }
        if (from != null && from.equals("MainActivity2")) {
            listView.smoothScrollToPositionFromTop(currentIndex, 10);
            listView.postDelayed(() -> {
                itemView = listView.getChildAt(currentIndex - listView.getFirstVisiblePosition());
                if (itemView != null) {
                    itemView.setBackgroundResource(R.color.blue_light);
                }
            }, 500);
        }

        // Add more rankings as needed
        listView.setAdapter(adapter);

        ImageView closeBottom = findViewById(R.id.window_close);
        closeBottom.setOnClickListener(v -> {
            resetItemViewBackgrounds();
            vibrator.vibrate(vibrateTime);
            Intent intent = new Intent(MainActivity3.this, MainActivity.class);
            startActivity(intent);
        });

        Button orderButton = findViewById(R.id.order);
        orderButton.setOnClickListener(v -> {
            vibrator.vibrate(vibrateTime);
            OrderFragment orderFragment = new OrderFragment();
            orderFragment.setOrderListener(MainActivity3.this);
            orderFragment.show(getSupportFragmentManager(), orderFragment.getTag());
        });

        Button modeButton = findViewById(R.id.mode);
        modeButton.setOnClickListener(v -> {
            vibrator.vibrate(vibrateTime);
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
        Intent intentFrom = getIntent();
        from = intentFrom.getStringExtra("from");
        currentIndex = 0;
        resetItemViewBackgrounds();

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
            for (int i = 0; i < scoreArrayList.size(); i++) {
                ScoreBoard.Score score = scoreArrayList.get(i);
                rankingList.add(new Ranking(score.getRank(), score.getScore(), score.getFullCake()));
                Log.d("CakeSort", score.getRank() + " " + score.getScore() + " " + score.getFullCake());
                if(score.getNum() == currentNum){
                    currentIndex = i;
                }
            }
            if (from != null && from.equals("MainActivity2") && mode == currentMode) {
                listView.smoothScrollToPositionFromTop(currentIndex, 10);
                listView.postDelayed(() -> {
                    View itemView = listView.getChildAt(currentIndex - listView.getFirstVisiblePosition());
                    if (itemView != null) {
                        itemView.setBackgroundResource(R.color.blue_light);
                    }
                }, 500);
            }
        } else if (order == 1) {
            ArrayList<ScoreBoard.Score> scoreArrayList = scoreBoard.getScoreBoard_cake(mode);
            for (int i = 0; i < scoreArrayList.size(); i++) {
                ScoreBoard.Score score = scoreArrayList.get(i);
                rankingList.add(new Ranking(score.getRank(), score.getScore(), score.getFullCake()));
                Log.d("CakeSort", score.getRank() + " " + score.getScore() + " " + score.getFullCake());
                if(score.getNum() == currentNum){
                    currentIndex = i;
                }
            }
            if (from != null && from.equals("MainActivity2") && mode == currentMode) {
                listView.smoothScrollToPositionFromTop(currentIndex, 10);
                listView.postDelayed(() -> {
                    View itemView = listView.getChildAt(currentIndex - listView.getFirstVisiblePosition());
                    if (itemView != null) {
                        itemView.setBackgroundResource(R.color.blue_light);
                    }
                }, 500);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void resetItemViewBackgrounds() {
        for (int i = 0; i < listView.getChildCount(); i++) {
            View itemView = listView.getChildAt(i);
            if (itemView != null) {
                itemView.setBackgroundResource(0); // 設置背景為透明
            }
        }
    }
}