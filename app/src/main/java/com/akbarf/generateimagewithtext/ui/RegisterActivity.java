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
import com.akbarf.generateimagewithtext.databinding.ActivityRegisterBinding;
import com.akbarf.generateimagewithtext.model.ResponseData;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Spannable customText = new SpannableString("Masuk sekarang");
        customText.setSpan(new ForegroundColorSpan(getColor(R.color.blue)), 0, customText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView text = binding.text;
        text.setText("Sudah punya akun?");
        text.append(" ");
        text.append(customText);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textWatcher();

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone, name, password, confirmPassword;
                phone = binding.etPhone.getText().toString();
                name = binding.etName.getText().toString();
                password = binding.etPassword.getText().toString();
                confirmPassword = binding.etConfirmPassword.getText().toString();

//                if (TextUtils.isEmpty(phone)){
//                    binding.tilPhone.isErrorEnabled();
//                    binding.tilPhone.setError("No Telepon harus diisi");
//                } else if (TextUtils.isEmpty(password) || password.length() < 8) {
//                    binding.tilPhone.setError(null);
//                    binding.tilPassword.setError("Minimal harus 8 karakter");
//                } else if (!confirmPassword.equals(password)) {
//                    binding.tilPhone.setError(null);
//                    binding.tilPassword.setError(null);
//                    binding.tilConfirmPassword.setError("Konfirmasi password tidak cocok");
//                } else {
//                    binding.tilPhone.setError(null);
//                    binding.tilPassword.setError(null);
//                    binding.tilConfirmPassword.setError(null);
//
//                    binding.btnRegister.setEnabled(false);
//                    binding.progressBar.setVisibility(View.VISIBLE);
//
//
//
//                }

                boolean validated = true;
                if (TextUtils.isEmpty(phone)) {
                    validated = false;
                    binding.tilPhone.setError("No Telepon harus diisi");
                }
                if (TextUtils.isEmpty(password) || password.length() < 6) {
                    validated = false;
                    binding.tilPassword.setError("Minimal harus 6 karakter");
                }
                if (!confirmPassword.equals(password)) {
                    validated = false;
                    binding.tilConfirmPassword.setError("Konfirmasi password tidak cocok");
                }
                if (validated) {
                    binding.tilPhone.setErrorEnabled(false);
                    binding.tilPassword.setErrorEnabled(false);
                    binding.tilConfirmPassword.setErrorEnabled(false);

                    register(phone, password, name);
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
        binding.etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence))
                    binding.tilConfirmPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void register(String phone, String password, String name) {
        binding.btnRegister.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);


        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phone", phone);
            jsonObject.put("password", password);
            jsonObject.put("name", name);

            RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    RetrofitClient.getInstance().register(requestBody).enqueue(new Callback<ResponseData>() {
                        @Override
                        public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                Constants.setIsLoggedIn(getApplicationContext(), true);
                                Constants.setUID(getApplicationContext(), response.body().getUserData().getId());

                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Username sudah terdaftar", Toast.LENGTH_SHORT).show();
                            }
                            Log.i("Register", String.valueOf(response.code()));
                            Log.i("Register", response.message());
                            Log.i("Register", response.body() != null ? response.body().getMessage() : "");

                            binding.progressBar.setVisibility(View.GONE);
                            binding.btnRegister.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<ResponseData> call, Throwable t) {
                            Log.e("Register", t.getMessage());
                            binding.progressBar.setVisibility(View.GONE);
                            binding.btnRegister.setEnabled(true);
                            Toast.makeText(RegisterActivity.this, "Terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });


        } catch (Exception e) {
            Toast.makeText(RegisterActivity.this, "Terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
            Log.e("Login", e.getMessage(), e);
        }
    }
}