package com.example.ibra18plus.zadanie8;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ProximitySensor extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mProximity;
    private static final int SENSOR_SENSITIVITY = 4;
    private boolean isSensorPresent = false;
    private TextView textView;
    private int farCounter = 0;
    private int nearCounter = 0;
    private int counter = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity_sensor);
        textView = (TextView) findViewById(R.id.proximityTV);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
            mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            isSensorPresent = true;
        } else {
            isSensorPresent = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorPresent) {
            mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(getApplicationContext(), "Czujnik zbliżeniowy wznowiony", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSensorPresent) {
            mSensorManager.unregisterListener(this);
            Toast.makeText(getApplicationContext(), "Czujnik zbliżeniowy zatrzymany", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                ++nearCounter;
                textView.setText("blisko");
                textView.setBackgroundResource(R.color.red);
                if (nearCounter == 2) {
                    Toast.makeText(getApplicationContext(), "Po następnym zbliżeniu aplikacja zamknie sie", Toast.LENGTH_SHORT).show();
                } else if (nearCounter == 3) {
                    System.exit(0);
                }

            } else {
                ++farCounter;
                textView.setBackgroundResource(R.color.green);
                textView.setTextSize(counter);
                counter +=10;
                textView.setText("Daleko");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
