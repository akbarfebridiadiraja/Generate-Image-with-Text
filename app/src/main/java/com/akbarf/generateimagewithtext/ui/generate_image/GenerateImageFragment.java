package com.akbarf.generateimagewithtext.ui.generate_image;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.akbarf.generateimagewithtext.Constants;
import com.akbarf.generateimagewithtext.api.RetrofitClient;
import com.akbarf.generateimagewithtext.databinding.FragmentGenerateImageBinding;
import com.akbarf.generateimagewithtext.model.ResponseData;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GenerateImageFragment extends Fragment {
    private FragmentGenerateImageBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGenerateImageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prompt = binding.etPrompt.getText().toString();
                if (TextUtils.isEmpty(prompt)) {
                    binding.tilPrompt.setError("Silahkan masukkan kata kunci");
                } else {
                    binding.tilPrompt.setError(null);
                    callAPI(prompt);
                }
            }
        });

        return root;
    }

    private void callAPI(String prompt) {
        binding.btnGenerate.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);

        Request request = new Request.Builder()
                .url("https://api.pexels.com/v1/search?query=" + prompt + "&per_page=1")
                .header("Authorization", Constants.apiKeyPexcel)
                .get()
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Generate Image", e.getMessage());
                Toast.makeText(getContext(), "Failed Generate Image", Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.INVISIBLE);
                binding.btnGenerate.setEnabled(true);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i("Generate Image", response.body().toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String imageUrl = jsonObject.getJSONArray("photos").getJSONObject(0).getJSONObject("src").getString("original");

                    // Store History Generate Images
                    if (!TextUtils.isEmpty(imageUrl)){
                        storeGenerateImage(prompt, imageUrl);
                    }

                } catch (Exception e) {
                    Log.e("Generate Image", e.getMessage(), e);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Failed Generate Image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            binding.progressBar.setVisibility(View.INVISIBLE);
                            binding.btnGenerate.setEnabled(true);
                        }
                    });
                }
            }
        });
    }

    private void loadImage(String imageUrl) {
        // Your non-UI thread code
        // Update UI on the UI thread
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Picasso.get().load(imageUrl)
                        .fit()
//                        .resize(300, 300)
                        .into(binding.imageView);

                binding.progressBar.setVisibility(View.INVISIBLE);
                binding.btnGenerate.setEnabled(true);
            }
        });
    }

    private void storeGenerateImage(String prompt, String imageUrl) {
       JSONObject jsonObject = new JSONObject();
       String userId = Constants.getUID(getContext());
        try {
            jsonObject.put("prompt", prompt);
            jsonObject.put("imageUrl", imageUrl);
            jsonObject.put("userId", userId);

            RequestBody requestBody = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());

            RetrofitClient.getInstance().storeGenerateImage(requestBody).enqueue(new retrofit2.Callback<ResponseData>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                    if (response.isSuccessful()){
                        loadImage(imageUrl);
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseData> call, Throwable t) {
                    Toast.makeText(getContext(), "Terjadi kesalahan saat generate gambar", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}