package com.applications.duckle.danz;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;

import java.util.Observable;
import java.util.Observer;


import static android.content.Context.SENSOR_SERVICE;

public class Accelerometer extends Observable implements SensorEventListener {

    private long lastUpdate;
    private float last_x, last_y, last_z;
    private final int SHAKE_THRESHOLD = 400;

    public Accelerometer (Observer obs, AppCompatActivity play){
        super();
        addObserver(obs);
        SensorManager mSensorManager = (SensorManager) play.getSystemService(SENSOR_SERVICE);
        Sensor acc_sensors = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, acc_sensors, SensorManager.SENSOR_DELAY_GAME);
        last_x = 0;
        last_y = 0;
        last_z = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        long curTime = System.currentTimeMillis();
        if ((curTime - lastUpdate) > 150) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;


            float x = sensorEvent.values[SensorManager.DATA_X];
            float y = sensorEvent.values[SensorManager.DATA_Y];
            float z = sensorEvent.values[SensorManager.DATA_Z];

            float x_speed = Math.abs(x - last_x) / diffTime * 10000;
            float y_speed = Math.abs(y - last_y) / diffTime * 10000;
            float z_speed = Math.abs(z - last_z) / diffTime * 10000;

            float max = Math.max(x_speed, y_speed);
            max = Math.max(max, z_speed);

            setChanged();
            if (x_speed > SHAKE_THRESHOLD && x_speed == max) {
               notifyObservers(Moves.LEFT_AND_RIGHT_MOVE);
            } else if (y_speed > SHAKE_THRESHOLD && y_speed == max) {
                notifyObservers(Moves.UP_AND_DOWN_MOVE);
            }  else if (z_speed > SHAKE_THRESHOLD && z_speed == max) {
                notifyObservers(Moves.FORWARD_AND_BACKWARD_MOVE);
            }

            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}