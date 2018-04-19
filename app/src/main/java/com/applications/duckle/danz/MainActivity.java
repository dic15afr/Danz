package com.applications.duckle.danz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String SONG_NAME = "com.example.duckle.danz.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void play(View v){
        Intent intent = new Intent(this, Play.class);
        String songName = "Chicken Dance";
        intent.putExtra(SONG_NAME, songName);
        startActivity(intent);
    }

    public void test(View v){
        startActivity(new Intent(this, Test.class));
    }
}
