package com.beblue.gfpf.test.bebluegfpftest.user.data.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GHSearchUser implements Serializable {

    public static final String REQUESTED_USER_KEY = "REQUESTED_USER_KEY";

    public GHSearchUser(Integer totalCount, boolean isIncompleteResult, String name, List<GHUser> users) {
        mTotalCount = totalCount;
        this.isIncompleteResult = isIncompleteResult;
        mUsers = users;
    }

    @SerializedName("total_count")
    private Integer mTotalCount;

    @SerializedName("incomplete_results")
    private boolean isIncompleteResult;

    @SerializedName("items")
    private List<GHUser> mUsers;


    public Integer getTotalCount() {
        return mTotalCount;
    }

    public void setTotalCount(Integer mTotalCount) {
        this.mTotalCount = mTotalCount;
    }

    public boolean isIncompleteResult() {
        return isIncompleteResult;
    }

    public void setIncompleteResult(boolean incompleteResult) {
        isIncompleteResult = incompleteResult;
    }

    public List<GHUser> getUsers() {
        return mUsers;
    }

    public void setUsers(List<GHUser> mUsers) {
        this.mUsers = mUsers;
    }
}
