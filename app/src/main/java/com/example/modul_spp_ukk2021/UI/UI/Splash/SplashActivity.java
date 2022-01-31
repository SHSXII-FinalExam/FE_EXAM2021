package com.example.modul_spp_ukk2021.UI.UI.Splash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.example.modul_spp_ukk2021.R;

public class SplashActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Fade fade = new Fade();
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        ImageView logo = findViewById(R.id.logo_ts);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this, logo, ViewCompat.getTransitionName(logo));
                Intent intent = new Intent(SplashActivity.this, LoginChoiceActivity.class);
                startActivity(intent, options.toBundle());
            }
        }, 2000);
    }
}
