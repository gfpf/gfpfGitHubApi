package com.beblue.gfpf.test.bebluegfpftest.data.domain;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Single;

public interface GHUserContract {

    interface View {

        void setProgressIndicator(boolean active);

        void showToastMessage(String message);

        void showGHUserListUI(List<GHUser> users, boolean isAppend);

        void showGHUserDetailUI(@NonNull GHUser requestedUser);
    }

    interface UserActionsListener {

        Single<GHSearchUser> searchUserByName(String searchTerm, boolean forceUpdate);

        Single<List<GHUser>> loadAllUsers();

        Single<GHUser> loadUserById(int id);
    }
}