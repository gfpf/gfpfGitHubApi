package com.beblue.gfpf.test.bebluegfpftest;

import android.content.Context;

import com.beblue.gfpf.test.bebluegfpftest.data.FakeServiceApiImpl;
import com.beblue.gfpf.test.bebluegfpftest.user.data.repository.GHUserRepository;
import com.beblue.gfpf.test.bebluegfpftest.user.data.repository.Repositories;


public class Injection {

    public static GHUserRepository provideGHUserRepository(Context context) {
        return Repositories.getGHUserInMemoryRepository(new FakeServiceApiImpl());
    }

}
