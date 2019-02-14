package com.beblue.gfpf.test.bebluegfpftest.user.data.repository;

import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUser;
import com.beblue.gfpf.test.bebluegfpftest.user.data.service.RetrofitServiceApiClient;
import com.beblue.gfpf.test.bebluegfpftest.user.data.service.ServiceApi;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class GHUserInMemoryRepository implements GHUserRepository {

    private final ServiceApi mServiceApi;

    /**
     * This method has reduced visibility for testing and is only visible to tests in the same package.
     */
    @VisibleForTesting
    private List<GHUser> mCachedResults;

    public GHUserInMemoryRepository(@NonNull ServiceApi serviceApi) {
        mServiceApi = checkNotNull(serviceApi);
    }

    @Override
    public Single<GHUser> searchGHUserByName(String name, DisposableSingleObserver<GHUser> callback) {
        checkNotNull(name);

        //Search user by name
        return mServiceApi.searchGHUserByName(name, RetrofitServiceApiClient.STARS_SORT_KEY, RetrofitServiceApiClient.DESC_ORDER_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        //.subscribeWith(callback);
    }

    @Override
    public Single<List<GHUser>> loadAllGHUsers(DisposableSingleObserver<List<GHUser>> callback) {

        return mServiceApi.loadAllGHUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        //.subscribeWith(callback);
    }

    @Override
    public Single<GHUser> loadGHUserById(@NonNull int id) {
        //Load user details by id
        return mServiceApi.loadGHUserById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        //.subscribeWith(callback);

    }


    @Override
    public void refreshData() {

    }
}
