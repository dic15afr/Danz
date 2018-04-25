package com.applications.duckle.danz;

import android.media.MediaPlayer;

import java.util.Collections;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;


public class ChickenDanceSong extends Observable implements Song {
    public MediaPlayer mediaPlayer;

    //Create the map of timings and moves
    private static final Map<Integer, Integer> moves = createMap();

    private static final int songFile = R.raw.birddance;

    public static final String songName = "Chicken Dance";

    private int currentTime = 0;
    private long pastTime = 0;

    private int pastMove;
    private int currentMove = Moves.NO_MOVE;

    public ChickenDanceSong(Play play){
        mediaPlayer = MediaPlayer.create(play, songFile);
        addObserver(play);
    }


    @Override
    public void run() {
        while(true){
            while(mediaPlayer.isPlaying()){
                currentTime = mediaPlayer.getCurrentPosition();
                if(moves.containsKey(currentTime)){
                    pastMove = currentMove;
                    currentMove = moves.get(currentTime);
                    if (currentMove != pastMove) {
                        setChanged();
                        notifyObservers(currentMove);
                    }
                }
            }
        }

    }

    private static Map<Integer, Integer> createMap(){
        Map<Integer, Integer> result = new TreeMap<>();

        result.put(0, Moves.NO_MOVE);
        int start = 6800;
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < 4; i++) {
                for (int n = Moves.WAVE_MOVE; n <= Moves.FORWARD_AND_BACKWARD_MOVE; n++) {
                    result.put(start, n);
                    start += 1250;
                }
            }
            result.put(start, Moves.NO_MOVE);
            start += 19800;
        }

        for (int n = Moves.WAVE_MOVE; n <= Moves.FORWARD_AND_BACKWARD_MOVE; n++) {
            result.put(start, n);
            start += 1550;
        }

        for (int n = Moves.WAVE_MOVE; n <= Moves.FORWARD_AND_BACKWARD_MOVE; n++) {
            result.put(start, n);
            start += 1300;
        }

        for (int n = Moves.WAVE_MOVE; n <= Moves.FORWARD_AND_BACKWARD_MOVE; n++) {
            result.put(start, n);
            start += 1175;
        }

        for (int n = Moves.WAVE_MOVE; n <= Moves.FORWARD_AND_BACKWARD_MOVE; n++) {
            result.put(start, n);
            start += 1100;
        }

        for (int n = Moves.WAVE_MOVE; n <= Moves.FORWARD_AND_BACKWARD_MOVE; n++) {
            result.put(start, n);
            start += 1025;
        }

        for (int n = Moves.WAVE_MOVE; n <= Moves.FORWARD_AND_BACKWARD_MOVE; n++) {
            result.put(start, n);
            start += 975;
        }

        for (int n = Moves.WAVE_MOVE; n <= Moves.FORWARD_AND_BACKWARD_MOVE; n++) {
            result.put(start, n);
            start += 950;
        }

        for (int n = Moves.WAVE_MOVE; n <= Moves.FORWARD_AND_BACKWARD_MOVE; n++) {
            result.put(start, n);
            start += 950;
        }

        result.put(start, Moves.NO_MOVE);

        return Collections.unmodifiableMap(result);
    }

    @Override
    public MediaPlayer mediaPlayer() {
        return mediaPlayer;
    }
}
