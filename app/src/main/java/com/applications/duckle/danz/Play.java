package com.applications.duckle.danz;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Play extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private MediaObserver observer = null;
    private ProgressBar progressBar;
    private ImageChanger imageChanger;
    private ImageView image;

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

        image = findViewById(R.id.image);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                observer.stop();
                progressBar.setProgress(mPlayer.getCurrentPosition());
            }
        });
        observer = new MediaObserver();
        new Thread(observer).start();

        imageChanger = new ImageChanger();
        new Thread(imageChanger).start();
    }

    public void playBtn(View v){
        mediaPlayer.start();
    }

    public void stopBtn(View v){
        mediaPlayer.stop();
        observer.stop();
        imageChanger.stop();
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

    private class ImageChanger implements Runnable {
        private boolean stop = false;

        public void stop() {
            stop = true;
        }

        @Override
        public void run() {
            changeImage(0);
            while(!stop){
                changeImage(1);
                changeImage(2);
                changeImage(1);
                changeImage(2);

                changeImage(3);
                changeImage(4);
                changeImage(3);
                changeImage(4);
            }
        }

        private void changeImage(final int img){
            while(!mediaPlayer.isPlaying() && !stop || mediaPlayer.getCurrentPosition() < 6700 && img != 0){}
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (img){
                        case 0:
                            image.setImageResource(R.drawable.start);
                            break;
                        case 1:
                            image.setImageResource(R.drawable.phoneup);
                            break;
                        case 2:
                            image.setImageResource(R.drawable.phonedown);
                            break;
                        case 3:
                            image.setImageResource(R.drawable.phoneleft);
                            break;
                        case 4:
                            image.setImageResource(R.drawable.phoneright);
                            break;
                    }
                }
            });
            Sleep(305);
        }

        private void Sleep(int millis){
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}