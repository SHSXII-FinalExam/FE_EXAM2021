package com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa.LoginSiswaActivity;
import com.example.modul_spp_ukk2021.UI.UI.Splash.LoginChoiceActivity;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;

public class KonfirmasiPetugasActivity extends AppCompatActivity {
    TextView textViewNama, textViewTagihan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_petugas);

        textViewNama = findViewById(R.id.textView5);
        textViewNama.setText(getIntent().getStringExtra("siswaNama"));

        SharedPreferences settings = getApplicationContext().getSharedPreferences("totalTagihan", 0);
        int totalTagihan = settings.getInt("tagihanSiswa", 0);

        textViewTagihan = findViewById(R.id.textView6);
        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);
        textViewTagihan.setText(format.format(totalTagihan));

        MaterialButton btnKurangBayar = findViewById(R.id.materialButton3);
        btnKurangBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KonfirmasiPetugasActivity.this, KurangBayarActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
