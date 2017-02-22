package com.example.muhammadimran.saylaniproject;

import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.muhammadimran.saylaniproject.SignUp.SignUp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiDex.install(this);


    }

    public void loginClick(View view) {
    }

    public void noAccount(View view) {
        getSupportFragmentManager().beginTransaction().add(R.id.mainContainer, new SignUp()).commit();

    }

}
