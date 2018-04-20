package com.example.student.crackingtablet;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class ConnectionService extends Service {
    private final String wcURL = "http://70.12.114.150/wc";

    private ReceiveData connectionReceiver;
    private boolean flag = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        processCommane(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    public void processCommane(Intent intent) {
        String command = intent.getStringExtra("command");
        String name = intent.getStringExtra("name");

        final Intent sintent = new Intent(getApplicationContext(), MainActivity.class);
        sintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                while(flag) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //좌표를 가져오기
                    Log.d("connectionReceiver ::" , "run");
                    connectionReceiver = new ReceiveData(wcURL+"/connection.do");
                    connectionReceiver.addParameter("?comm=t");
                    try {
                        String res = connectionReceiver.execute().get();
                        sintent.putExtra("command", "conn");
                        sintent.putExtra("res", res);
                        startActivity(sintent);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        new Thread(r).start();
    }

    public void changeFlag() {
        flag = false;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
