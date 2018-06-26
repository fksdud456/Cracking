package com.example.student.crackingtablet;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;

public class SendData extends AsyncTask<String, String, Void> {
    String url;

    SendData(String url) {
        this.url = Util.wcURL + url;
    }

    public void setParameter(String parameter) {
        url += parameter;
    }

    @Override
    protected Void doInBackground(String... strings) {
        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(this.url);
            conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "*/*");

                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return null;
    }
}
