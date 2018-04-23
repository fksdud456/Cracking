package com.example.student.crackingtablet;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class ConnService extends Service {
    private final String TAG = "ConnectionService ::";
    private final String wcURL = "http://70.12.114.144/wc";

    private ReceiveData connectionReceiver;
    private boolean flag = true;

    public ConnService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG , "onStartCommand");
        if (intent == null) {
            return Service.START_STICKY;
        } else {
            processCommand(intent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void processCommand(Intent intent) {
        String command = intent.getStringExtra("command");
        String name = intent.getStringExtra("name");

        final Intent sintent = new Intent(getApplicationContext(), MainActivity.class);
        sintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                while(flag) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    try {
                        connectionReceiver = new ReceiveData(wcURL+"/connection.do");
                        connectionReceiver.addParameter("?comm=t");
                        String res = connectionReceiver.execute().get();
                        sintent.putExtra("command", "conn");
                        sintent.putExtra("res", res);
                        startActivity(sintent);

                        connectionReceiver = new ReceiveData(wcURL+"/loginuser.do");
                        connectionReceiver.addParameter("?comm=t");
                        res = connectionReceiver.execute().get();
                        sintent.putExtra("command", "login");
                        sintent.putExtra("res", res);
                        startActivity(sintent);

                        connectionReceiver = new ReceiveData(wcURL+"/data.do");
                        connectionReceiver.addParameter("?comm=t");
                        res = connectionReceiver.execute().get();
                        sintent.putExtra("command", "data");
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

    @Override
    public void onDestroy() {
        flag = false;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
