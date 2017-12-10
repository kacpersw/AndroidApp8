package com.example.ibra18plus.zadanie8;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

    /**
     * Created by Ibra18plus on 2017-12-10.
     */

    public static class TodoTask {
        private long id;
        private String description;
        private boolean completed;

        public TodoTask(long id, String description, boolean completed) {
            this.id = id;
            this.description = description;
            this.completed = completed;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }
}
