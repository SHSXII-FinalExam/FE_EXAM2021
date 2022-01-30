package com.example.modul_spp_ukk2021.UI.UI.Splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.example.modul_spp_ukk2021.R;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences loginPreference;
    String MY_PREF = "my_pref";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loginPreference = getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);

        // we are adding fade animation
        // between two imageviews.
        Fade fade = new Fade();
        View decor = getWindow().getDecorView();

        // below 3 lines of code is to exclude
        // action bar,title bar and navigation
        // bar from animation.
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);

        // we are adding fade animation
        // for enter transition.
        getWindow().setEnterTransition(fade);

        // we are also setting fade
        // animation for exit transition.
        getWindow().setExitTransition(fade);

        final ImageView imageView = findViewById(R.id.logo_ts);

        if (loginPreference.getString("tag", "notok").equals("notok")) {
            SharedPreferences.Editor edit = loginPreference.edit();
            edit.putString("tag", "ok");
            edit.apply();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this, imageView, ViewCompat.getTransitionName(imageView));
                    Intent intent = new Intent(SplashActivity.this, LoginChoiceActivity.class);
                    startActivity(intent, options.toBundle());
                    finish();
                }
            }, 2000);

        } else if (loginPreference.getString("tag", null).equals("ok")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this, imageView, ViewCompat.getTransitionName(imageView));
                    Intent intent = new Intent(SplashActivity.this, LoginChoiceActivity.class);
                    startActivity(intent, options.toBundle());
                    finish();
                }
            }, 2000);
        }

    }
}
