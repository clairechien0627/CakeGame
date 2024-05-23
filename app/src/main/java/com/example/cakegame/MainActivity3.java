package com.example.cakegame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Button newgame = findViewById(R.id.startnewgame);
        newgame.setOnClickListener(v -> {
            Intent intent =  new Intent(MainActivity3.this, MainActivity.class);
            startActivity(intent);
        });
    }
}