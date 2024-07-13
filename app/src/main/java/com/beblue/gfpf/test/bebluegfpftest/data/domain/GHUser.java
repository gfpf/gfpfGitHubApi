package com.beblue.gfpf.test.bebluegfpftest.data.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GHUser implements Serializable {

    public static final String REQUESTED_USER_KEY = "REQUESTED_USER_KEY";

    public GHUser(Integer id, String login, String name, String avatarUrl, String ghUrl) {
        mId = id;
        mLogin = login;
        mName = name;
        mAvatarUrl = avatarUrl;
        mGHUrl = ghUrl;
    }

    @SerializedName("id")
    private Integer mId;

    @SerializedName("login")
    private String mLogin;

    @SerializedName("name")
    private String mName;

    @SerializedName("avatar_url")
    private String mAvatarUrl;

    @SerializedName("html_url")
    private String mGHUrl;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        mLogin = login;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        mAvatarUrl = avatarUrl;
    }

    public String getGHUrl() {
        return mGHUrl;
    }

    public void setGHUrl(String ghUrl) {
        mGHUrl = ghUrl;
    }
}
