package com.example.cakegame;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity implements BottomSheetFragment.OnDialogButtonFragmentListener {

    private String selectedOption;
    List<Ranking> rankingList = new ArrayList<>();
    RankingAdapter adapter = new RankingAdapter(this, rankingList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        selectedOption = "Score";

        ListView listView = findViewById(R.id.ranking_list_view);


        rankingList.add(new Ranking(1, 90, 80));
        rankingList.add(new Ranking(2, 85, 85));
        rankingList.add(new Ranking(3, 92, 70));
        // Add more rankings as needed

        listView.setAdapter(adapter);

        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        TextView closeBottom = findViewById(R.id.window_close);
        closeBottom.setTypeface(fontAwesomeFont);
        closeBottom.setTextSize(24);
        closeBottom.setOnClickListener(v ->{
            Intent intent =  new Intent(MainActivity3.this, MainActivity.class);
            startActivity(intent);
        });

        Button modalFragmentButton = findViewById(R.id.choice);
        modalFragmentButton.setOnClickListener(v -> {
            BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
            bottomSheetFragment.setListener(MainActivity3.this);
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });

    }

    @Override
    public void onSelectDialog(String select) {
        selectedOption = select;
        Log.d("CakeSort", select);
        Toast.makeText(this, "Select" + select, Toast.LENGTH_SHORT).show();
        rankingList.clear();
        if(select.equals("Score")){

        }
        adapter.notifyDataSetChanged();
    }
}