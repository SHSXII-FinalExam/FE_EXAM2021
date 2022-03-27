package com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Repository.LoginSiswaRepository;
import com.example.modul_spp_ukk2021.UI.UI.Splash.LoginChoiceActivity;
import com.github.captain_miao.optroundcardview.OptRoundCardView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.baseURL.url;

public class LoginSiswaActivity extends AppCompatActivity {
    private OptRoundCardView card;
    private SharedPreferences sharedprefs;
    private EditText editNISN, editPassword;
    private LottieAnimationView loadingProgress;
    private TextInputLayout textInputLayout, textInputLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_siswa);
        sharedprefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);

        card = findViewById(R.id.card);
        editNISN = findViewById(R.id.nisn);
        editPassword = findViewById(R.id.password);
        MaterialButton masuk = findViewById(R.id.masuk);
        MaterialButton kembali = findViewById(R.id.kembali);
        textInputLayout = findViewById(R.id.textInputLayout);
        loadingProgress = findViewById(R.id.loadingProgress);
        textInputLayout2 = findViewById(R.id.textInputLayout2);

        kembali.setOnClickListener(v -> onBackPressed());
        masuk.setOnClickListener(v -> validateForm(editNISN.getText().toString().trim(), editPassword.getText().toString().trim()));
    }

    @Override
    public void onBackPressed() {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginSiswaActivity.this, card, ViewCompat.getTransitionName(card));
        Intent intent = new Intent(LoginSiswaActivity.this, LoginChoiceActivity.class);
        startActivity(intent, options.toBundle());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void validateForm(String nisn, String password) {
        if (nisn.length() < 10) {
            textInputLayout2.setErrorEnabled(true);
            textInputLayout.setError("NISN kurang/salah");
            editNISN.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    textInputLayout.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        } else if (password.isEmpty()) {
            textInputLayout2.setErrorEnabled(true);
            textInputLayout2.setError("Password kosong/salah");
            editPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    textInputLayout2.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        } else {
            loginSiswa(nisn, password);
            textInputLayout2.setErrorEnabled(false);
            loadingProgress.playAnimation();
            loadingProgress.setVisibility(LottieAnimationView.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void loginSiswa(String nisn, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);

        Call<LoginSiswaRepository> call = api.loginSiswa(nisn, password);
        call.enqueue(new Callback<LoginSiswaRepository>() {
            @Override
            public void onResponse(Call<LoginSiswaRepository> call, Response<LoginSiswaRepository> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (value.equals("1")) {
                            sharedprefs.edit().putString("nisnSiswa", nisn).apply();
                            sharedprefs.edit().putString("passwordSiswa", password).apply();
                            Log.e("DEBUG", "NISN:" + nisn);

                            Intent intent = new Intent(LoginSiswaActivity.this, HomeSiswaActivity.class);
                            startActivity(intent);

                        } else {
                            loadingProgress.pauseAnimation();
                            loadingProgress.setVisibility(LottieAnimationView.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(LoginSiswaActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 2000);
            }

            @Override
            public void onFailure(Call<LoginSiswaRepository> call, Throwable t) {
                loadingProgress.pauseAnimation();
                loadingProgress.setVisibility(LottieAnimationView.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(LoginSiswaActivity.this, "Gagal koneksi sistem, silahkan coba lagi...", Toast.LENGTH_LONG).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}