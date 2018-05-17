package com.applications.duckle.danz;

import android.media.MediaPlayer;
import java.util.Observable;
import java.util.TreeMap;


public class ChickenDanceSong extends Observable implements Song {
    public MediaPlayer mediaPlayer;

    //Create the map of timings and moves
    private TreeMap<Integer, Integer> moves = createMap();

    private static final int songFile = R.raw.birddanceshort;

    public static final String songName = "Chicken Dance";

    private int currentTime = 0;

    private int pastMove;
    private int currentMove = Moves.NO_MOVE;

    private boolean stop = false;

    public ChickenDanceSong(Play play){
        mediaPlayer = MediaPlayer.create(play, songFile);
        addObserver(play);
    }

    public ChickenDanceSong (Tutorial tutorial){
        mediaPlayer = MediaPlayer.create(tutorial, songFile);
        addObserver(tutorial);
    }


    @Override
    public void run() {
        while(!moves.isEmpty() && !stop) {
            currentTime = mediaPlayer.getCurrentPosition();
            if (moves.firstKey() <= currentTime) {
                pastMove = currentMove;
                currentMove = moves.pollFirstEntry().getValue();
                if (currentMove != pastMove) {
                    setChanged();
                    notifyObservers(currentMove);
                }
            }
        }
    }

    public void stop(){
        stop = true;
    }

    private static TreeMap<Integer, Integer> createMap(){
        TreeMap<Integer, Integer> result = new TreeMap<>();

        result.put(0, Moves.NO_MOVE);
        int start = 6800;
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < 4; i++) {
                for (int n = Moves.UP_AND_DOWN_MOVE; n <= Moves.LEFT_AND_RIGHT_MOVE; n++) {
                    result.put(start, n);
                    start += 1250;
                    if(n == Moves.UP_AND_DOWN_MOVE){
                        result.put(start, n);
                        start += 1250;
                    }
                }
            }
            result.put(start, Moves.FREESTYLE);
            start += 19800;
        }

        result.put(start, Moves.NO_MOVE);

        return result;
    }

    @Override
    public MediaPlayer mediaPlayer() {
        return mediaPlayer;
    }
}