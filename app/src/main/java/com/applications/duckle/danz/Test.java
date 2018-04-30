package com.applications.duckle.danz;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Test extends AppCompatActivity implements SensorEventListener {


    private SensorManager mSensorManager;
    private Sensor acc_sensors;
    private float [] acc_values;
    TextView xText, yText, zText, shakeText, thetaText, gravText, phiText;
    ImageView shakeIndicator;
    LinearLayout backGround;
    private long lastUpdate;
    private final int SHAKE_THRESHOLD = 400;
    private final int UPDATE_RATE = 10;
    float last_x, last_y, last_z;
    double last_theta, last_grav, last_phi;
    private int move;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acc_sensors = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        xText = (TextView) findViewById(R.id.text_x);
        yText = (TextView) findViewById(R.id.text_y);
        zText = (TextView) findViewById(R.id.text_z);
        thetaText = (TextView) findViewById(R.id.text_theta);
        phiText = (TextView) findViewById(R.id.text_phi);
        gravText = (TextView) findViewById(R.id.text_gravity);
        shakeText = (TextView) findViewById(R.id.text_shake);
        mSensorManager.registerListener(this, acc_sensors, SensorManager.SENSOR_DELAY_GAME);
        lastUpdate = System.currentTimeMillis();
        last_x = 0;
        last_y = 0;
        last_z = 0;
        last_theta = 0;
        last_grav = 0;
        last_phi = 0;
        shakeIndicator = (ImageView) findViewById(R.id.indicator_shake);
        backGround = (LinearLayout) findViewById(R.id.background);
        move = 0;
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        acc_values = sensorEvent.values;
        xText.setText("X: " + roundPrecision(acc_values[0], 1));
        yText.setText("Y: " + roundPrecision(acc_values[1], 1));
        zText.setText("Z: " + roundPrecision(acc_values[2], 1));


        long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
        if ((curTime - lastUpdate) > 1000/UPDATE_RATE) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;


            float x = sensorEvent.values[SensorManager.DATA_X];
            float y = sensorEvent.values[SensorManager.DATA_Y];
            float z = sensorEvent.values[SensorManager.DATA_Z];

            float x_speed = Math.abs(x - last_x) / diffTime * 10000;
            float y_speed = Math.abs(y - last_y) / diffTime * 10000;
            float z_speed = Math.abs(z - last_z) / diffTime * 10000;

            //float max = Math.max(x_speed, y_speed);
            //max = Math.max(max, z_speed);

            // will always point down.
            double grav = Math.sqrt(
                    Math.pow((double)x, 2) +
                    Math.pow((double)y, 2)+
                    Math.pow((double)z, 2));

            // angle in x-y plane
            double theta = Math.toDegrees(Math.atan2(y, x));

            // inclination angle based on the y-z plane
            double phi = Math.toDegrees(Math.atan2(z, y));

            // value doesnt matter, difference is important
            double theta_speed = Math.abs(theta - last_theta) / diffTime * 10000;
            double grav_speed = Math.abs(grav - last_grav)  / diffTime * 10000;
            double phi_speed = Math.abs(phi - last_phi) / diffTime * 10000;

            thetaText.setText("Theta: " + roundPrecision((float)theta, 3));
            phiText.setText("Phi: " + roundPrecision((float)phi, 3));
            gravText.setText("Gravity: " + roundPrecision((float)grav, 3));

            //max = Math.max(max, (float)ang_speed);


            //if (ang_speed > SHAKE_THRESHOLD){
            //    backGround.setBackgroundColor(Color.YELLOW);
            /*
             if (x_speed > SHAKE_THRESHOLD && x_speed == max) {
                shakeText.setText("Shaking left right");
                backGround.setBackgroundColor(Color.RED);
            } else if (y_speed > SHAKE_THRESHOLD && y_speed == max) {
                shakeText.setText("Shaking up down");
                backGround.setBackgroundColor(Color.GREEN);
            }  else if (z_speed > SHAKE_THRESHOLD && z_speed == max) {
                shakeText.setText("Shaking forward backward");
                backGround.setBackgroundColor(Color.BLUE);
            }   else if (ang_speed > SHAKE_THRESHOLD && ang_speed == max){
                shakeText.setText("Twisting");
                backGround.setBackgroundColor(Color.YELLOW);
             else {
                shakeText.setText("Not shaking");
                backGround.setBackgroundColor(Color.WHITE);
            }
            */

            double polar_max = Math.max(grav_speed, theta_speed);
            polar_max = Math.max(polar_max, phi_speed);

            if(grav_speed > SHAKE_THRESHOLD && grav_speed == polar_max){
                shakeText.setText("Shaking");
                backGround.setBackgroundColor(Color.YELLOW);
            } else if (phi_speed > SHAKE_THRESHOLD && phi_speed == polar_max) {
                shakeText.setText("Tipping");
                backGround.setBackgroundColor(Color.MAGENTA);
            } else if (theta_speed > SHAKE_THRESHOLD && theta_speed == polar_max){
                shakeText.setText("rolling");
                backGround.setBackgroundColor(Color.CYAN);
            } else {
                shakeText.setText("Not shaking");
                backGround.setBackgroundColor(Color.WHITE);
            }




            last_x = x;
            last_y = y;
            last_z = z;
            last_grav = grav;
            last_theta = theta;
            last_phi = phi;
        }

    }

    /*
    private Color LerpColor(Color target){

    }
    */

    private String roundPrecision(float num, int deci){
        String numb = Float.toString(num);
        int comma = numb.indexOf(".");
        if( comma > -1 && comma < numb.length() - deci){
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
        mSensorManager.registerListener(this, acc_sensors, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
    public void switchBack(View view){
        mSensorManager.unregisterListener(this);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }*/
}
