package com.akbarf.generateimagewithtext.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseData {
    @SerializedName("message") private String message;
    @SerializedName("user") private UserData userData;
    @SerializedName("historyGenerateImages") private List<HistoryGenerateImageData> historyGenerateImageDataList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public List<HistoryGenerateImageData> getGenerateImageDataList() {
        return historyGenerateImageDataList;
    }

    public void setGenerateImageDataList(List<HistoryGenerateImageData> historyGenerateImageDataList) {
        this.historyGenerateImageDataList = historyGenerateImageDataList;
    }
}
