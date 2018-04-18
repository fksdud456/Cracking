package com.example.student.crackingtablet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void clickBtn(View v) {
        if(v.getId() == R.id.btn_login) {

        }
    }
}
