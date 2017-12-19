package com.example.ibra18plus.zadanie8;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private static int count = 0;
    private static int countHelper = count;
    private static ListView listView;
    private static ArrayList<Sensor> arrayWithPlan = new ArrayList<>();
    private static ArrayAdapter<Sensor> listAdapter;

    private final String ACCELOMETER = "accelerometer";
    private final String PROXIMITY = "proximity";


    private static final String QUERY_URL = "http://openlibrary.org/search.json?q=";

    private Button btnApi;

    public static void setListAdapter() {
        MainActivity.listAdapter.notifyDataSetChanged();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // mainTextView.setText("Set in Java!");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Configuration configuration = getResources().getConfiguration();


        setContentView(R.layout.activity_main);


        btnApi=(Button) findViewById(R.id.btAPI);


        btnApi.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(v.getContext(), Main2Activity.class);
                startActivity(intent);
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        arrayWithPlan.addAll(deviceSensors);


        TextView sensorTextView = (TextView) findViewById(R.id.SensorTextView);
        sensorTextView.setText(getResources().getQuantityString(R.plurals.sensorsSummaryInfo, deviceSensors.size(), deviceSensors.size()));

        listView = (ListView) findViewById(R.id.listView);

        listAdapter = new CustomAdapter(this, arrayWithPlan);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                count = listView.getCheckedItemCount();
                if (count == 0) {
                    Toast.makeText(MainActivity.this, R.string.foundZero, Toast.LENGTH_LONG).show();
                    return;
                }
                Resources res = getResources();
                String itemsFound = res.getQuantityString(R.plurals.numberOfSelectedItems, count, count);
                Toast.makeText(MainActivity.this, "" + itemsFound, Toast.LENGTH_LONG).show();
                countHelper = count;
            }
        });

    }








    public void goSensorAcitivity(View view) {
        Button b = (Button) findViewById(R.id.goToSensorButton);
        int id = listView.getPositionForView(view);
        Sensor sensor = (Sensor) listView.getItemAtPosition(id);
        String msg = sensor.getName().toString();
        if (msg.toLowerCase().contains(ACCELOMETER.toLowerCase())) {
            Intent intent2 = new Intent(MainActivity.this, Accelometr.class);
            startActivity(intent2);
        } else if (msg.toLowerCase().contains(PROXIMITY.toLowerCase())) {
            Intent intent2 = new Intent(MainActivity.this, ProximitySensor.class);
            startActivity(intent2);
        }

    }

    public void goBall(View view) {
        Intent intent2 = new Intent(MainActivity.this, AccelometrBall.class);
        startActivity(intent2);
    }

    /**
     * Created by Ibra18plus on 2017-12-10.
     */

    public static class ToDoTask {
    }
}
