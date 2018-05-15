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
import java.util.Observable;
import java.util.Observer;

public class Tutorial extends AppCompatActivity  implements Observer{
    private MediaPlayer pointMediaPlayer;
    private TextView score, promptText;
    private VideoView video;
    private String songName;
    private int currentMove;
    private Vibrator v;
    private Accelerometer accelerometer;
    private int points = 0;


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
        switch (songName){
            case "Chicken Dance":
                currentMove = 1;
                break;
            case "Levels":
                currentMove = 5;
                break;
            default:
                System.exit(0);
        }
        promptUpdate();
    }


    public void replayBtn(View v){
        points = 0;
        String scoreText = "Points: " + points;
        score.setText(scoreText);

        if(songName.equals("Chicken Dance")){
            currentMove = 1;
        } else {
            currentMove = 5;
        }
        promptUpdate();
    }

    private void promptUpdate(){
        switch (currentMove) {
            case 1:
                promptText.setText("Move your phone up and down");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.move1));
                    }
                });
                break;
            case 2:
                promptText.setText("Move your phone up and down");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.move2));
                    }
                });
                break;
            case 3:
                promptText.setText("Move your phone forward and backward");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.move3));
                    }
                });
                break;
            case 4:
                promptText.setText("Move your phone left and right");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.move4));
                    }
                });
                break;
            case 5:
                promptText.setText("Pump your fist in the air!");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.fistpump));
                    }
                });
                break;
            default:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.white));
                    }
                });
                break;
        }
    }

    public void nextBtn(View v){
        next();
    }

    private void next(){
        if(songName.equals("Chicken Dance") && currentMove != 4){
            points = 0;
            String scoreText = "Points: " + points;
            score.setText(scoreText);

            currentMove++;
            promptUpdate();
        } else{
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

       if (o instanceof Accelerometer) {
            if ((int) arg == currentMove || currentMove == Moves.FIST_PUMP || (currentMove == Moves.UP_AND_DOWN_MOVE_2 && (int) arg == Moves.UP_AND_DOWN_MOVE)){
                points++;
                if(points % 10 == 0){
                    pointMediaPlayer.start();
                    next();
                }
                v.vibrate(40);
                String scoreText = "Points: " + points;
                score.setText(scoreText);

            }
        }
    }

    public void onPause() {
        super.onPause();
        video.stopPlayback();
        accelerometer.deleteObservers();
        finish();
    }
}