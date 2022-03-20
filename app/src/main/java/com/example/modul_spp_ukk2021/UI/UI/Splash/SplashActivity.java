package com.example.modul_spp_ukk2021.UI.UI.Splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin.HomeAdminActivity;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.HomePetugasActivity;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.LoginStaffActivity;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa.HomeSiswaActivity;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa.LoginSiswaActivity;

public class SplashActivity extends AppCompatActivity {
    private String nisn, username, level;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView logo = findViewById(R.id.logo_ts);

        SharedPreferences sharedprefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        nisn = sharedprefs.getString("nisn", null);
        username = sharedprefs.getString("username", null);
        level = sharedprefs.getString("level", null);

        if (nisn != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, HomeSiswaActivity.class);
                    intent.putExtra("nisnSiswa", nisn);
                    startActivity(intent);
                }
            }, 2000);
        } else if (username != null & level != null && level.equals("Petugas")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, HomePetugasActivity.class);
                    intent.putExtra("level", level);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            }, 2000);
        } else if (username != null & level != null && level.equals("Admin")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, HomeAdminActivity.class);
                    intent.putExtra("level", level);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            }, 2000);
        } else {
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
}
