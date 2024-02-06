package com.example.simpleapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Dashboard extends AppCompatActivity {
    private Button buttonLogout;
    private TextView textViewUsername;
    private TextView textViewDateLogin;
    private TextView textViewLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        buttonLogout = findViewById(R.id.logout);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewDateLogin = findViewById(R.id.textViewDateLogin);
        textViewLevel = findViewById(R.id.textViewLevel);

        // Ambil data dari Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppName", MODE_PRIVATE);
        String username = sharedPreferences.getString("user_name", "");
        long dateLoginInMillis = sharedPreferences.getLong("date_login", 0);
        String level = sharedPreferences.getString("level", "");

        // Tampilkan data pada UI
        textViewUsername.setText(username);
        textViewDateLogin.setText(formatDate(dateLoginInMillis));
        textViewLevel.setText("Level: " + level);

        if (sharedPreferences.getString("logged", "false").equals("false")) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hapus data sesi
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("logged", "false");
                editor.apply();

                // Kembali ke halaman login
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finishAffinity(); // Menutup semua aktivitas sebelumnya
            }
        });
    }

    private String formatDate(long dateInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMillis);
        return sdf.format(calendar.getTime());
    }
}
