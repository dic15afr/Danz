package com.applications.duckle.danz;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Observable;


import static android.content.Context.SENSOR_SERVICE;

public class Accelerometer extends Observable implements SensorEventListener {

    private int current_move;
    private SensorManager mSensorManager;
    private Sensor acc_sensors;
    private float [] prev_acc;
    private long lastUpdate;

    public Accelerometer (Play play){
        super();
        current_move = Moves.NO_MOVE;
        addObserver(play);
        mSensorManager = (SensorManager) play.getSystemService(SENSOR_SERVICE);
        acc_sensors = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, acc_sensors, SensorManager.SENSOR_DELAY_GAME);
        //prev_acc = new float [3];
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long curTime = System.currentTimeMillis();
        float[] acc = event.values;

        if(curTime - lastUpdate > 100) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float[] diff = new float[3];
            for (int i = 0; i < 3; i++) {
                diff[i] = acc[i] - prev_acc[i];
            }
            
            float max_diff = Math.max(diff[0], diff[1]);
            max_diff = Math.max(max_diff, diff[2]);

            if (diff[0] == max_diff) {
                // X axis
                notifyObservers(Moves.LEFT_AND_RIGHT_MOVE);
            } else if (diff[1] == max_diff) {
                // Y axis
                notifyObservers(Moves.UP_AND_DOWN_MOVE);
            } else if (diff[2] == max_diff) {
                // Z axis
                notifyObservers(Moves.FORWARD_AND_BACKWARD_MOVE);
            } else {
                notifyObservers(Moves.NO_MOVE);
            }
        }
        prev_acc = acc;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
