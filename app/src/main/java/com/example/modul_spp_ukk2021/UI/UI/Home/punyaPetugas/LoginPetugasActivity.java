package com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.Data.Model.LoginStaff;
import com.example.modul_spp_ukk2021.UI.Data.Repository.LoginStaffRepository;
import com.example.modul_spp_ukk2021.UI.Network.ApiEndPoints;
import com.example.modul_spp_ukk2021.UI.UI.Home.punyaAdmin.HomeAdminFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.Network.baseURL.url;

public class LoginPetugasActivity extends AppCompatActivity {
    private EditText editUsername, editPassword;
    private TextInputLayout textInputLayout, textInputLayout2;
    private MaterialButton kembali, masuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pp_activity_login);

        editUsername = findViewById(R.id.username);
        editPassword = findViewById(R.id.password);
        textInputLayout = findViewById(R.id.textInputLayout);
        textInputLayout2 = findViewById(R.id.textInputLayout2);
        masuk = findViewById(R.id.masuk);
        kembali = findViewById(R.id.kembali);

        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void validateForm() {
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

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
        } else {
            textInputLayout.setErrorEnabled(false);
            loadDataPembayaran(username, password);
        }

        if (password.isEmpty()) {
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
            loadDataPembayaran(username, password);
        }
    }

    private List<LoginStaff> fetchResults(Response<LoginStaffRepository> response) {
        LoginStaffRepository loginStaffRepository = response.body();
        return loginStaffRepository.getResult();
    }

    private void loadDataPembayaran(String username, String password) {
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
                    Log.e("keshav", "Level ->" + level);

                    if (value.equals("1") && level.equals("petugas")) {
                        Intent intent = new Intent(LoginPetugasActivity.this, DataSiswaActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
                    } else if (value.equals("1") && level.equals("admin")) {
                        Intent intent = new Intent(LoginPetugasActivity.this, HomeAdminFragment.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
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