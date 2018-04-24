package com.example.student.crackingtablet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText et_id, et_pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_id= findViewById(R.id.et_id);
        et_pwd = findViewById(R.id.et_pwd);
    }

    public void clickBtn(View v) {
        if (v.getId() == R.id.btn_login) {
            String id = et_id.getText().toString().trim();
            String pwd = et_pwd.getText().toString().trim();
            if(id.equals("admin") && pwd.equals("admin")){
                successLogin();
            }
            else{
                failLogin();
            }
        }
    }

    public void successLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void failLogin() {
        Toast.makeText(this, "Not available ID, Password", Toast.LENGTH_SHORT).show();
    }
}
