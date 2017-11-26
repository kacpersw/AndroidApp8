package com.example.ibra18plus.zadanie8;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Accelometr extends Activity implements SensorEventListener {

    private TextView textView;
    private SensorManager sensorManager;
    private Sensor accSensor;
    private boolean isSensorPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_accelometr);
        textView = (TextView) findViewById(R.id.accTV);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isSensorPresent = true;
        } else {
            isSensorPresent = false;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorPresent) {
            sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(getApplicationContext(), "Akcelerometr wznowiony", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSensorPresent) {
            sensorManager.unregisterListener(this);
            Toast.makeText(getApplicationContext(), "Akcelerometr zatrzymany", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        textView.setText("x: " + event.values[0] + "\ny: " + event.values[1] + "\nz: " + event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
