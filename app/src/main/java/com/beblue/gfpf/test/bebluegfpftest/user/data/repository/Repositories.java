package com.beblue.gfpf.test.bebluegfpftest.user.data.repository;

import com.beblue.gfpf.test.bebluegfpftest.user.data.service.ServiceApi;

import androidx.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class Repositories {

    private Repositories() {
        // no instance
    }

    private static GHUserRepository repository = null;

    public synchronized static GHUserRepository getGHUserInMemoryRepository(@NonNull ServiceApi serviceApi) {
        checkNotNull(serviceApi);

        if (repository == null) {
            repository = new GHUserInMemoryRepository(serviceApi);
        }
        return repository;
    }

}
