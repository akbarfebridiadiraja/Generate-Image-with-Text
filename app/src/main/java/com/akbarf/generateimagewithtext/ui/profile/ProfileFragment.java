package com.akbarf.generateimagewithtext.ui.profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.akbarf.generateimagewithtext.Constants;
import com.akbarf.generateimagewithtext.R;
import com.akbarf.generateimagewithtext.api.RetrofitClient;
import com.akbarf.generateimagewithtext.databinding.FragmentProfileBinding;
import com.akbarf.generateimagewithtext.model.ResponseData;
import com.akbarf.generateimagewithtext.model.UserData;
import com.akbarf.generateimagewithtext.ui.GenerateImageHistoryActivity;
import com.akbarf.generateimagewithtext.ui.LoginActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // GET USER
        getUser();

        binding.llRiwayatGenerateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(view.getContext(), GenerateImageHistoryActivity.class));
            }
        });


        binding.llUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogUpdatePassword();
            }
        });


        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false).setTitle("Keluar").setMessage("Apakah anda yakin ingin mengakhiri sesi kali ini?").setPositiveButton("Keluar", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                    Constants.clearShadPref(getContext());
                }).setNegativeButton("Batal", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        return root;
    }

    private void getUser() {
        String userId = Constants.getUID(getContext());
        Log.d("User ID", "User Id: " + userId);
        RetrofitClient.getInstance().getUser(userId).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful() && response.body().getUserData() != null) {
                    UserData userData = response.body().getUserData();
                    binding.tvName.setText(userData.getName());
                    binding.tvPhone.setText(userData.getPhone());
                }
                Log.d("getUser", response.message());
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.d("getUser", t.getMessage());
            }
        });
    }

    @SuppressLint("MissingInflatedId")
    private void showDialogUpdatePassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dialog_update_password, null);
        builder.setView(view);


        ImageView imageViewClose = view.findViewById(R.id.image_view_close);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                TextInputLayout tilOldPassword, tilNewPassword, tilConfirmPassword;
                TextInputEditText etOldPassword, etNewPassword, etConfirmPassword;
                Button btnUpdatePassword;
                ProgressBar progressBar;
                tilOldPassword = view.findViewById(R.id.til_old_password);
                tilNewPassword = view.findViewById(R.id.til_new_password);
                tilConfirmPassword = view.findViewById(R.id.til_confirm_password);
                etOldPassword = view.findViewById(R.id.et_old_password);
                etNewPassword = view.findViewById(R.id.et_new_password);
                etConfirmPassword = view.findViewById(R.id.et_confirm_password);
                btnUpdatePassword = view.findViewById(R.id.btn_update_password);
                progressBar = view.findViewById(R.id.progress_bar);

                textWatcher(tilOldPassword, etOldPassword);
                textWatcher(tilNewPassword, etNewPassword);
                textWatcher(tilConfirmPassword, etConfirmPassword);

                btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userId = Constants.getUID(view.getContext());
                        String oldPassword = etOldPassword.getText().toString();
                        String newPassword = etNewPassword.getText().toString();
                        String confirmNewPassword = etConfirmPassword.getText().toString();

                        boolean validated = true;
                        if (TextUtils.isEmpty(oldPassword) || oldPassword.length() < 6) {
                            validated = false;
                            tilOldPassword.setError("Minimal 6 karakter");
                        }
                        if (TextUtils.isEmpty(newPassword) || newPassword.length() < 6) {
                            validated = false;
                            tilNewPassword.setError("Minimal 6 karakter");
                        }
                        if (!TextUtils.isEmpty(newPassword) && newPassword.equals(oldPassword)) {
                            validated = false;
                            tilNewPassword.setError("Password baru tidak boleh sama dengan password lama!");
                        }
                        if (!confirmNewPassword.equals(newPassword)){
                            validated = false;
                            tilConfirmPassword.setError("Pastikan password sudah benar");
                        }
                        if (validated) {
                            updatePassword(userId, oldPassword, newPassword, btnUpdatePassword, progressBar, dialogInterface, tilOldPassword);
                        }
                    }
                });
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void textWatcher(TextInputLayout textInputLayout, TextInputEditText textInputEditText) {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) textInputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    private void updatePassword(String userId, String oldPassword, String newPassword, Button btnUpdatePassword, ProgressBar progressBar, DialogInterface dialogInterface, TextInputLayout tilOldPassword) {
        btnUpdatePassword.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("oldPassword", oldPassword);
            jsonObject.put("newPassword", newPassword);

            RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    RetrofitClient.getInstance().updatePassword(userId, requestBody).enqueue(new Callback<ResponseData>() {
                        @Override
                        public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Update Password Berhasil", Toast.LENGTH_SHORT);
                                dialogInterface.dismiss();
                            }
                            if (response.code() == 401){
                                tilOldPassword.setError("Password Lama anda tidak valid");
                            }


                            btnUpdatePassword.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            Log.d("Update Password", response.message());
                        }

                        @Override
                        public void onFailure(Call<ResponseData> call, Throwable t) {
                            btnUpdatePassword.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            Log.d("Update Password", t.getMessage());
                        }
                    });

                }
            });


        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}