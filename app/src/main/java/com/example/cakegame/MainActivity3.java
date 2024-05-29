package com.example.cakegame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity implements OrderFragment.OnDialogButtonFragmentListener, ModeFragment.OnDialogButtonFragmentListener {

    // 全局的ScoreBoard對象
    public static ScoreBoard scoreBoard = new ScoreBoard();

    // 排行榜列表和適配器
    private List<Ranking> rankingList = new ArrayList<>();
    private RankingAdapter adapter = new RankingAdapter(this, rankingList);

    // 遊戲模式和順序變量
    private int mode;
    private int order;
    private String from;
    private int currentIndex;
    private int currentMode;
    private int currentNum = 0;

    // ListView及其子項視圖
    private ListView listView;
    private View itemView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        listView = findViewById(R.id.ranking_list_view);

        // 獲取傳入的Intent
        Intent intentFrom = getIntent();
        from = intentFrom.getStringExtra("from");

        // 初始化順序和模式
        order = 0;
        mode = 0;

        // 如果從MainActivity2跳轉過來，獲取當前模式和編號
        if (from != null && from.equals("MainActivity2")) {
            ArrayList<ScoreBoard.Score> numArrayList = ScoreBoard.getScoreBoard_num();
            if (!numArrayList.isEmpty()) {
                mode = numArrayList.get(numArrayList.size() - 1).getMode();
                currentMode = mode;
                currentNum = numArrayList.get(numArrayList.size() - 1).getNum();
            }
        }

        // 設置模式文本
        TextView modeText = findViewById(R.id.modeText);
        modeText.setText(getModeText(mode));

        // 獲取當前模式的分數列表並添加到排行榜
        updateRankingList();

        // 如果是從MainActivity2跳轉過來，平滑滾動到當前位置並高亮顯示
        if (from != null && from.equals("MainActivity2")) {
            highlightCurrentItem();
        }

        // 設置列表適配器
        listView.setAdapter(adapter);

        // 關閉按鈕設置點擊事件
        ImageView closeBottom = findViewById(R.id.window_close);
        closeBottom.setOnClickListener(v -> {
            resetItemViewBackgrounds();
            VibrationHelper.vibrate();
            startActivity(new Intent(MainActivity3.this, MainActivity.class));
        });

        // 訂單按鈕設置點擊事件
        Button orderButton = findViewById(R.id.order);
        orderButton.setOnClickListener(v -> {
            VibrationHelper.vibrate();
            OrderFragment orderFragment = new OrderFragment();
            orderFragment.setOrderListener(MainActivity3.this);
            orderFragment.show(getSupportFragmentManager(), orderFragment.getTag());
        });

        // 模式按鈕設置點擊事件
        Button modeButton = findViewById(R.id.mode);
        modeButton.setOnClickListener(v -> {
            VibrationHelper.vibrate();
            ModeFragment modeFragment = new ModeFragment();
            modeFragment.setModeListener(MainActivity3.this);
            modeFragment.show(getSupportFragmentManager(), modeFragment.getTag());
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSelectDialog(String select) {
        Log.d("CakeSort", select);
        SoundPlay.playSound("click");
        VibrationHelper.vibrate();
        Toast.makeText(this, "Select " + select, Toast.LENGTH_SHORT).show();

        TextView orderText = findViewById(R.id.orderText);
        TextView modeText = findViewById(R.id.modeText);
        rankingList.clear();
        Intent intentFrom = getIntent();
        from = intentFrom.getStringExtra("from");
        currentIndex = 0;
        resetItemViewBackgrounds();

        switch (select) {
            case "Easy":
                mode = 0;
                modeText.setText("Easy");
                break;
            case "Normal":
                mode = 1;
                modeText.setText("Normal");
                break;
            case "Hard":
                mode = 2;
                modeText.setText("Hard");
                break;
            case "Devil":
                mode = 3;
                modeText.setText("Devil");
                break;
            case "Score":
                order = 0;
                orderText.setText("Score");
                break;
            case "Cake":
                order = 1;
                orderText.setText("Cake");
                break;
        }

        // 更新排行榜列表
        updateRankingList();
        adapter.notifyDataSetChanged();
    }

    // 更新排行榜列表
    private void updateRankingList() {
        ArrayList<ScoreBoard.Score> scoreArrayList = (order == 0) ? ScoreBoard.getScoreBoard_score(mode) : ScoreBoard.getScoreBoard_cake(mode);

        for (int i = 0; i < scoreArrayList.size(); i++) {
            ScoreBoard.Score score = scoreArrayList.get(i);
            rankingList.add(new Ranking(score.getRank(), score.getScore(), score.getFullCake()));
            Log.d("CakeSort", score.getRank() + " " + score.getScore() + " " + score.getFullCake());
            if (score.getNum() == currentNum) {
                currentIndex = i;
            }
        }

        if (from != null && from.equals("MainActivity2") && mode == currentMode) {
            highlightCurrentItem();
        }
    }

    // 平滑滾動到當前位置並高亮顯示
    private void highlightCurrentItem() {
        smoothScrollToPositionFromTop(listView, currentIndex);
        listView.postDelayed(() -> {
            itemView = listView.getChildAt(currentIndex - listView.getFirstVisiblePosition());
            if (itemView != null) {
                itemView.setBackgroundResource(R.color.blue_light);
            }
        }, 500);
    }

    // 重置列表項背景
    private void resetItemViewBackgrounds() {
        for (int i = 0; i < listView.getChildCount(); i++) {
            View itemView = listView.getChildAt(i);
            if (itemView != null) {
                itemView.setBackgroundResource(0); // 設置背景為透明
            }
        }
    }

    // 平滑滾動到指定位置
    public static void smoothScrollToPositionFromTop(final AbsListView view, final int position) {
        View child = getChildAtPosition(view, position);
        if ((child != null) && ((child.getTop() == 0) || ((child.getTop() > 0) && !view.canScrollVertically(1)))) {
            return;
        }

        view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    view.setOnScrollListener(null);
                    new Handler().post(() -> view.setSelection(position));
                }
            }

            @Override
            public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {}
        });

        new Handler().post(() -> view.smoothScrollToPositionFromTop(position, 0));
    }

    // 獲取指定位置的子項視圖
    public static View getChildAtPosition(final AdapterView view, final int position) {
        final int index = position - view.getFirstVisiblePosition();
        if ((index >= 0) && (index < view.getChildCount())) {
            return view.getChildAt(index);
        } else {
            return null;
        }
    }

    // 根據模式返回相應的文本
    private String getModeText(int mode) {
        switch (mode) {
            case 0:
                return "Easy";
            case 1:
                return "Normal";
            case 2:
                return "Hard";
            case 3:
                return "Devil";
            default:
                return "Unknown";
        }
    }
}
