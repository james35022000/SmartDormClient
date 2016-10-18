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


public class DebugFragment extends BaseFragment {

    private static final String DATA_NAME = "name";

    private String title = "";

    public static DebugFragment newInstance(String title, int indicatorColor, int dividerColor) {
        DebugFragment f = new DebugFragment();
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
        return inflater.inflate(R.layout.debug_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.button_send = (Button) view.findViewById(R.id.button_send);
        MainActivity.editText_IP = (EditText) view.findViewById(R.id.editText_IP);
        MainActivity.editText_port = (EditText) view.findViewById(R.id.editText_port);
        MainActivity.editText_send = (EditText) view.findViewById(R.id.editText_send);
        MainActivity.editText_device = (EditText) view.findViewById(R.id.editText_device);
        MainActivity.mDataField  = (TextView) view.findViewById(R.id.mDataField);
        MainActivity.echoResult = (ScrollView) view.findViewById(R.id.echoResult);
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
