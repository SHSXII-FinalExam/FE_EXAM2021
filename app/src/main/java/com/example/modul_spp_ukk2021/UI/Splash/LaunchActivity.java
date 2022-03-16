package com.example.modul_spp_ukk2021.UI.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.example.modul_spp_ukk2021.R;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        ImageView logo = findViewById(R.id.logo_ts);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(LaunchActivity.this, logo, ViewCompat.getTransitionName(logo));
                Intent intent = new Intent(LaunchActivity.this, SplashActivity.class);
                startActivity(intent, options.toBundle());
            }
        }, 2000);
    }
}
