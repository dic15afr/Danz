package com.applications.duckle.danz;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import java.util.Observable;
import java.util.Observer;

public class Play extends AppCompatActivity implements Observer{
    private MediaPlayer mediaPlayer, pointMediaPlayer;
    private MediaObserver observer = null;
    private ProgressBar progressBar;
    private VideoView video;
    private ImageView image;
    private Song song;
    private Accelerometer accelerometer;
    private int currentMove;
    private int previousMove = 0;
    private int points = 0;
    private TextView score;
    private Vibrator v;

    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        accelerometer = new Accelerometer(this,this);

        Intent intent = getIntent();

        String songName = intent.getStringExtra(MainActivity.SONG_NAME);
        switch (songName){
            case "Chicken Dance":
                song = new ChickenDanceSong(this);
                break;
            case "Levels":
                song = new LevelsSong(this);
                break;
            default:
                System.exit(0);
        }

        mediaPlayer = song.mediaPlayer();
        pointMediaPlayer = MediaPlayer.create(this, R.raw.ding);

        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        new Thread(song).start();

        score = findViewById(R.id.score);
        TextView songNameTextView = findViewById(R.id.songName);
        songNameTextView.setText(songName);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(mediaPlayer.getDuration());

        image = findViewById(R.id.image);
        imageButton = findViewById(R.id.toggle_btn);

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
                //findViewById(R.id.play_btn).setEnabled(false);
                //findViewById(R.id.pause_btn).setEnabled(false);
                imageButton.setEnabled(false);
            }
        });

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                video.start();
            }
        });
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {

                mp.setVolume(0, 0);
            }
        });

        observer = new MediaObserver();
        new Thread(observer).start();
        togglePlay();
    }

    public void playBtn(View v){
        if(!mediaPlayer.isPlaying()){
            video.start();
            image.setImageResource(R.drawable.start);
        }
        mediaPlayer.start();
    }

    public void stopBtn(View v){
        mediaPlayer.stop();
        observer.stop();
        video.stopPlayback();
        song.stop();
        accelerometer.deleteObservers();
        finish();
    }

    public void pauseBtn(View v){
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            video.pause();
        }
    }

    public void playPausebtn(View v){
        togglePlay();
    }

    public void togglePlay(){
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            video.pause();
            imageButton.setImageResource(R.drawable.play);
        } else {
            video.start();
            image.setImageResource(R.drawable.start);
            mediaPlayer.start();
            imageButton.setImageResource(R.drawable.pause);
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
            if(currentMove != Moves.NO_MOVE && mediaPlayer.isPlaying()) {
                int accMove = (int) arg;
                switch (currentMove){
                    case Moves.CLAP:
                        if(accMove == Moves.FORWARD_AND_BACKWARD_MOVE || accMove == Moves.LEFT_AND_RIGHT_MOVE && previousMove != Moves.UP_AND_DOWN_MOVE){
                            successfulMove();
                        }
                        break;
                    case Moves.FIST_PUMP:
                        if(previousMove == Moves.FORWARD_AND_BACKWARD_MOVE){
                            if(accMove == Moves.UP_AND_DOWN_MOVE){
                                successfulMove();

                            }
                        } else if (previousMove == Moves.UP_AND_DOWN_MOVE){
                            if (accMove == Moves.FORWARD_AND_BACKWARD_MOVE){
                                successfulMove();
                            }
                        }
                        break;
                    case Moves.WAVE:
                        if(accMove == Moves.FORWARD_AND_BACKWARD_MOVE || accMove == Moves.LEFT_AND_RIGHT_MOVE && previousMove != Moves.UP_AND_DOWN_MOVE){
                            successfulMove();
                        }
                        break;
                    case Moves.UP_AND_DOWN_MOVE:
                        if(accMove == Moves.UP_AND_DOWN_MOVE){
                            successfulMove();
                        }
                        break;
                    case Moves.LEFT_AND_RIGHT_MOVE:
                        if(accMove == Moves.LEFT_AND_RIGHT_MOVE){
                            successfulMove();
                        }
                        break;
                    case Moves.FREESTYLE:
                        if(previousMove != accMove){
                            successfulMove();
                        }
                        break;
                    case Moves.FORWARD_AND_BACKWARD_MOVE:
                        if(accMove == Moves.FORWARD_AND_BACKWARD_MOVE){
                            successfulMove();
                        }
                        break;
                    default:
                        break;
                }
                previousMove = accMove;
            }
        }
    }

    private void successfulMove(){
        points++;
        if (points % 10 == 0) {
            pointMediaPlayer.start();
        }
        v.vibrate(40);
        String scoreText = "Points: " + points;
        score.setText(scoreText);
    }

    private void updateMove(){
        if (currentMove == Moves.NO_MOVE){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    image.setImageResource(R.drawable.main);
                    video.setAlpha(0);
                    image.setAlpha(255);
                    video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.white));
                }
            });
        }else if(currentMove == Moves.FREESTYLE) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    image.setImageResource(R.drawable.freestyle);
                    video.setAlpha(0);
                    image.setAlpha(255);
                    video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.white));
                }
            });
        }else {

            int move;
            switch (currentMove){
                case 1:
                    move = R.raw.m1;
                    break;
                case 2:
                    move = R.raw.m2;
                    break;
                case 3:
                    move = R.raw.m3;
                    break;
                case 4:
                    move = R.raw.m4;
                    break;
                case Moves.FIST_PUMP:
                    move = R.raw.fistpump;
                    break;
                case Moves.CLAP:
                    move = R.raw.clapm;
                    break;
                case Moves.WAVE:
                    move = R.raw.wavemove;
                    break;
                default:
                    move = 0;
                    break;
            }

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
        accelerometer.deleteObservers();
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