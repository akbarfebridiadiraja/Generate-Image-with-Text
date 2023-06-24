package com.akbarf.generateimagewithtext.model;

import com.google.gson.annotations.SerializedName;

public class HistoryGenerateImageData {
    @SerializedName("prompt") private String prompt;
    @SerializedName("imageUrl") private String imageUrl;
    @SerializedName("userId") private String userId;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
