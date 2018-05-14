package com.applications.duckle.danz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PostTutorial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_tutorial);
    }

    public void cont(View v){
        String songName = getIntent().getStringExtra(MainActivity.SONG_NAME);
        Intent intent = new Intent(this, Play.class);
        intent.putExtra(MainActivity.SONG_NAME, songName);
        startActivity(intent);
        finish();
    }

    public void replay(View v){
        String songName = getIntent().getStringExtra(MainActivity.SONG_NAME);
        Intent intent = new Intent(this, PreTutorial.class);
        intent.putExtra(MainActivity.SONG_NAME, songName);
        startActivity(intent);
        finish();
    }

    public void onPause() {
        super.onPause();
        finish();
    }
}
