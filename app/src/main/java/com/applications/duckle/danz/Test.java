package com.applications.duckle.danz;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Test extends AppCompatActivity implements SensorEventListener {


    private SensorManager mSensorManager;
    private Sensor acc_sensors;
    private float [] acc_values;
    TextView xText, yText, zText, shakeText;
    private long lastUpdate;
    private final int SHAKE_THRESHOLD = 800;
    float last_x, last_y, last_z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acc_sensors = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        xText = (TextView) findViewById(R.id.text_x);
        yText = (TextView) findViewById(R.id.text_y);
        zText = (TextView) findViewById(R.id.text_z);
        shakeText = (TextView) findViewById(R.id.text_shake);
        mSensorManager.registerListener(this, acc_sensors, SensorManager.SENSOR_DELAY_NORMAL);
        lastUpdate = System.currentTimeMillis();
        last_x = 0;
        last_y = 0;
        last_z = 0;
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        acc_values = sensorEvent.values;
        xText.setText("X: " + roundPrecision(acc_values[0], 3));
        yText.setText("Y: " + roundPrecision(acc_values[1], 3));
        zText.setText("Z: " + roundPrecision(acc_values[2], 3));


        long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
        if ((curTime - lastUpdate) > 100) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float x = sensorEvent.values[SensorManager.DATA_X];
            float y = sensorEvent.values[SensorManager.DATA_Y];
            float z = sensorEvent.values[SensorManager.DATA_Z];

            float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;

            if (speed > SHAKE_THRESHOLD) {
                shakeText.setText("Shaking");
            } else {
                shakeText.setText("Not shaking");
            }
            last_x = x;
            last_y = y;
            last_z = z;
        }

    }

    private String roundPrecision(float num, int deci){
        String numb = Float.toString(num);
        int comma = numb.indexOf(".");
        if( comma > -1){
            numb = numb.substring(0, comma + deci);
        }
        return numb;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, acc_sensors, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
    public void switchBack(View view){
        mSensorManager.unregisterListener(this);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }*/
}
