package com.applications.duckle.danz;

import android.media.MediaPlayer;

import java.util.Map;

public interface Song extends Runnable {

    public MediaPlayer mediaPlayer();

    public void stop();
}
