package com.example.modul_spp_ukk2021.UI.UI.Splash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.LoginPetugasActivity;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa.LoginSiswaActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class LoginChoiceActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choice);

        Fade fade = new Fade();
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        TextView btnLoginPetugas = findViewById(R.id.btn_petugas);
        TextView btnLoginSiswa = findViewById(R.id.btn_siswa);
        MaterialCardView card = findViewById(R.id.card);

        ScaleAnimation scaleAnim = new ScaleAnimation(
                1f, 1f,
                0f, 1f,
                Animation.ABSOLUTE, 0,
                Animation.RELATIVE_TO_SELF, 1);
        scaleAnim.setDuration(500);
        scaleAnim.setRepeatCount(0);
        scaleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnim.setFillAfter(true);
        scaleAnim.setFillBefore(true);
        scaleAnim.setFillEnabled(true);

        card.startAnimation(scaleAnim);

        btnLoginSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginChoiceActivity.this, card, ViewCompat.getTransitionName(card));
                Intent intent = new Intent(LoginChoiceActivity.this, LoginSiswaActivity.class);
                startActivity(intent, options.toBundle());
            }
        });

        btnLoginPetugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginChoiceActivity.this, card, ViewCompat.getTransitionName(card));
                Intent intent = new Intent(LoginChoiceActivity.this, LoginPetugasActivity.class);
                startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
