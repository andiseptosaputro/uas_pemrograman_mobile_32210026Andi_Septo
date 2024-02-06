package com.example.simpleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    TextView textViewRegisterNow;
    TextInputEditText textInputEditTextName, textInputEditTextPassword;
    Button buttonSubmit;
    String username, password, level;
    TextView textViewError;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textInputEditTextName = findViewById(R.id.username);
        textInputEditTextPassword = findViewById(R.id.password);
        textViewRegisterNow = findViewById(R.id.registerNow);
        textViewError = findViewById(R.id.error);
        buttonSubmit = findViewById(R.id.submit);
        progressBar = findViewById(R.id.loading);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppName", MODE_PRIVATE);

        String[] levelOptions = {"User", "Admin"};
        Spinner spinnerLevel = findViewById(R.id.spinnerLevel);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, levelOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLevel.setAdapter(adapter);

        if (sharedPreferences.getString("logged", "false").equals("true")) {
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(intent);
            finish();
        }

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewError.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                username = String.valueOf(textInputEditTextName.getText());
                password = String.valueOf(textInputEditTextPassword.getText());
                String expectedLevel = spinnerLevel.getSelectedItem().toString(); // Ambil level dari spinner

                // Simulasi autentikasi lokal
                if (performLocalAuthentication(username, password, expectedLevel)) {
                    // Autentikasi berhasil
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("logged", "true");
                    editor.putString("user_name", username);
                    editor.putString("level", expectedLevel);
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Autentikasi gagal
                    textViewError.setText("Username, password, atau level salah");
                    textViewError.setVisibility(View.VISIBLE);
                }

                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private boolean performLocalAuthentication(String username, String password, String expectedLevel) {
        // Implementasi autentikasi lokal (gantilah dengan logika autentikasi Anda)
        // Misalnya, cocokkan dengan data pengguna yang telah disimpan secara lokal di aplikasi
        Map<String, String> userDatabase = getUserDatabase();
        String storedPassword = userDatabase.get(username);

        if (storedPassword != null && storedPassword.equals(password)) {
            // Jika password cocok, ambil level dari database
            String storedLevel = userDatabase.get(username + "_level");

            if (storedLevel != null && storedLevel.equals(expectedLevel)) {
                return true; // Autentikasi berhasil
            }
        }

        return false; // Autentikasi gagal
    }

    private Map<String, String> getUserDatabase() {
        // Simulasi database pengguna lokal (gantilah dengan penyimpanan data yang sesuai)
        Map<String, String> userDatabase = new HashMap<>();
        userDatabase.put("user1", "password1");
        userDatabase.put("user1_level", "User");
        userDatabase.put("admin1", "password1");
        userDatabase.put("admin1_level", "Admin");
        return userDatabase;
    }
}
