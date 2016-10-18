package com.example.jack_pc.internet_practicev10;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.Map;


public class WashingClothesFragment extends BaseFragment {

    private static final String DATA_NAME = "name";

    private String title = "";

    public static WashingClothesFragment newInstance(String title, int indicatorColor, int dividerColor) {
        WashingClothesFragment f = new WashingClothesFragment();
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
        View view = inflater.inflate(R.layout.washingclothes_lineup, container, false);
        MainActivity.listView_WCinfo = (ListView) view.findViewById(R.id.listView_WCinfo);
        MainActivity.button_update = (Button) view.findViewById(R.id.button_update);
        MainActivity.button_lineup = (Button) view.findViewById(R.id.button_lineup);
        MainActivity.textView_WCStime = (TextView) view.findViewById(R.id.textView_WCStime);
        MainActivity.textView_WCSinfo = (TextView) view.findViewById(R.id.textView_WCSinfo);
        MainActivity.imageView_BT = (ImageView) view.findViewById(R.id.imageView_BT);
        MainActivity.Adapter_WCinfo = new SimpleAdapter(
                getActivity(), MainActivity.List_WCinfo, android.R.layout.simple_list_item_2
                , new String[] {"DeviceName", "EstimateTime"}
                , new int[] {android.R.id.text1, android.R.id.text2});
        MainActivity.listView_WCinfo.setAdapter(MainActivity.Adapter_WCinfo);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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