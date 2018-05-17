package com.applications.duckle.danz;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

public class Tutorial extends AppCompatActivity  implements Observer{
    private MediaPlayer pointMediaPlayer;
    private TextView score, promptText;
    private VideoView video;
    private String songName;
    private int currentMove, previousMove;
    private Vibrator v;
    private Accelerometer accelerometer;
    private int points = 0;
    Stack<Integer> moveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        video = findViewById(R.id.videoView);
        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.white));
        video.start();

        accelerometer = new Accelerometer(this, this);
        score = findViewById(R.id.text_score);
        promptText = findViewById(R.id.text_prompt);
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        pointMediaPlayer = MediaPlayer.create(this, R.raw.ding);

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                video.start();
            }
        });

        Intent intent = getIntent();
        songName = intent.getStringExtra(MainActivity.SONG_NAME);
        moveList = new Stack<>();
        initializeMoves();
        promptUpdate();
    }

    public void initializeMoves(){
        moveList.removeAllElements();
        switch (songName){
            case "Chicken Dance":
                moveList.addAll(Arrays.asList(Moves.LEFT_AND_RIGHT_MOVE, Moves.FORWARD_AND_BACKWARD_MOVE, Moves.UP_AND_DOWN_MOVE, Moves.UP_AND_DOWN_MOVE));
                break;
            case "Levels":
                moveList.addAll(Arrays.asList( Moves.WAVE, Moves.CLAP, Moves.FIST_PUMP));
                break;
        }
        currentMove = moveList.pop();
    }


    public void replayBtn(View v){
        points = 0;
        String scoreText = "Points: " + points;
        score.setText(scoreText);

        initializeMoves();
        promptUpdate();
    }

    private void promptUpdate(){
        int moveClip;
        switch (currentMove) {
            case Moves.UP_AND_DOWN_MOVE:
                promptText.setText("Move your phone up and down");
                if(moveList.peek().equals(Moves.UP_AND_DOWN_MOVE)){
                    moveClip = R.raw.chickenmove1;
                }else{
                    moveClip = R.raw.chickenmove2;
                }
                break;
            case Moves.FORWARD_AND_BACKWARD_MOVE:
                promptText.setText("Move your phone forward and backward");
                moveClip = R.raw.chickenmove3;
                break;
            case Moves.LEFT_AND_RIGHT_MOVE:
                promptText.setText("Clap!");
                moveClip = R.raw.chickenmove4;
                break;
            case Moves.FIST_PUMP:
                promptText.setText("Pump your fist in the air!");
                moveClip = R.raw.fistpump;
                break;
            case Moves.CLAP:
                promptText.setText("Clap!");
                moveClip = R.raw.clap;
                break;
            case Moves.WAVE:
                promptText.setText("Wave your arms in the air!!");
                moveClip = R.raw.wavemove;
                break;
            default:
                moveClip = R.raw.white;
                break;
        }

        final Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + moveClip);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                video.setVideoURI(uri);
            }
        });
    }

    public void nextBtn(View v){
        next();
    }

    private void next(){
        if(!moveList.empty()){
            points = 0;
            String scoreText = "Points: " + points;
            score.setText(scoreText);

            currentMove = moveList.pop();
            promptUpdate();
        }else{
            Intent intent = new Intent(this, PostTutorial.class);
            intent.putExtra(MainActivity.SONG_NAME, songName);
            startActivity(intent);
            video.stopPlayback();
            accelerometer.deleteObservers();
            finish();
        }
    }

    @Override
    public void update(Observable o, Object arg) {

        int accMove = (int) arg;
        switch (currentMove){
            case Moves.CLAP:
                if((accMove == Moves.FORWARD_AND_BACKWARD_MOVE || accMove == Moves.LEFT_AND_RIGHT_MOVE) && previousMove != Moves.UP_AND_DOWN_MOVE){
                    successfulMove();
                }
                break;
            case Moves.FIST_PUMP:
                if(previousMove == Moves.FORWARD_AND_BACKWARD_MOVE || previousMove == Moves.LEFT_AND_RIGHT_MOVE){
                    if(accMove == Moves.UP_AND_DOWN_MOVE){
                        successfulMove();

                    }
                } else if (previousMove == Moves.UP_AND_DOWN_MOVE){
                    if (accMove == Moves.FORWARD_AND_BACKWARD_MOVE || accMove == Moves.LEFT_AND_RIGHT_MOVE){
                        successfulMove();
                    }
                }
                break;
            case Moves.WAVE:
                if((accMove == Moves.FORWARD_AND_BACKWARD_MOVE || accMove == Moves.LEFT_AND_RIGHT_MOVE) && previousMove != Moves.UP_AND_DOWN_MOVE){
                    successfulMove();
                }
                break;
            case Moves.UP_AND_DOWN_MOVE:
                if(accMove == Moves.UP_AND_DOWN_MOVE){
                    successfulMove();
                }
                break;
            case Moves.LEFT_AND_RIGHT_MOVE:
                if(accMove == Moves.LEFT_AND_RIGHT_MOVE || accMove == Moves.FORWARD_AND_BACKWARD_MOVE){
                    successfulMove();
                }
                break;
            case Moves.FORWARD_AND_BACKWARD_MOVE:
                if(accMove == Moves.FORWARD_AND_BACKWARD_MOVE || accMove == Moves.LEFT_AND_RIGHT_MOVE){
                    successfulMove();
                }
                break;
            default:
                return;
        }
        previousMove = accMove;
    }

    private void successfulMove(){
        points++;
        v.vibrate(40);
        String scoreText = "Points: " + points;
        score.setText(scoreText);

        if(points % 10 == 0){
            pointMediaPlayer.start();
            next();
        }
    }

    public void onPause() {
        super.onPause();
        video.stopPlayback();
        accelerometer.deleteObservers();
        finish();
    }
}