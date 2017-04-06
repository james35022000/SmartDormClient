package com.example.jack_pc.internet_practicev10;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import android.os.Handler;
import java.lang.Object;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    //*************Debug***************
    final boolean debug_mode = true;


    // Variables------------------------------------------------------
    Thread thread_create;
    private BluetoothAdapter mBluetoothAdapter;
    static Button button_send;
    static Button button_update;
    static Button button_lineup;
    static EditText editText_IP;
    static EditText editText_port;
    static EditText editText_send;
    static EditText editText_device;
    static TextView mDataField;
    static TextView textView_WCStime;
    static TextView textView_WCSinfo;
    static TextView textView_controller;
    static TextView textView_temperature;
    static TextView textView_humidity;
    static TextView textView_updatetime;
    static TextView textView_distance;
    static ScrollView echoResult;
    static ListView listView_WCinfo;
    static ImageView imageView_ACpower;
    static ImageView imageView_plus;
    static ImageView imageView_minus;
    static ImageView imageView_BT;
    static ArrayList<Map<String, String>> List_WCinfo;
    static SimpleAdapter Adapter_WCinfo;
    boolean SendBack;
    boolean ACpower;
    static boolean WCS_Update_state;
    String SendBuffer;
    static String WCS_Update_data;
    Socket clientSocket;
    OutputStream out, out_WCS;
    InputStream in, in_WCS;
    boolean connected = false, connecting = false;
    private enum command {
        DISPLAY_DISTANCE, TIME_UPDATE, WCS_UPDATE, WCS_LINEUP, WCS_AUTH, ROOM_INFO, NOTACOMMAND;
        public static command convertTocommand(int input) {
            switch(input) {
                case 0:
                    return command.TIME_UPDATE;
                case 1:
                    return command.DISPLAY_DISTANCE;
                case 21:
                    return command.WCS_UPDATE;
                case 22:
                    return command.WCS_LINEUP;
                case 23:
                    return command.WCS_AUTH;
                case 31:
                    return command.ROOM_INFO;
                default:
                    return command.NOTACOMMAND;
            }
        }
    }
    private Runnable create_Socket = new Runnable() {
        @Override
        public void run() {
            try {
                connecting = true;
                invalidateOptionsMenu();
                byte[] data = new byte[100];
                boolean welFlag = false;
                InetAddress serverIP = InetAddress.getByName(editText_IP.getText().toString());
                int serverPort = Integer.parseInt(editText_port.getText().toString());
                clientSocket = new Socket(serverIP, serverPort);
                out = clientSocket.getOutputStream();
                in = clientSocket.getInputStream();

                // Send "Welcome" Message to server
                out.write(">>99\0".getBytes());

                // Listen
                while(true) {
                    int dataLength = in.read(data);
                    if(dataLength > 0) {
                        String recvData = new String(data, 0, dataLength);
                        if(!welFlag) {
                            if(recvData.equals("<<99welcome")) {
                                connected = true;
                                connecting = false;
                                invalidateOptionsMenu();
                                Message mMsg = recvMsg.obtainMessage(99, "Welcome");
                                recvMsg.sendMessage(mMsg);
                                welFlag = true;
                            }
                            else {
                                connected = false;
                                connecting = false;
                                invalidateOptionsMenu();
                                Message mMsg = recvMsg.obtainMessage(99, recvData);
                                recvMsg.sendMessage(mMsg);
                                clientSocket.close();
                                break;
                            }
                        }
                        else {
                            if(isCommand(recvData.toCharArray())){
                                if(SendBack) {
                                    out.write((SendBuffer + "\0").getBytes());
                                    Message mMsg = recvMsg.obtainMessage(1, SendBuffer);
                                    recvMsg.sendMessage(mMsg);
                                }
                            }
                            Message mMsg = recvMsg.obtainMessage(1, recvData);
                            recvMsg.sendMessage(mMsg);
                        }
                    }
                }
            } catch (Exception e) {
                connected = false;
                connecting = false;
                invalidateOptionsMenu();
                Message mMsg = recvMsg.obtainMessage(99, "Lost Connection");
                recvMsg.sendMessage(mMsg);
            }
        }
    };

    private Runnable WCS_Socket = new Runnable() {
        @Override
        public void run() {
            try {
                connecting = true;
                invalidateOptionsMenu();
                byte[] data = new byte[100];
                boolean welFlag = false;
                InetAddress serverIP = InetAddress.getByName(editText_IP.getText().toString());
                int serverPort = Integer.parseInt(editText_port.getText().toString());
                clientSocket = new Socket(serverIP, serverPort);
                out_WCS = clientSocket.getOutputStream();
                in_WCS = clientSocket.getInputStream();

                // Send "Welcome" Message to server
                out_WCS.write(">>99\0".getBytes());

                // Listen
                while(true) {
                    int dataLength = in_WCS.read(data);
                    if(dataLength > 0) {
                        String recvData = new String(data, 0, dataLength);
                        if(!welFlag) {
                            if(recvData.equals("<<99welcome")) {
                                connected = true;
                                connecting = false;
                                invalidateOptionsMenu();
                                Message mMsg = recvMsg.obtainMessage(99, "Welcome");
                                recvMsg.sendMessage(mMsg);
                                welFlag = true;
                            }
                            else {
                                connected = false;
                                connecting = false;
                                invalidateOptionsMenu();
                                Message mMsg = recvMsg.obtainMessage(99, recvData);
                                recvMsg.sendMessage(mMsg);
                                clientSocket.close();
                                break;
                            }
                        }
                        else {
                            if(isCommand(recvData.toCharArray())){
                                if(SendBack) {
                                    out_WCS.write((SendBuffer + "\0").getBytes());
                                    Message mMsg = recvMsg.obtainMessage(1, SendBuffer);
                                    recvMsg.sendMessage(mMsg);
                                }
                            }
                            Message mMsg = recvMsg.obtainMessage(1, recvData);
                            recvMsg.sendMessage(mMsg);
                        }
                    }
                }
            } catch (Exception e) {
                connected = false;
                connecting = false;
                invalidateOptionsMenu();
                Message mMsg = recvMsg.obtainMessage(99, "Lost Connection");
                recvMsg.sendMessage(mMsg);
            }
        }
    };

    public static Handler recvMsg = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    mDataField.append((String)msg.obj + "\n");
                    break;
                case 21:
                    WCS_Update((String)msg.obj);
                    break;
                case 23:
                    WCS_BT_Status(Integer.parseInt((String)msg.obj));
                    break;
                case 31:
                    setRoomInfo((String)msg.obj);
                    break;
                case 99:
                    mDataField.setText((String)msg.obj + "\n");
                    break;
                default:
                    break;
            }
        }
    };


    // Program start------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List_WCinfo = new ArrayList<Map<String, String>>();
        initActionBar();
        initTabFragment(savedInstanceState);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ACpower = true;
        connected = false;
        connecting = false;
        SendBack = false;
        WCS_Update_state = false;

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    // My functions------------------------------------------------------
    private boolean isCommand(char[] data) {
        if(data[0] == '<' && data[1] == '<') {
            command cmd = command.convertTocommand((data[2] - '0')*10 + (data[3] - '0'));
            String info = new String((new StringBuilder(new String(data))).delete(0, 4));
            return runCommand(cmd, info);
        }
        return false;
    }

    private boolean runCommand(command cmd, String info) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("dd.MM.yy.EE;HH:mm:ss");
        switch (cmd) {
            case TIME_UPDATE:
                SendBack = true;
                //Toast.makeText(MainActivity.this, sDateFormat.format(new Date()), Toast.LENGTH_SHORT).show();
                SendBuffer = ">>00" + (new SimpleDateFormat("dd.MM.yy.")).format(new Date()) + "1;" +
                        (new SimpleDateFormat("HH:mm:ss")).format(new Date());
                return true;
            case DISPLAY_DISTANCE:
                SendBack = false;
                //textView_ResultDis.setText(info + "cm");
                return true;
            case WCS_UPDATE:
                SendBack = false;
                recvMsg.sendMessage(recvMsg.obtainMessage(21, info));
                return true;
            case WCS_LINEUP:
                SendBack = false;
                return true;
            case WCS_AUTH:
                SendBack = false;
                recvMsg.sendMessage(recvMsg.obtainMessage(23, info));
                return true;
            case ROOM_INFO:
                SendBack = false;
                recvMsg.sendMessage(recvMsg.obtainMessage(31, info));
                return true;
            case NOTACOMMAND:
                SendBack = false;
                return false;
            default:
                SendBack = false;
                return false;
        }
    }

    private static void WCS_Update(String info) {
        try {
            if (!WCS_Update_state) {
                if(info.split(",")[0].equals("&&Finish")) {
                    WCS_Update_state = true;
                    info = "," + info.split(",")[1];
                }
                WCS_Update_data += info;
            } else {
                if (WCS_Update_data.equals("")) {
                    List_WCinfo.clear();
                    Adapter_WCinfo.notifyDataSetChanged();
                    textView_WCStime.setText("Server Time : Fail to get server info");
                    WCS_Update_state = false;
                    WCS_Update_data = "";
                } else {
                    String[] token = WCS_Update_data.split(",");
                    textView_WCSinfo.setText("Queuing number : " + Integer.toString(token.length - 1));
                    textView_WCStime.setText("Server Time : " + timeStandardize(token[0].toCharArray()));
                    List_WCinfo.clear();
                    for (int i = 1; i < token.length; i++) {
                        String[] sub_token = token[i].split(":");
                        Map<String, String> M = new HashMap<String, String>();
                        M.put("DeviceName", sub_token[0]);
                        if (sub_token[1].equals("1")) {
                            M.put("EstimateTime", "Finish time : " + timeStandardize(sub_token[2].toCharArray()));
                        } else {
                            M.put("EstimateTime", "Finish time : Not yet begun");
                        }
                        List_WCinfo.add(M);
                    }
                    WCS_Update_state = false;
                    WCS_Update_data = "";
                    Adapter_WCinfo.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {  }
    }

    private static String timeStandardize(char time[])
    {
        return "" + time[0] + time[1] + "/" + time[2] + time[3] + " "
                + time[4] + time[5] + ":" + time[6] + time[7];
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setTitle(R.string.main_title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorTitle));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initTabFragment(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            TabFragment tabFragment = new TabFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_fragment, tabFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if(connecting) {
            menu.findItem(R.id.menu_connecting).setActionView(R.layout.progress_bar_layout);
            menu.findItem(R.id.menu_connected).setVisible(false);
            menu.findItem(R.id.menu_reconnect).setVisible(false);
        }
        else if(connected) {
            menu.findItem(R.id.menu_connecting).setActionView(null);
            menu.findItem(R.id.menu_connected).setVisible(true);
            menu.findItem(R.id.menu_reconnect).setVisible(false);
        }
        else {
            menu.findItem(R.id.menu_connecting).setActionView(null);
            menu.findItem(R.id.menu_connected).setVisible(false);
            menu.findItem(R.id.menu_reconnect).setVisible(true);
        }
        return true;
    }

    public void reconnect_onclick(MenuItem item) {
        try {
            setVibrate(30);
            mDataField.setText("");
            if (thread_create != null) thread_create.interrupt();
            thread_create = new Thread(create_Socket);
            thread_create.start();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Reconnect to server fail !", Toast.LENGTH_SHORT).show();
        }
    }

    private void button_send_func() {
        try {
            if (editText_send.getText() != null) {
                if(!editText_send.getText().toString().equals("")) {
                    SendBuffer = editText_send.getText().toString();
                    out.write((SendBuffer + "\0").getBytes());
                    out.flush();
                }
            }
        } catch (Exception e) {
            connected = false;
            connecting = false;
            invalidateOptionsMenu();
            Message mMsg = recvMsg.obtainMessage(99, "Unexpected error! Please reconnect.");
            recvMsg.sendMessage(mMsg);
            //Toast.makeText(MainActivity.this, "Send string fail...", Toast.LENGTH_SHORT).show();
        }
    }

    private void button_lineup_func() {
        try {
            out.write((">>22" + editText_device.getText().length() +
                    editText_device.getText().toString() + "\0").getBytes());
            out.flush();
            Thread.sleep(500);
            out.write(">>21\0".getBytes());
            out.flush();
        } catch (Exception e) {
            List_WCinfo.clear();
            Adapter_WCinfo.notifyDataSetChanged();
            Toast.makeText(MainActivity.this,
                    "Please Update List First !", Toast.LENGTH_SHORT).show();
        }
    }

    private void button_update_func() {
        try {
            List_WCinfo.clear();
            Adapter_WCinfo.notifyDataSetChanged();
            out.write(">>21\0".getBytes());
            out.flush();
        } catch (Exception e) {
            List_WCinfo.clear();
            Adapter_WCinfo.notifyDataSetChanged();
            Toast.makeText(MainActivity.this,
                    "Connect to Server Fail !", Toast.LENGTH_SHORT).show();
        }
    }

    public void setVibrate(int time) {
        Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(time);
    }

    private void ACpower() {
        if(ACpower) {
            imageView_ACpower.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_power_setting_red));
            textView_controller.setVisibility(View.VISIBLE);
            ACpower = !ACpower;
        }
        else {
            imageView_ACpower.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_power_settings));
            textView_controller.setVisibility(View.INVISIBLE);
            ACpower = !ACpower;
        }
    }

    private void ACcontroller(boolean cmd) {
        if(!ACpower) {
            int temperature;
            temperature = (textView_controller.getText().toString().toCharArray()[0] - '0') * 10 +
                    (textView_controller.getText().toString().toCharArray()[1] - '0');
            if(cmd) {
                if (temperature < 32)
                    textView_controller.setText(Integer.toString(temperature + 1) + " °C");
            }
            else {
                if (temperature > 18)
                    textView_controller.setText(Integer.toString(temperature - 1) + " °C");
            }
        }
    }

    private void getRoomInfo() {
        textView_temperature.setText("26 °C");
        textView_humidity.setText("54 %");
        try {
            out.write(">>31".getBytes());
            out.flush();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this,
                    "Error !", Toast.LENGTH_SHORT).show();
        }
    }

    private static void setRoomInfo(String info) {
        /*if(Integer.getInteger(info) > 100)
            textView_distance.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDistanceWarning));
        else
            textView_distance.setTextColor(getResources().getColor(R.color.colorDistance));*/
        String[] token = info.split(":");
        textView_distance.setText(token[0] + " cm");
        textView_updatetime.setText(timeStandardize(token[1].toCharArray()));
    }

    private void imageView_send_BT() {
        try {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
            out.write((">>23" + editText_device.getText().length() +
                    editText_device.getText().toString() + "\0").getBytes());
            out.flush();
            Thread.sleep(500);
            out.write(">>21\0".getBytes());
            out.flush();
        } catch (Exception e) {   }
    }

    private static void WCS_BT_Status(int cmd) {
        switch (cmd) {
            case 0:
                imageView_BT.setBackgroundColor(Color.RED);
                break;
            case 1:
                imageView_BT.setBackgroundColor(Color.GREEN);
                break;
            case 2:
                imageView_BT.setBackgroundColor(Color.WHITE);
                break;
            case 3:
                imageView_BT.setBackgroundColor(Color.YELLOW);
                break;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_send:
                button_send_func();
                break;
            case R.id.button_lineup:
                button_lineup_func();
                break;
            case R.id.button_update:
                button_update_func();
                break;
            case R.id.imageView_plus:
                setVibrate(30);
                ACcontroller(true);
                break;
            case R.id.imageView_minus:
                setVibrate(30);
                ACcontroller(false);
                break;
            case R.id.imageView_BT:
                setVibrate(30);
                imageView_send_BT();
                break;
            case R.id.imageView_update:
                setVibrate(30);
                getRoomInfo();
                break;
            case R.id.imageView_ACpower:
                setVibrate(30);
                ACpower();
            default:
                break;
        }
    }


}
