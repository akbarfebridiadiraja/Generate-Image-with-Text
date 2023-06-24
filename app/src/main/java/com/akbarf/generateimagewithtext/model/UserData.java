package com.akbarf.generateimagewithtext.model;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("name") String name;
    @SerializedName("phone") String phone;
    @SerializedName("id") String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
