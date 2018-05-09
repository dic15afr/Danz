package com.applications.duckle.danz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.VideoView;

import java.util.Observable;
import java.util.Observer;

public class Tutorial extends AppCompatActivity implements Observer{

    private ImageButton replayButton;
    private ImageButton nextButton;
    private VideoView video;

    private Accelerometer accelerometer;

    private int[] moves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        replayButton = (ImageButton) findViewById(R.id.replay_button);
        nextButton = (ImageButton) findViewById(R.id.next_button);
        video = (VideoView) findViewById(R.id.video_view);
        accelerometer = new Accelerometer(this, this);
    }

    public void replayBtn(){

    }

    public void nextBtn(){

    }



    @Override
    public void update(Observable o, Object arg) {

    }
}
