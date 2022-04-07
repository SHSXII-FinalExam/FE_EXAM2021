package com.example.modul_spp_ukk2021.UI.UI.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.LoginStaffActivity;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa.LoginSiswaActivity;
import com.example.modul_spp_ukk2021.UI.UI.Splash.BoardingAdapter.BoardingAdapter;
import com.example.modul_spp_ukk2021.UI.UI.Splash.BoardingAdapter.DataBoarding;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private BoardingAdapter boardingAdapter;
    private LinearLayout layoutOnboardingIndicator;
    private boolean doubleBackToExitPressedOnce = false;
    private MaterialButton btnLanjut, btnSiswa, btnStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_boarding);

        btnSiswa = findViewById(R.id.btnSiswa);
        btnStaff = findViewById(R.id.btnStaff);
        btnLanjut = findViewById(R.id.btnLanjut);
        viewPager = findViewById(R.id.viewPager);
        TextView tvLewati = findViewById(R.id.tvLewati);
        layoutOnboardingIndicator = findViewById(R.id.layoutOnboardingIndicators);

        setOnboardingItem();
        viewPager.setAdapter(boardingAdapter);
        viewPager.setOffscreenPageLimit(boardingAdapter.getItemCount());
        setOnboadingIndicator();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicators(position);
            }
        });

        if (getIntent().getExtras() != null) {
            viewPager.setCurrentItem(boardingAdapter.getItemCount());
        }

        btnLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() < boardingAdapter.getItemCount()) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
        });

        tvLewati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() < boardingAdapter.getItemCount()) {
                    viewPager.setCurrentItem(boardingAdapter.getItemCount());
                }
            }
        });

        btnSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnboardingActivity.this, LoginSiswaActivity.class);
                startActivity(intent);
            }
        });

        btnStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnboardingActivity.this, LoginStaffActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan tombol kembali lagi untuk keluar", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void setOnboardingItem() {
        List<DataBoarding> dataBoardings = new ArrayList<>();

        DataBoarding a = new DataBoarding();
        a.setTitle("Bayar SPP");
        a.setDescription("Bayar tagihan SPP di perangkat kamu dimanapun kamu berada dengan SiAKAD");
        a.setImage(R.drawable.onboarding_1);

        DataBoarding b = new DataBoarding();
        b.setTitle("Awasi Transaksi");
        b.setDescription("Kontrol Tagihan dan Riwayatmu melalui SiAKAD serta kontak petugas untuk update transaksi SPP kamu");
        b.setImage(R.drawable.onboarding_2);

        DataBoarding c = new DataBoarding();
        c.setTitle("Kenali Dirimu");
        c.setDescription("Masuk Sebagai Siswa atau Staff");
        c.setImage(R.drawable.onboarding_3);

        dataBoardings.add(a);
        dataBoardings.add(b);
        dataBoardings.add(c);
        boardingAdapter = new BoardingAdapter(dataBoardings, OnboardingActivity.this);
    }

    private void setOnboadingIndicator() {
        ImageView[] indicators = new ImageView[boardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_inactive));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicator.addView(indicators[i]);
        }
    }

    private void setCurrentOnboardingIndicators(int index) {
        int childCount = layoutOnboardingIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutOnboardingIndicator.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_inactive));
            }
        }
        if (index == boardingAdapter.getItemCount() - 1) {
            btnLanjut.setVisibility(View.INVISIBLE);
            btnSiswa.setVisibility(View.VISIBLE);
            btnStaff.setVisibility(View.VISIBLE);
        } else {
            btnLanjut.setVisibility(View.VISIBLE);
            btnSiswa.setVisibility(View.INVISIBLE);
            btnStaff.setVisibility(View.INVISIBLE);
        }
    }
}