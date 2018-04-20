package com.example.student.crackingtablet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void clickBtn(View v) {
        if (v.getId() == R.id.btn_login) {
            if (requestLogin()) {
                successLogin();
            } else {
                failLogin();
            }
        }
    }

    public boolean requestLogin() {
        boolean result = true;


        return result;
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
