package com.applications.duckle.danz;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Observable;
import java.util.Observer;

public class Play extends AppCompatActivity implements Observer{
    private MediaPlayer mediaPlayer;
    private MediaObserver observer = null;
    private ProgressBar progressBar;
    private VideoView video;
    private ImageView image;
    private Song song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent intent = getIntent();

        String songName = intent.getStringExtra(MainActivity.SONG_NAME);
        switch (songName){
            case "Chicken Dance":
                song = new ChickenDanceSong(this);
                break;
            default:
                System.exit(0);
        }

        mediaPlayer = song.mediaPlayer();

        new Thread(song).start();

        TextView songNameTextView = findViewById(R.id.songName);
        songNameTextView.setText(songName);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(mediaPlayer.getDuration());

        video = findViewById(R.id.video);
        image = findViewById(R.id.image);
        image.setVisibility(View.VISIBLE);
        video.setVisibility(View.INVISIBLE);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                observer.stop();
                video.stopPlayback();
                video.setVisibility(View.INVISIBLE);
                image.setImageResource(R.drawable.main);
                image.setVisibility(View.VISIBLE);
                progressBar.setProgress(mPlayer.getCurrentPosition());
            }
        });

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                video.start();
            }
        });

        observer = new MediaObserver();
        new Thread(observer).start();
    }

    public void playBtn(View v){
        mediaPlayer.start();
        image.setImageResource(R.drawable.start);
    }

    public void stopBtn(View v){
        mediaPlayer.stop();
        observer.stop();
        finish();
    }

    public void pauseBtn(View v){
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void update(Observable o, Object arg) {

        int move = R.raw.leftright;
        switch ((int) arg){
            case 0:
                break;
            case 1:
                move = R.raw.wave;
                break;
            case 2:
                move = R.raw.updown;
                break;
            case 3:
                move = R.raw.leftright;
                break;
            case 4:
                move = R.raw.forback;
                break;

        }
        final Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+move); //Declare your url here.

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    video.setVideoURI(uri);
                    video.start();
                    image.setVisibility(View.INVISIBLE);
                    video.setVisibility(View.VISIBLE);
                }
            });
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