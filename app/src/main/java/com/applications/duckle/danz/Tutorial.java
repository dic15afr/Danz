package com.applications.duckle.danz;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Tutorial extends AppCompatActivity  implements Observer{
   // private ImageButton replayButton;
   // private ImageButton nextButton;
    private MediaPlayer mediaPlayer, pointMediaPlayer;
    private TextView score, tutorialText, promptText;
    private VideoView video;
    private Song song;
    private String songName;
    private int currentMove;
    private Vibrator v;

    private Accelerometer accelerometer;

    private int points = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);


       // replayButton = (ImageButton) findViewById(R.id.replay_button);
       // nextButton = (ImageButton) findViewById(R.id.next_button);
       // MediaController mc;
        video = (VideoView) findViewById(R.id.videoView);
        video.setMediaController(new MediaController(this));
        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.white));
        video.requestFocus();
        video.start();

        accelerometer = new Accelerometer(this, this);
        score = (TextView) findViewById(R.id.text_score);
        tutorialText = (TextView) findViewById(R.id.text_tutorial);
        promptText = (TextView) findViewById(R.id.text_prompt);
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        currentMove = Moves.NO_MOVE;

        Intent intent = getIntent();
        songName = intent.getStringExtra(MainActivity.SONG_NAME);
        switch (songName){
            case "Chicken Dance":
                song = new ChickenDanceSong(this);
                currentMove = Moves.UP_AND_DOWN_MOVE;
                break;
            case "Levels":
                song = new LevelsSong(this);
                video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.fistpump));
                currentMove = Moves.FIST_PUMP;
                break;
            default:
                System.exit(0);
        }
        promptUpdate(currentMove);
        mediaPlayer = song.mediaPlayer();
        pointMediaPlayer = MediaPlayer.create(this, R.raw.ding);

    }


    public void replayBtn(View v){
        Intent intent = new Intent(this, Tutorial.class);
        intent.putExtra(MainActivity.SONG_NAME, songName);
        startActivity(intent);
    }

    private void promptUpdate(final int move){
        currentMove = move;
        switch (move) {
            case Moves.FIST_PUMP:
                promptText.setText("PUMP YOUR FIST IN THE AIR. WOO!");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        video.setAlpha(0);
                        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + move));
                    }
                });
                break;
            case Moves.FORWARD_AND_BACKWARD_MOVE:
                promptText.setText("Move your phone forward and backward");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        video.setAlpha(0);
                        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + move));
                    }
                });
                break;
            case Moves.LEFT_AND_RIGHT_MOVE:
                promptText.setText("Move your phone left and right");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        video.setAlpha(0);
                        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + move));
                    }
                });
                break;
            case Moves.UP_AND_DOWN_MOVE:
                promptText.setText("Move your phone up and down");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        video.setAlpha(0);
                        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + move));
                    }
                });
                break;
            default:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        video.setAlpha(0);
                        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.white));
                    }
                });
                break;
        }
    }

    public void nextBtn(View v){
        if(songName.equals("Chicken Dance") && currentMove != 5){
            currentMove++;
            promptUpdate(currentMove);
            tutorialText.setText("Keep the phone vertical and aimed towards you.");
        } else{
            Intent intent = new Intent(this, Play.class);
            intent.putExtra(MainActivity.SONG_NAME, songName);
            startActivity(intent);
        }
    }




    @Override
    public void update(Observable o, Object arg) {

       if (o instanceof Accelerometer) {
            if ((int) arg == currentMove && currentMove != Moves.NO_MOVE){
                points++;
                if(points % 10 == 0){
                    pointMediaPlayer.start();
                    tutorialText.setText("Completed. move on to the next");
                    points = 0;
                }
                v.vibrate(40);
                String scoreText = "Points: " + points;
                score.setText(scoreText);
            }
            else if ((currentMove == 5 && ((int) arg == 1 || (int) arg == 2 || (int) arg == 3 || (int)arg == 4))){
                points++;
                if(points % 10 == 0){
                    pointMediaPlayer.start();
                    tutorialText.setText("Completed. move on to the next");
                    points = 0;
                }
                v.vibrate(40);
                String scoreText = "Points: " + points;
                score.setText(scoreText);
            }
        }
    }
}
