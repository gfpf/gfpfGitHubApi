package com.beblue.gfpf.test.bebluegfpftest;

import android.content.Context;

import com.beblue.gfpf.test.bebluegfpftest.data.repository.Repositories;
import com.beblue.gfpf.test.bebluegfpftest.data.repository.UserRepository;
import com.beblue.gfpf.test.bebluegfpftest.data.service.RetrofitServiceApiClient;
import com.beblue.gfpf.test.bebluegfpftest.data.service.ServiceApi;


public class Injection {

    public static UserRepository provideGHUserRepository(Context context) {
        ServiceApi serviceApi = RetrofitServiceApiClient.getClient(context)
                .create(ServiceApi.class);

        return Repositories.getGHUserInMemoryRepository(serviceApi);
    }

}
