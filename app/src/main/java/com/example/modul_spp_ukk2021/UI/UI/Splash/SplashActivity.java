package com.example.modul_spp_ukk2021.UI.UI.Splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin.HomeAdminActivity;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.HomePetugasActivity;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa.HomeSiswaActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedprefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String nisnSiswa = sharedprefs.getString("nisnSiswa", null);
        String passwordSiswa = sharedprefs.getString("passwordSiswa", null);
        String usernameStaff = sharedprefs.getString("usernameStaff", null);
        String levelStaff = sharedprefs.getString("levelStaff", null);
        String passwordStaff = sharedprefs.getString("passwordStaff", null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (nisnSiswa != null && passwordSiswa != null && haveNetworkConnection()) {
                    Intent intent = new Intent(SplashActivity.this, HomeSiswaActivity.class);
                    intent.putExtra("nisnSiswa", nisnSiswa);
                    startActivity(intent);

                } else if (usernameStaff != null & levelStaff != null & passwordStaff != null && levelStaff.equals("Petugas") && haveNetworkConnection()) {
                    Intent intent = new Intent(SplashActivity.this, HomePetugasActivity.class);
                    intent.putExtra("level", levelStaff);
                    intent.putExtra("username", usernameStaff);
                    startActivity(intent);

                } else if (usernameStaff != null & levelStaff != null & passwordStaff != null && levelStaff.equals("Admin") && haveNetworkConnection()) {
                    Intent intent = new Intent(SplashActivity.this, HomeAdminActivity.class);
                    intent.putExtra("level", levelStaff);
                    intent.putExtra("username", usernameStaff);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(SplashActivity.this, OnboardingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        }, 2000);
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
