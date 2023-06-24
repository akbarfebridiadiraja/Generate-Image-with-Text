package com.akbarf.generateimagewithtext.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.akbarf.generateimagewithtext.Constants;
import com.akbarf.generateimagewithtext.R;
import com.akbarf.generateimagewithtext.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, Constants.isLoggedIn(getApplicationContext()) ? MainActivity.class : LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}