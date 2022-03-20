package com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas;

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
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.LoginStaff;
import com.example.modul_spp_ukk2021.UI.Data.Repository.LoginStaffRepository;
import com.example.modul_spp_ukk2021.UI.DB.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin.HomeAdminActivity;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaSiswa.LoginSiswaActivity;
import com.example.modul_spp_ukk2021.UI.UI.Splash.LoginChoiceActivity;
import com.github.captain_miao.optroundcardview.OptRoundCardView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.baseURL.url;

public class LoginStaffActivity extends AppCompatActivity {
    private MaterialButton masuk;
    private OptRoundCardView card;
    private ProgressDialog progressbar;
    private SharedPreferences sharedprefs;
    private EditText editUsername, editPassword;
    private TextInputLayout textInputLayout, textInputLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_staff);
        sharedprefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);

        card = findViewById(R.id.card);
        masuk = findViewById(R.id.masuk);
        editUsername = findViewById(R.id.username);
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
        masuk.setOnClickListener(v -> validateForm(editUsername.getText().toString().trim(), editPassword.getText().toString().trim()));
    }

    @Override
    public void onBackPressed() {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginStaffActivity.this, card, ViewCompat.getTransitionName(card));
        Intent intent = new Intent(LoginStaffActivity.this, LoginChoiceActivity.class);
        startActivity(intent, options.toBundle());
    }

    private void validateForm(String username, String password) {
        if (username.isEmpty()) {
            textInputLayout.setError("Username salah");
            editUsername.addTextChangedListener(new TextWatcher() {
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
            loginStaff(username, password);
            textInputLayout2.setErrorEnabled(false);
        }
    }

    private List<LoginStaff> fetchResults(Response<LoginStaffRepository> response) {
        LoginStaffRepository loginStaffRepository = response.body();
        return loginStaffRepository.getResult();
    }

    private void loginStaff(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiEndPoints api = retrofit.create(ApiEndPoints.class);
        Call<LoginStaffRepository> call = api.loginStaff(username, password);
        call.enqueue(new Callback<LoginStaffRepository>() {
            @Override
            public void onResponse(Call<LoginStaffRepository> call, Response<LoginStaffRepository> response) {
                String value = response.body().getValue();
                List<LoginStaff> results = fetchResults(response);

                if (value.equals("1")) {
                    for (int i = 0; i < results.size(); i++) {
                        String level = results.get(i).getLevel();
                        Log.e("DEBUG", "Staff level:" + level);

                        sharedprefs.edit().putString("level", level).apply();
                        sharedprefs.edit().putString("username", username).apply();

                        if (level.equals("Petugas")) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(LoginStaffActivity.this, HomePetugasActivity.class);
                                    intent.putExtra("level", level);
                                    intent.putExtra("username", username);
                                    startActivity(intent);
                                }
                            }, 600);
                        } else if (level.equals("Admin")) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(LoginStaffActivity.this, HomeAdminActivity.class);
                                    intent.putExtra("level", level);
                                    intent.putExtra("username", username);
                                    startActivity(intent);
                                }
                            }, 600);
                        }
                    }
                } else {
                    progressbar.dismiss();
                    Toast.makeText(LoginStaffActivity.this, "Login gagal, silahkan coba lagi...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginStaffRepository> call, Throwable t) {
                progressbar.dismiss();
                Toast.makeText(LoginStaffActivity.this, "Gagal koneksi sistem, silahkan coba lagi...", Toast.LENGTH_SHORT).show();
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}