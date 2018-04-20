package com.example.student.crackinggalaxy;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ControlActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;    // SensorManager : 센서를 다루기 위해 제공되는 시스템 서비스 객체
    private Sensor sensor;                   // Sensor : 센서 정보를 포함하고 있다.
    TextView tv_toggle;
    String strON = "ON";
    String strOFF = "OFF";
    String temp = "";

    SoundPool sound = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
    int streamId;
    Boolean threadFlag, deviceFlag;

    ToggleTask toggleTask;
    ConnectionTask connectionTask;
    Intent intent;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        tv_toggle = findViewById(R.id.tv_toggle);

        threadFlag = true;
        deviceFlag = true;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);    // 폰에 존재하는 센서목록을 가져온다.
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);   // PROXIMITY센서를 호출한다.

        new Thread(r1).start();

        intent = new Intent();
        intent = getIntent();
        id = intent.getStringExtra("id");
        new Thread(r).start();
        Log.d("idCheck###########", id);
    }

    Runnable r1 = new Runnable() {
        @Override
        public void run() {
            while (true) {
                threadFlag = true;
                Log.d("Thread.....", "Running....." + threadFlag);

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                threadFlag = false;
                Log.d("Thread.....", "Running....." + threadFlag);

            }
        }
    };


    Runnable r = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
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
        super.onPause();
        sensorManager.unregisterListener(this);
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

        if (threadFlag == true && dbDistance == 0.0) {
            if (temp.equals("")) {
                temp = strON;
            } else if (temp.equals(strON)) {
                temp = strOFF;
            } else if (temp.equals(strOFF)) {
                temp = strON;
            }
            //Toast.makeText(this, "distance(" + dbDistance + ")" , Toast.LENGTH_SHORT).show();
            Log.d("Sensor", "distance(" + dbDistance + ")");

            tv_toggle.setText(temp);
            callSound(temp);
            threadFlag = false;
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
            String status = strings[0];


            url += "?status=" + status;
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
            toggleTask = new ToggleTask("http://70.12.114.144/wc/data.do");
            toggleTask.execute(getTv_toggle());
        }
    }

    public String getTv_toggle() {
        return tv_toggle.getText().toString();
    }


}
