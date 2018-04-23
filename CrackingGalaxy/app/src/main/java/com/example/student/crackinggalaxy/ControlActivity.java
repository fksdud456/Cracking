package com.example.student.crackinggalaxy;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.media.AudioManager;
import android.media.SoundPool;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class ControlActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;    // SensorManager : 센서를 다루기 위해 제공되는 시스템 서비스 객체
    private Sensor sensor;                   // Sensor : 센서 정보를 포함하고 있다.
    TextView tv_toggle;
    String strON = "ON";
    String strOFF = "OFF";
    String temp = "";
    Switch switch1;

    SoundPool sound = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
    int streamId;
    Boolean threadFlag, deviceFlag;

    ToggleTask toggleTask;
    ConnectionTask connectionTask;
    LocationTask locationTask;
    Intent intent;
    String id;
    double latitude; //35.5~38.5
    double longitude; // 126.6~128.8
    String result;
    Thread t1,t2,t3,t4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        tv_toggle = findViewById(R.id.tv_toggle);
        switch1 = findViewById(R.id.switch1);

        threadFlag = true;
        deviceFlag = true;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);    // 폰에 존재하는 센서목록을 가져온다.
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);   // PROXIMITY센서를 호출한다.

        new Thread(r1).start();
        new Thread(r).start();

        intent = new Intent();
        intent = getIntent();
        id = intent.getStringExtra("id");
        Log.d("idCheck###########",id);

        TextView tvId = findViewById(R.id.tv_userId);
        tvId.setText("ID : " + id);

        latitude = ((Math.random()*(38.5-35.5+1))+35.5);
        longitude = ((Math.random()*(128.8-126.6+1))+126.6);
        String lat = String.valueOf(latitude);
        String lon = String.valueOf(longitude);

        locationTask = new LocationTask("http://70.12.114.144/wc/location.do");
        locationTask.execute(lat, lon, id);

        t1 = new Thread(chk_disconnect);
        t1.start();
    }

    Runnable chk_disconnect = new Runnable() {
        @Override
        public void run() {
            while (!t1.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(3000);
                    DisconnectTask disconnectTask = new DisconnectTask("http://70.12.114.144/wc/disconnect.do");
                    disconnectTask.execute(id);

            } catch(Exception e){
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
       }
    }
    };


    Runnable r1 = new Runnable() {
        @Override
        public void run() {
            while (true) {
                threadFlag = true;
                //Log.d("Thread.....", "Running....." + threadFlag);

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                threadFlag = false;
                //Log.d("Thread.....", "Running....." + threadFlag);

            }
        }
    };


    Runnable r = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            connectionTask = new ConnectionTask("http://70.12.114.144/wc/connection.do");
            connectionTask.execute(id);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        //1.SENSOR_DELAY_FASTEST          0ms 최대한 빠르게
        //2.SENSOR_DELAY_GAME        20,000ms 게임에 적합한 속도
        //3.SENSOR_DELAY_UI          60,000ms UI 수정에 적합한 속도
        //4.SENSOR_DELAY_NORMAL     200,000ms 화면 방향 변화를 모니터링하기에 적합한
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float dbDistance = event.values[0];
        // 스마트폰 위에 손을 올리면 아래와 같이 출력되고
        // distance(0.0)
        // 손을 치우면 아래와 같이 출력된다.
        // distance(8.0)


        int color = getResources().getColor(R.color.colorPrimaryDark);
        if (threadFlag == true && dbDistance == 0.0) {
            if (temp.equals("")) {
                temp = strON;
                color = getResources().getColor(R.color.colorAccent);
                switch1.setChecked(true);
            } else if (temp.equals(strON)) {
                temp = strOFF;
                switch1.setChecked(false);
            } else if (temp.equals(strOFF)) {
                temp = strON;
                color = getResources().getColor(R.color.colorAccent);
                        //Color.parseColor("#cc33ff");
                switch1.setChecked(true);
            }
            //Toast.makeText(this, "distance(" + dbDistance + ")" , Toast.LENGTH_SHORT).show();
            Log.d("Sensor", "distance(" + dbDistance + ")");


            tv_toggle.setTextColor(color);
            tv_toggle.setText(temp);

            callSound(temp);
            threadFlag = false;

            toggleTask = new ToggleTask("http://70.12.114.144/wc/data.do");
            toggleTask.execute(id, getTv_toggle());
        }
    }

    public void callSound(String temp) {
        int soundId = 0;
        if (temp == strON) {
            soundId = sound.load(this, R.raw.on, 1);
        } else {
            soundId = sound.load(this, R.raw.off, 1);
        }
        streamId = sound.play(soundId, 0.5F, 0.5F, 1, 0, 1.0F);
    }

    class DisconnectTask extends  AsyncTask<String, Void, String>{
        String url="";
        public DisconnectTask() {}
        public DisconnectTask(String url){
            this.url= url;
        }

        @Override
        protected String doInBackground(String[] strs) {
            String id = strs[0];
            url += "?comm=s&id="+id;
            StringBuilder sb = new StringBuilder();
            HttpURLConnection con = null;
            BufferedReader br = null;
            //http통신
            try{
                URL url = new URL(this.url);
                con = (HttpURLConnection)url.openConnection();
                if(con!=null){
                    con.setReadTimeout(10000); //제한시간
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept","*/*");
                    if(con.getResponseCode()!=HttpURLConnection.HTTP_OK)
                        return null;
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line = null;
                    while(true){
                        line=br.readLine();
                        if(line==null)
                            break;
                        sb.append(line);
                    }
                }
            }
            catch (Exception e){
                Log.d("errorCheck",e.getMessage());
            }
            finally {
                try{
                    if(br!=null)
                        br.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                con.disconnect();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("disdischkchk$$$", s);
            if (s.equals("1")) {
                onBackPressed();
            }
            else if(s==null){
                //return;
                Log.d("disdischkchk$$$", "no signal");
            }
        }
    }

    class LocationTask extends AsyncTask<String, Void, String>{
        String url="";
        public LocationTask() {}
        public LocationTask(String url){
            this.url= url;
        }

        @Override
        protected String doInBackground(String[] strs) {
            String latitude = strs[0].substring(0,8);
            String longitude = strs[1].substring(0,9);
            String id = strs[2];

            url += "?comm=s&lat="+latitude+"&lon="+longitude+"&id="+id;

            StringBuilder sb = new StringBuilder();
            HttpURLConnection con = null;
            BufferedReader br = null;
            //http통신
            try{
                Log.d("try check############",url);
                URL url = new URL(this.url);
                con = (HttpURLConnection)url.openConnection();

                if(con!=null){
                    con.setReadTimeout(10000); //제한시간
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept","*/*");
                    if(con.getResponseCode()!=HttpURLConnection.HTTP_OK)
                        return null;
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line = null;
                    while(true){
                        Log.d("while check############",this.url);
                        line=br.readLine();
                        if(line==null)
                            break;
                        sb.append(line);
                    }
                }

            }
            catch (Exception e){
                Log.d("errorCheck",e.getMessage());
            }
            finally {
                try{
                    if(br!=null)
                        br.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                con.disconnect();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    class ConnectionTask extends AsyncTask<String, Void, String> {
        String url;

        public ConnectionTask() {
        }

        public ConnectionTask(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String[] strings) {
            String id = strings[0];
            url += "?comm=s&id=" + id;
            StringBuilder sb = new StringBuilder();
            HttpURLConnection con = null;
            BufferedReader br = null;
            //http통신
            try {
                Log.d("try check############", url);
                URL url = new URL(this.url);
                con = (HttpURLConnection) url.openConnection();

                if (con != null) {
                    con.setReadTimeout(10000); //제한시간
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept", "*/*");
                    if (con.getResponseCode() != HttpURLConnection.HTTP_OK)
                        return null;
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line = null;
                    while (true) {
                        Log.d("while check############", this.url);
                        line = br.readLine();
                        if (line == null)
                            break;
                        sb.append(line);
                    }
                }

            } catch (Exception e) {
                return e.getMessage();
            } finally {
                try {
                    if (br != null)
                        br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                con.disconnect();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("excute", s);
            if (s.equals("1")) {
                //Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
            } else if (s.equals("0")) {
                //Toast.makeText(MainActivity.this, "Login Fail, Please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ToggleTask extends AsyncTask<String, String, String> {
        String url;
        public ToggleTask() {
        }

        public ToggleTask(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String[] strings) {
            String id = strings[0];
            String status = strings[1];
            url += "?comm=s&id="+id+"&status=" + status;
            StringBuilder sb = new StringBuilder();
            HttpURLConnection con = null;
            BufferedReader br = null;
            //http통신
            try {
                Log.d("try check############", url);
                URL url = new URL(this.url);
                con = (HttpURLConnection) url.openConnection();

                if (con != null) {
                    con.setReadTimeout(10000); //제한시간
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept", "*/*");
                    if (con.getResponseCode() != HttpURLConnection.HTTP_OK)
                        return null;
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line = null;
                    while (true) {
                        Log.d("while check############", this.url);
                        line = br.readLine();
                        if (line == null)
                            break;
                        sb.append(line);
                    }
                }

            } catch (Exception e) {
                return e.getMessage();
            } finally {
                try {
                    if (br != null)
                        br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                con.disconnect();
            }
            return sb.toString();
        }
    }




    @Override
    public void onBackPressed() {
        String address = "http://70.12.114.144/wc/logout.do?comm=s&id="+id;
        HttpURLConnection con=null;
        try {
            URL url = new URL(address);
            con = (HttpURLConnection)url.openConnection();

            if(con!=null){
                con.setReadTimeout(10000); //제한시간
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept","*/*");
                if(con.getResponseCode()!=HttpURLConnection.HTTP_OK)
                    return;
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
        }

        t1.interrupt();
        finish();
    }

    @Override
    protected void onDestroy() {
        Log.d("onDestroy", "please kill me");
        t1.interrupt();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        super.onDestroy();
    }

    public String getTv_toggle() {
        return tv_toggle.getText().toString();
    }
}
