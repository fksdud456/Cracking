package com.example.student.crackinggalaxy;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText et_id, et_pwd;
    LoginTask loginTask;
    Boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_id= findViewById(R.id.et_id);
        et_pwd= findViewById(R.id.et_pwd);
    }
    public void clickBtn(View v){
        if(v.getId()==R.id.btn_apply){
            Intent intent = new Intent(this, ApplyActivity.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.btn_login){
            String id = et_id.getText().toString();
            String pwd= et_pwd.getText().toString();

            if(id!=null && pwd != null){
                loginTask = new LoginTask("http://70.12.114.144/wc/login.do");
                loginTask.execute(id.trim(), pwd.trim());
            }
        }
    }

    class LoginTask extends AsyncTask<String, Void, String>{
        String url;
        public LoginTask() {}
        public LoginTask(String url){
            this.url = url;
        }


        @Override
        protected String doInBackground(String... strings) {
            String id= strings[0];
            String pwd= strings[1];

            url += "?id="+id+"&pwd="+pwd;
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
            Log.d("excute",s);
            if(s.equals("1")){
                Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ControlActivity.class);
                intent.putExtra("id",et_id.getText().toString());
                startActivity(intent);
                finish();
            }
            else if(s.equals("0")){
                Toast.makeText(MainActivity.this, "Login Fail, Please try again", Toast.LENGTH_SHORT).show();
            }
        }
        }
    }



