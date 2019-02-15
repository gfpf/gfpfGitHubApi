package com.beblue.gfpf.test.bebluegfpftest.user.data.repository;

import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHSearchUser;
import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUser;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;

public interface GHUserRepository {

    Single<GHSearchUser> searchGHUserByName(@NonNull String name, DisposableSingleObserver<GHUser> callback);

    Single<List<GHUser>> loadAllGHUsers(@NonNull DisposableSingleObserver<List<GHUser>> callback);

    Single<GHUser> loadGHUserById(@NonNull int id);

    void refreshData();
}
