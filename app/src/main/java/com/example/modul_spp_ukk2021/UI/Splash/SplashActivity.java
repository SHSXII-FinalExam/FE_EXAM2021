package com.example.modul_spp_ukk2021.UI.Splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modul_spp_ukk2021.R;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences loginPreference;
    String MY_PREF = "my_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loginPreference = getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);

        if (loginPreference.getString("tag", "notok").equals("notok")) {
            SharedPreferences.Editor edit = loginPreference.edit();
            edit.putString("tag", "ok");
            edit.apply();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginChoiceActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);

        } else if (loginPreference.getString("tag", null).equals("ok")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginChoiceActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }

    }
}
