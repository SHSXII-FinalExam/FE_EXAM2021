package com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Repository.LoginSiswaRepository;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.UI.Splash.LoginChoiceActivity;
import com.github.captain_miao.optroundcardview.OptRoundCardView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class LoginSiswaActivity extends AppCompatActivity {
    private OptRoundCardView card;
    private MaterialButton masuk;
    private EditText editNISN, editPassword;
    private TextInputLayout textInputLayout, textInputLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_siswa);
        card = findViewById(R.id.card);
        masuk = findViewById(R.id.masuk);
        editNISN = findViewById(R.id.nisn);
        editPassword = findViewById(R.id.password);
        textInputLayout = findViewById(R.id.textInputLayout);
        MaterialButton kembali = findViewById(R.id.kembali);
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
            textInputLayout2.setErrorEnabled(false);
            loginSiswa(nisn, password);
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
                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginSiswaActivity.this, masuk, ViewCompat.getTransitionName(masuk));
                            Intent intent = new Intent(LoginSiswaActivity.this, HomeSiswaActivity.class);
                            intent.putExtra("nisnSiswa", nisn);
                            startActivity(intent, options.toBundle());
                        }
                    }, 1000);
                }
            }

            @Override
            public void onFailure(Call<LoginSiswaRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}