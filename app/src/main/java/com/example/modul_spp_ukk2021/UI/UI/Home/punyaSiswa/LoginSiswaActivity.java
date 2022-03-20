package com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Repository.LoginSiswaRepository;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas.LoginStaffActivity;
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
    private MaterialButton masuk;
    private OptRoundCardView card;
    private ProgressDialog progressbar;
    private SharedPreferences sharedprefs;
    private EditText editNISN, editPassword;
    private TextInputLayout textInputLayout, textInputLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_siswa);
        sharedprefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);

        card = findViewById(R.id.card);
        masuk = findViewById(R.id.masuk);
        editNISN = findViewById(R.id.nisn);
        editPassword = findViewById(R.id.password);
        MaterialButton kembali = findViewById(R.id.kembali);
        textInputLayout = findViewById(R.id.textInputLayout);
        textInputLayout2 = findViewById(R.id.textInputLayout2);

        progressbar = new ProgressDialog(this);
        progressbar.setMessage("Loading...");
        progressbar.setCancelable(false);
        progressbar.setIndeterminate(true);
        progressbar.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressbar.dismiss();

        kembali.setOnClickListener(v -> onBackPressed());
        masuk.setOnClickListener(v -> validateForm(editNISN.getText().toString().trim(), editPassword.getText().toString().trim()));
    }

    @Override
    public void onBackPressed() {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginSiswaActivity.this, card, ViewCompat.getTransitionName(card));
        Intent intent = new Intent(LoginSiswaActivity.this, LoginChoiceActivity.class);
        startActivity(intent, options.toBundle());
    }

    private void validateForm(String nisn, String password) {
        if (nisn.length() < 10) {
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
            progressbar.show();
            loginSiswa(nisn, password);
            textInputLayout2.setErrorEnabled(false);
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

                if (value.equals("1")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sharedprefs.edit().putString("nisn",nisn).apply();
                            Intent intent = new Intent(LoginSiswaActivity.this, HomeSiswaActivity.class);
                            intent.putExtra("nisnSiswa", nisn);
                            startActivity(intent);
                        }
                    }, 600);
                } else {
                    progressbar.dismiss();
                    Toast.makeText(LoginSiswaActivity.this, "Login gagal, silahkan coba lagi...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginSiswaRepository> call, Throwable t) {
                progressbar.dismiss();
                Toast.makeText(LoginSiswaActivity.this, "Gagal koneksi sistem, silahkan coba lagi...", Toast.LENGTH_SHORT).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}