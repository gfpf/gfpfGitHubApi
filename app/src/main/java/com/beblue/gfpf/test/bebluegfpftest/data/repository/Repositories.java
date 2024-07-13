package com.beblue.gfpf.test.bebluegfpftest.data.repository;

import com.beblue.gfpf.test.bebluegfpftest.data.service.ServiceApi;

import androidx.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class Repositories {

    private Repositories() {
        // no instance
    }

    private static UserRepository repository = null;

    public synchronized static UserRepository getGHUserInMemoryRepository(@NonNull ServiceApi serviceApi) {
        checkNotNull(serviceApi);

        if (repository == null) {
            repository = new UserInMemoryRepository(serviceApi);
        }
        return repository;
    }

}
