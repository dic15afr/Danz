package com.applications.duckle.danz;

import android.media.MediaPlayer;


public interface Song extends Runnable {

    public MediaPlayer mediaPlayer();

    public void stop();
}
