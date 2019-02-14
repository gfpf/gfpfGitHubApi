package com.beblue.gfpf.test.bebluegfpftest.user.data.domain;

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

        Single<GHUser> searchGHUserByName(String searchTerm, boolean forceUpdate);

        Single<List<GHUser>> loadAllGHUsers();

        Single<GHUser> loadGHUserById(int id);
    }
}