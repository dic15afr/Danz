package com.applications.duckle.danz;

import android.media.MediaPlayer;

import java.util.Observable;
import java.util.TreeMap;

public class LevelsSong extends Observable implements Song{
    public MediaPlayer mediaPlayer;

    //Create the map of timings and moves
    private TreeMap<Integer, Integer> moves = createMap();

    private static final int songFile = R.raw.levels;

    public static final String songName = "Levels";

    private int currentTime = 0;

    private int pastMove;
    private int currentMove = Moves.NO_MOVE;

    private boolean stop = false;

    public LevelsSong(Play play){
        mediaPlayer = MediaPlayer.create(play, songFile);
        addObserver(play);
    }

    public LevelsSong (Tutorial tutorial){
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
        result.put(7300, Moves.FIST_PUMP);

        return result;
    }

    @Override
    public MediaPlayer mediaPlayer() {
        return mediaPlayer;
    }
}
