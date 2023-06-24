package com.akbarf.generateimagewithtext.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.akbarf.generateimagewithtext.Constants;
import com.akbarf.generateimagewithtext.R;
import com.akbarf.generateimagewithtext.adapter.GenerateImageHistoryAdapter;
import com.akbarf.generateimagewithtext.api.RetrofitClient;
import com.akbarf.generateimagewithtext.databinding.ActivityGenerateImageHistoryBinding;
import com.akbarf.generateimagewithtext.model.HistoryGenerateImageData;
import com.akbarf.generateimagewithtext.model.ResponseData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenerateImageHistoryActivity extends AppCompatActivity {

    private ActivityGenerateImageHistoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGenerateImageHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fetchData();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        String userId = Constants.getUID(this);
        RetrofitClient.getInstance().getHistoryGenerateImages(userId).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful() && response.body().getGenerateImageDataList() != null) {
                    List<HistoryGenerateImageData> historyGenerateImageDataList = response.body().getGenerateImageDataList();
                    binding.recyclerView.setAdapter(new GenerateImageHistoryAdapter(historyGenerateImageDataList));
                }

                if (!response.body().getGenerateImageDataList().isEmpty()) {
                    binding.textView.setVisibility(View.GONE);
                } else
                    binding.textView.setVisibility(View.VISIBLE);

                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_generate_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_clear_history) {
            clearHistory();
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearHistory() {
        binding.progressBar.setVisibility(View.VISIBLE);
        String userId = Constants.getUID(this);
        RetrofitClient.getInstance().clearAllHistoryGenerateImage(userId).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    fetchData();
                    Toast.makeText(GenerateImageHistoryActivity.this, "Riwayat berhasil dihapus", Toast.LENGTH_SHORT).show();
                }
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(GenerateImageHistoryActivity.this, "Terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}