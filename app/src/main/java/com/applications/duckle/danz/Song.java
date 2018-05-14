package com.applications.duckle.danz;

import android.media.MediaPlayer;


public interface Song extends Runnable {

    MediaPlayer mediaPlayer();

    void stop();
}
