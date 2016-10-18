package com.example.jack_pc.internet_practicev10;

import android.media.Image;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class AirConditionFragment extends BaseFragment {

    private static final String DATA_NAME = "name";

    private String title = "";

    public static AirConditionFragment newInstance(String title,
                                                   int indicatorColor, int dividerColor) {
        AirConditionFragment f = new AirConditionFragment();
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
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.air_condition_controller, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.textView_controller = (TextView) view.findViewById(R.id.textView_controller);
        MainActivity.imageView_ACpower = (ImageView) view.findViewById(R.id.imageView_ACpower);
        MainActivity.imageView_plus = (ImageView) view.findViewById(R.id.imageView_plus);
        MainActivity.imageView_minus = (ImageView) view.findViewById(R.id.imageView_minus);
        MainActivity.imageView_plus.setBackgroundColor(
                getResources().getColor(R.color.colorController));
        MainActivity.imageView_minus.setBackgroundColor(
                getResources().getColor(R.color.colorController));
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
