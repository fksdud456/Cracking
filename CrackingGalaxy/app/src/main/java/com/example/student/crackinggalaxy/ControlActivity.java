package com.example.student.crackinggalaxy;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ControlActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;    // SensorManager : 센서를 다루기 위해 제공되는 시스템 서비스 객체
    private Sensor sensor;                   // Sensor : 센서 정보를 포함하고 있다.
    TextView tv_toggle;
    String strON = "ON";
    String strOFF = "OFF";
    String temp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_control );
        tv_toggle=findViewById(R.id.tv_toggle);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE );    // 폰에 존재하는 센서목록을 가져온다.
        sensor = sensorManager.getDefaultSensor( Sensor.TYPE_PROXIMITY );   // PROXIMITY센서를 호출한다.
        //1.SENSOR_DELAY_FASTEST          0ms 최대한 빠르게
        //2.SENSOR_DELAY_GAME        20,000ms 게임에 적합한 속도
        //3.SENSOR_DELAY_UI          60,000ms UI 수정에 적합한 속도
        //4.SENSOR_DELAY_NORMAL     200,000ms 화면 방향 변화를 모니터링하기에 적합한

    }

    @Override
    protected void onResume(){
        super.onResume( );
        sensorManager.registerListener( this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause( );
        sensorManager.unregisterListener( this );
    }

    @Override
    public void onAccuracyChanged( Sensor sensor, int accuracy )  {
    }

    @Override
    public void onSensorChanged( SensorEvent event ) {
        float dbDistance = event.values[0];
        // 스마트폰 위에 손을 올리면 아래와 같이 출력되고
        // distance(0.0)
        // 손을 치우면 아래와 같이 출력된다.
        // distance(8.0)
        //Toast.makeText(this, "distance(" + dbDistance + ")" , Toast.LENGTH_SHORT).show();
        Log.d( "Sensor", "distance(" + dbDistance + ")" );


        if(dbDistance==0.0){
            if(temp.equals("")){
                temp=strON;

            }

            else if(temp.equals(strON)){
                temp=strOFF;
            }
            else if(temp.equals(strOFF)){
                temp=strON;
            }
            tv_toggle.setText(temp);
        }



    }


}
