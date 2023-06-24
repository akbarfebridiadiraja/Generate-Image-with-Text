package com.akbarf.generateimagewithtext.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.akbarf.generateimagewithtext.Constants;
import com.akbarf.generateimagewithtext.R;
import com.akbarf.generateimagewithtext.api.RetrofitClient;
import com.akbarf.generateimagewithtext.databinding.ActivityLoginBinding;
import com.akbarf.generateimagewithtext.model.ResponseData;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Spannable customText = new SpannableString("Daftar sekarang");
        customText.setSpan(new ForegroundColorSpan(getColor(R.color.blue)), 0, customText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView text = binding.text;
        text.setText("Belum punya akun?");
        text.append(" ");
        text.append(customText);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), RegisterActivity.class));
            }
        });

        textWatcher();

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone, password;
                phone = binding.etPhone.getText().toString();
                password = binding.etPassword.getText().toString();

                boolean validated = true;
                if (TextUtils.isEmpty(phone)) {
                    validated = false;
                    binding.tilPhone.setError("No Telepon harus diisi");
                }
                if (TextUtils.isEmpty(password) || password.length() < 6) {
                    validated = false;
                    binding.tilPassword.setError("Minimal harus 6 karakter");
                }
                if (validated) {
                    binding.tilPhone.setErrorEnabled(false);
                    binding.tilPassword.setErrorEnabled(false);

                    login(phone, password);
                }
            }
        });
    }

    private void textWatcher() {
        binding.etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) binding.tilPhone.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) binding.tilPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void login(String phone, String password) {
        binding.btnLogin.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phone", phone);
            jsonObject.put("password", password);

            RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    RetrofitClient.getInstance().login(requestBody).enqueue(new Callback<ResponseData>() {
                        @Override
                        public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                Constants.setIsLoggedIn(getApplicationContext(), true);
                                Constants.setUID(getApplicationContext(), response.body().getUserData().getId());
                                Log.d("User Id", "User Id" + response.body().getUserData().getId());

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "No Hp atau password anda salah", Toast.LENGTH_SHORT).show();
                            }
                            Log.i("Login", String.valueOf(response.code()));
                            Log.i("Login", response.message());
                            Log.i("Login", response.body() != null ? response.body().getMessage() : "");

                            binding.progressBar.setVisibility(View.GONE);
                            binding.btnLogin.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<ResponseData> call, Throwable t) {
                            Log.e("Login", t.getMessage());
                            binding.progressBar.setVisibility(View.GONE);
                            binding.btnLogin.setEnabled(true);
                            Toast.makeText(LoginActivity.this, "Terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        } catch (Exception e) {
            Log.e("Login", e.getMessage(), e);
        }
    }
}