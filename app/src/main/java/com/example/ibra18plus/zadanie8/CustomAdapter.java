package com.example.ibra18plus.zadanie8;

import android.content.Context;
import android.hardware.Sensor;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ibra18plus on 2017-11-24.
 */

public class CustomAdapter extends ArrayAdapter<Sensor> {


    public CustomAdapter(@NonNull Context context, @LayoutRes ArrayList<Sensor> sensors) {
        super(context, R.layout.layout_row, sensors);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.layout_row, parent, false);
        Sensor singlePosition = getItem(position);

        TextView textView4 = (TextView) view.findViewById(R.id.textView4);
        textView4.setText(singlePosition.getName().toString());

        Button button = (Button) view.findViewById(R.id.goToSensorButton);


        return view;
    }

    @Override
    public void remove(Sensor object) {
        super.remove(object);
    }


}
