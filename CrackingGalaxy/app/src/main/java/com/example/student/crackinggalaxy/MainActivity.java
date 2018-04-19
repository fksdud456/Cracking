package com.example.student.crackinggalaxy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickBtn(View v){
        if(v.getId()==R.id.btn_apply){
            Intent intent = new Intent(this, ApplyActivity.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.btn_login){
            //Intent intent = new Intent(this, ApplyActivity.class);
            //startActivity(intent);
        }
    }
}
