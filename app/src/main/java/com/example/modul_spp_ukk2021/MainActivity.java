package com.example.modul_spp_ukk2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button btnSiswa = findViewById(R.id.btn_siswa);
        Button btnPetugas = findViewById(R.id.btn_petugas);

        btnSiswa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent masukSiswa = new Intent(MainActivity.this,LoginSiswa.class);
                view.getContext().startActivity(masukSiswa);}
        });

        btnPetugas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent masukPetugas = new Intent(MainActivity.this,LoginPetugas.class);
                view.getContext().startActivity(masukPetugas);}
        });
    }
}