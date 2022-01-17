package com.example.modul_spp_ukk2021.UI.UI.Home.punyaPetugas;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modul_spp_ukk2021.R;
import com.example.modul_spp_ukk2021.UI.UI.Splash.LoginChoiceActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class LoginPetugasActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword;
    TextInputLayout textInputLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_petugasadmin);

        LoginForm();
    }

    private void LoginForm() {
        edtUsername = findViewById(R.id.login_PetugasUsername);
        edtPassword = findViewById(R.id.login_PetugasPass);
        MaterialButton btnSignInSiswa = findViewById(R.id.signin_petugas);
        ImageView btnBack = findViewById(R.id.imageView);
        textInputLayout2 = findViewById(R.id.textInputLayout2);

        btnSignInSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPetugasActivity.this, LoginChoiceActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void validateForm() {
        String nisn = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (nisn.isEmpty()) {
            edtUsername.setError("Username kosong/salah");
        } else if (password.isEmpty()) {
            edtPassword.setError("Password kosong/salah");
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
            Intent intent = new Intent(LoginPetugasActivity.this, PetugasBottomNavigation.class);
            startActivity(intent);
            finish();
        }
    }
}