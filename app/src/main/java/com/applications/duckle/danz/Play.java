package com.applications.duckle.danz;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Play extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private MediaObserver observer = null;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        mediaPlayer = MediaPlayer.create(this, R.raw.birddance);

        Intent intent = getIntent();

        String songName = intent.getStringExtra(MainActivity.SONG_NAME);
        TextView songNameTextView = findViewById(R.id.songName);
        songNameTextView.setText(songName);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(mediaPlayer.getDuration());
    }

    public void playBtn(View v){

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                observer.stop();
                progressBar.setProgress(mPlayer.getCurrentPosition());
            }
        });
        observer = new MediaObserver();
        mediaPlayer.start();
        new Thread(observer).start();
    }

    public void stopBtn(View v){
        mediaPlayer.stop();
        finish();
    }

    public void pauseBtn(View v){
        mediaPlayer.pause();
    }

    private class MediaObserver implements Runnable {
        private boolean stop = false;

        public void stop() {
            stop = true;
        }

        @Override
        public void run(){
            while (!stop) {
                progressBar.setProgress(mediaPlayer.getCurrentPosition());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


