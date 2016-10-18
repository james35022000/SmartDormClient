package com.example.jack_pc.internet_practicev10;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class TemperatureFragment extends BaseFragment {

    private static final String DATA_NAME = "name";

    private String title = "";

    public static TemperatureFragment newInstance(String title, int indicatorColor, int dividerColor) {
        TemperatureFragment f = new TemperatureFragment();
        f.setTitle(title);
        f.setIndicatorColor(indicatorColor);
        f.setDividerColor(dividerColor);


        //pass data
        Bundle args = new Bundle();
        args.putString(DATA_NAME, title);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get data
        title = getArguments().getString(DATA_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.temperature_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.textView_temperature = (TextView) view.findViewById(R.id.textView_temperature);
        MainActivity.textView_humidity = (TextView) view.findViewById(R.id.textView_humidity);
        MainActivity.textView_updatetime = (TextView) view.findViewById(R.id.textView_updatetime);
        MainActivity.textView_distance = (TextView) view.findViewById(R.id.textView_distance);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
