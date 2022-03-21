package com.example.modul_spp_ukk2021.UI.UI.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.LoginStaffActivity;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa.LoginSiswaActivity;
import com.github.captain_miao.optroundcardview.OptRoundCardView;

public class LoginChoiceActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_choice);
        OptRoundCardView card = findViewById(R.id.card);
        TextView btnLoginSiswa = findViewById(R.id.btn_siswa);
        TextView btnLoginPetugas = findViewById(R.id.btn_petugas);

//        ScaleAnimation scaleAnim = new ScaleAnimation(
//                1f, 1f,
//                0f, 1f,
//                Animation.ABSOLUTE, 0,
//                Animation.RELATIVE_TO_SELF, 1);
//        scaleAnim.setDuration(500);
//        scaleAnim.setRepeatCount(0);
//        scaleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
//        scaleAnim.setFillAfter(true);
//        scaleAnim.setFillBefore(false);
//        scaleAnim.setFillEnabled(true);
//        card.startAnimation(scaleAnim);

        btnLoginSiswa.setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginChoiceActivity.this, card, ViewCompat.getTransitionName(card));
            Intent intent = new Intent(LoginChoiceActivity.this, LoginSiswaActivity.class);
            startActivity(intent, options.toBundle());
            getWindow().setExitTransition(null);
        });
        btnLoginPetugas.setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginChoiceActivity.this, card, ViewCompat.getTransitionName(card));
            Intent intent = new Intent(LoginChoiceActivity.this, LoginStaffActivity.class);
            startActivity(intent, options.toBundle());
            getWindow().setExitTransition(null);
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
}
