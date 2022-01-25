package com.example.modul_spp_ukk2021.UI.Home.punyaAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modul_spp_ukk2021.R;

public class DataKelasActivity extends AppCompatActivity {
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_kelas);

        btnBack = findViewById(R.id.imageView);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DataKelasActivity.this, BottomNavigationAdmin.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
