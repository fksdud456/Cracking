package com.example.student.crackingtablet;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReceiveData extends AsyncTask<String, String, String> {
    String url;

    ReceiveData() {

    }

    ReceiveData(String url) {
        this.url = url;
    }

    public void addParameter(String parameter) {
        url += parameter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            URL url = new URL(this.url);
            conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "*/*");

                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                reader = new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream()
                        )
                );

                String line = null;
                while (true) {
                    line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                }

            }
        } catch (Exception e) {
            return e.getMessage();
        } finally {
            try {
                if (reader != null)
                    reader.close(); // 반드시 끊어주자!
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(conn!= null)
                conn.disconnect();
        }
        return sb.toString();
    }

}
