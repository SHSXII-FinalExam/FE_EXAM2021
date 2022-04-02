package com.example.modul_spp_ukk2021.UI.Home.punyaPetugas;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.DB.APIEndPoints;
import com.example.modul_spp_ukk2021.UI.Data.Model.LoginStaff;
import com.example.modul_spp_ukk2021.UI.Data.Repository.LoginStaffRepository;
import com.example.modul_spp_ukk2021.UI.Home.punyaAdmin.HomeAdminActivity;
import com.example.modul_spp_ukk2021.UI.Splash.LoginChoiceActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.modul_spp_ukk2021.UI.DB.BaseURL.url;

public class LoginStaffActivity extends AppCompatActivity {
    private TextInputLayout textInputLayout2;
    private EditText edtUsername, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_staff);

        edtPassword = findViewById(R.id.login_PetugasPass);
        ImageView btnBack = findViewById(R.id.imageView11);
        edtUsername = findViewById(R.id.login_PetugasUsername);
        textInputLayout2 = findViewById(R.id.textInputLayout2);
        MaterialButton btnLoginStaff = findViewById(R.id.signin_petugas);

        btnLoginStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginStaffActivity.this, LoginChoiceActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void validateForm() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty()) {
            edtUsername.setError("Username Kurang, silahkan coba lagi...");
        } else if (password.isEmpty()) {
            edtPassword.setError("Password Kurang, silahkan coba lagi...");
            textInputLayout2.setEndIconVisible(false);

            edtPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    textInputLayout2.setEndIconVisible(true);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        } else {
            loadDataPembayaran(username, password);
        }
    }

    private void loadDataPembayaran(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIEndPoints api = retrofit.create(APIEndPoints.class);

        Call<LoginStaffRepository> call = api.loginStaff(username, password);
        call.enqueue(new Callback<LoginStaffRepository>() {
            @Override
            public void onResponse(Call<LoginStaffRepository> call, Response<LoginStaffRepository> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                List<LoginStaff> results = response.body().getResult();

                if (value.equals("1")) {
                    for (int i = 0; i < results.size(); i++) {
                        String level = results.get(i).getLevel();
                        String id_petugas = results.get(i).getId_petugas();
                        Log.e("Level: ", level);

                        if (level.equalsIgnoreCase("petugas")) {
                            Intent intent = new Intent(LoginStaffActivity.this, HomePetugasActivity.class);
                            intent.putExtra("level", level);
                            intent.putExtra("username", username);
                            intent.putExtra("id_petugas", id_petugas);
                            startActivity(intent);
                        } else if (level.equalsIgnoreCase("admin")) {
                            Intent intent = new Intent(LoginStaffActivity.this, HomeAdminActivity.class);
                            intent.putExtra("level", level);
                            intent.putExtra("username", username);
                            intent.putExtra("id_petugas", id_petugas);
                            startActivity(intent);
                        }
                    }
                } else {
                    Toast.makeText(LoginStaffActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginStaffRepository> call, Throwable t) {
                Log.e("DEBUG", "Error: ", t);
            }
        });
    }
}