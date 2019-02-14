package com.beblue.gfpf.test.bebluegfpftest;

import android.content.Context;

import com.beblue.gfpf.test.bebluegfpftest.user.data.repository.GHUserRepository;
import com.beblue.gfpf.test.bebluegfpftest.user.data.repository.Repositories;
import com.beblue.gfpf.test.bebluegfpftest.user.data.service.RetrofitServiceApiClient;
import com.beblue.gfpf.test.bebluegfpftest.user.data.service.ServiceApi;


public class Injection {

    public static GHUserRepository provideGHUserRepository(Context context) {
        ServiceApi serviceApi = RetrofitServiceApiClient.getClient(context)
                .create(ServiceApi.class);

        return Repositories.getGHUserInMemoryRepository(serviceApi);
    }

}
