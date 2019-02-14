package com.beblue.gfpf.test.bebluegfpftest.user;

import android.app.Application;

import com.beblue.gfpf.test.bebluegfpftest.Injection;
import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUser;
import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUserContract;
import com.beblue.gfpf.test.bebluegfpftest.user.data.repository.GHUserRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import io.reactivex.Single;


public class GHUserViewModel extends AndroidViewModel implements GHUserContract.UserActionsListener {

    private Application mApplication;
    private GHUserRepository mGHUserRepository;

    public GHUserViewModel(Application application) {
        super(application);
        mApplication = application;
        mGHUserRepository = Injection.provideGHUserRepository(application);
    }

    @Override
    public Single<GHUser> searchGHUserByName(String searchTerm, boolean forceUpdate) {
        if (forceUpdate) {
            mGHUserRepository.refreshData();
        }

        return mGHUserRepository.searchGHUserByName(searchTerm, null);
    }

    @Override
    public Single<List<GHUser>> loadAllGHUsers() {
        return mGHUserRepository.loadAllGHUsers(null);
    }

    @Override
    public Single<GHUser> loadGHUserById(int id) {
        return mGHUserRepository.loadGHUserById(id);
    }
}