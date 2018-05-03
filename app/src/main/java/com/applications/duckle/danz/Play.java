package com.applications.duckle.danz;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
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
    private Accelerometer accelerometer;
    private int currentMove;
    private int points = 0;
    private TextView score;
    private Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        accelerometer = new Accelerometer(this);

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

        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        new Thread(song).start();

        score = findViewById(R.id.score);
        TextView songNameTextView = findViewById(R.id.songName);
        songNameTextView.setText(songName);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(mediaPlayer.getDuration());

        image = findViewById(R.id.image);

        video = findViewById(R.id.video);
        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.white));
        video.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                observer.stop();
                video.stopPlayback();
                video.setAlpha(0);
                image.setImageResource(R.drawable.main);
                image.setAlpha(255);
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
        video.start();
        image.setImageResource(R.drawable.start);
    }

    public void stopBtn(View v){
        mediaPlayer.stop();
        observer.stop();
        video.stopPlayback();
        song.stop();
        finish();
    }

    public void pauseBtn(View v){
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            video.pause();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Song) {
            if(currentMove != (int) arg){
                currentMove = (int) arg;
                updateMove();
            }
        }else if (o instanceof Accelerometer) {
            if ((int) arg == currentMove && currentMove != Moves.NO_MOVE){
                points++;
                v.vibrate(40);
                String scoreText = "Points: " + points;
                score.setText(scoreText);
            }
        }
    }

    private void updateMove(){
        int move;
        switch (currentMove){
            case 0:
                move = 0;
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
            default:
                move = 0;
                break;
        }

        if (move == 0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    image.setImageResource(R.drawable.main);
                    video.setAlpha(0);
                    image.setAlpha(255);
                    video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.white));
                }
            });

        }else {
            final Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + move);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    video.setVideoURI(uri);
                    if(video.getAlpha() == 0){
                        image.setAlpha(0);
                        video.setAlpha(1);
                    }
                }
            });
        }
    }

    public void onPause() {
        super.onPause();
        mediaPlayer.stop();
        observer.stop();
        video.stopPlayback();
        song.stop();
        finish();
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