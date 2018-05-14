package com.applications.duckle.danz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PreTutorial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_tutorial);
    }

    public void start(View v){
        String songName = getIntent().getStringExtra(MainActivity.SONG_NAME);
        Intent intent = new Intent(this, Tutorial.class);
        intent.putExtra(MainActivity.SONG_NAME, songName);
        startActivity(intent);
        finish();
    }

    public void skip(View v){
        String songName = getIntent().getStringExtra(MainActivity.SONG_NAME);
        Intent intent = new Intent(this, Play.class);
        intent.putExtra(MainActivity.SONG_NAME, songName);
        startActivity(intent);
        finish();
    }

    public void onPause() {
        super.onPause();
        finish();
    }
}
