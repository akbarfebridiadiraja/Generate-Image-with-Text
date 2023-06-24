package com.akbarf.generateimagewithtext.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.akbarf.generateimagewithtext.R;
import com.akbarf.generateimagewithtext.model.HistoryGenerateImageData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GenerateImageHistoryAdapter extends RecyclerView.Adapter<GenerateImageHistoryAdapter.ViewHolder> {

    private final List<HistoryGenerateImageData> mList;

    // Constructor
    public GenerateImageHistoryAdapter(List<HistoryGenerateImageData> list) {
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_generate_image_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryGenerateImageData data = mList.get(position);
        Picasso.get().load(data.getImageUrl()).into(holder.imageView);
        holder.textView.setText(data.getPrompt());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.text_view);
        }
    }
}
