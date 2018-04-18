package com.example.student.crackinggalaxy;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApplyActivity extends AppCompatActivity {
    EditText et_id,et_pwd, et_name;
    ApplyTask applyTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        et_id=findViewById(R.id.et_id);
        et_pwd=findViewById(R.id.et_pwd);
        et_name=findViewById(R.id.et_name);
    }

    public void clickBtn(View v){
        if(v.getId()==R.id.btn_ok){
            String id = et_id.getText().toString();
            String pwd = et_pwd.getText().toString();
            String name = et_name.getText().toString();

            if(id!=null && pwd!=null && name!=null){
                   applyTask = new ApplyTask("http://70.12.114.144/wc/apply.do");
                   applyTask.execute(id.trim(), pwd.trim(), name.trim());
            }
        }
        if(v.getId()==R.id.btn_cancel){
            finish();
        }
    }

    class ApplyTask extends AsyncTask<String, Void, String>{
        String url;
        public ApplyTask() {}

        public ApplyTask(String url) {
            this.url = url;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            String pwd = strings[1];
            String name = strings[2];

            url += "?id="+id+"&pwd="+pwd+"&name="+name;
            StringBuilder sb = new StringBuilder();
            HttpURLConnection con = null;
            BufferedReader br = null;

            //http통신
            try{
                URL url = new URL(this.url);
                con = (HttpURLConnection)url.openConnection();
                Log.d("aaa###########",this.url);
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
                return e.getMessage();
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

}
