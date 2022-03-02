package com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas;

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
import com.example.modul_spp_ukk2021.UI.Data.Model.LoginStaff;
import com.example.modul_spp_ukk2021.UI.Data.Repository.LoginStaffRepository;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin.HomeAdminActivity;
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

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class LoginStaffActivity extends AppCompatActivity {
    private OptRoundCardView card;
    private MaterialButton masuk;
    private EditText editUsername, editPassword;
    private TextInputLayout textInputLayout, textInputLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_staff);
        card = findViewById(R.id.card);
        masuk = findViewById(R.id.masuk);
        editUsername = findViewById(R.id.username);
        editPassword = findViewById(R.id.password);
        MaterialButton kembali = findViewById(R.id.kembali);
        textInputLayout = findViewById(R.id.textInputLayout);
        textInputLayout2 = findViewById(R.id.textInputLayout2);

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
            textInputLayout2.setErrorEnabled(false);
            loginStaff(username, password);
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

                for (int i = 0; i < results.size(); i++) {
                    String level = results.get(i).getLevel();
                    Log.e("DEBUG", "Staff level:" + level);

                    if (value.equals("1") && level.equals("Petugas")) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginStaffActivity.this, masuk, ViewCompat.getTransitionName(masuk));
                                Intent intent = new Intent(LoginStaffActivity.this, HomePetugasActivity.class);
                                intent.putExtra("level", level);
                                intent.putExtra("username", username);
                                startActivity(intent, options.toBundle());
                            }
                        }, 600);
                    } else if (value.equals("1") && level.equals("Admin")) {
                        Intent intent = new Intent(LoginStaffActivity.this, HomeAdminActivity.class);
                        intent.putExtra("level", level);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginStaffRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}