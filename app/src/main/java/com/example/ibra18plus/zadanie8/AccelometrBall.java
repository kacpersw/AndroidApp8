package com.example.ibra18plus.zadanie8;

import android.app.Activity;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class AccelometrBall extends Activity implements SensorEventListener {

    private TextView textView;
    private TextView textView2;
    private ImageView imageView;
    private SensorManager sensorManager;
    private Sensor accSensor;
    private boolean isSensorPresent = false;
    private float x, y;
    private boolean hasStarted = false;
    private boolean hasSyringe = false;
    private float scalePlus = (float) 0.10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelometr_ball);

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        imageView = (ImageView) findViewById(R.id.barbellIV);

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
            sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_FASTEST);
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
        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        float maxX = mdispSize.x - 270;
        float maxY = mdispSize.y - 270;

        if (!hasStarted) {
            x = maxX / 2;
            y = maxY / 2;
            hasStarted = !hasStarted;
            hasSyringe = false;
        } else {
            x -= (float) event.values[0];
            y += (float) event.values[1];
        }

        if (!hasSyringe) {
            float randomX = nextFloat(0, maxX);
            float randomY = nextFloat(0, maxY);
            hasSyringe = !hasSyringe;
        }


        if (x >= 0 && y >= 0 && x <= maxX && y <= maxY) {
            imageView.setX(x);
            imageView.setY(y);
            textView.setText("x: " + event.values[0] + "\ny: " + event.values[1] + "\nz: " + event.values[2]);
        } else {
            Toast.makeText(getApplicationContext(), "Przegrałeś", Toast.LENGTH_SHORT).show();
            hasStarted = false;
            imageView.setScaleX(1);
            imageView.setScaleY(1);
        }

    }

    public boolean collision(ImageView a, ImageView b) {
        float bl = a.getY();
        float bt = a.getX();
        float br = a.getWidth() + bl;
        float bb = a.getHeight() + bt;
        float pl = b.getY();
        float pt = b.getX();
        float pr = b.getWidth() + pl;
        float pb = b.getHeight() + pt;
        if (bl <= pr && bl >= pl && bt >= pt && bt <= pb) {
            return true;
        } else if (br >= pl && br <= pr && bb >= pt && bb <= pb) {
            return true;
        } else if (bt <= pb && bt >= pt && br >= pl && br <= pr) {
            return true;
        } else if (bb >= pt && bb <= pb && bl >= pl && bl <= pr) {
            return true;
        }
        return false;
    }

    public float nextFloat(float min, float max) {
        Random random = new Random();
        return min + random.nextFloat() * (max - min);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}