package com.beblue.gfpf.test.bebluegfpftest

import android.content.Context
import com.beblue.gfpf.test.bebluegfpftest.data.repository.RepositoryManager
import com.gfpf.github_api.data.repository.IGHUserRepository
import com.gfpf.github_api.data.service.GHServiceApi
import com.gfpf.github_api.data.service.RetrofitGHServiceApiClient

object RepositoryInjection {

    fun provideGHUserRepository(context: Context): IGHUserRepository {
        val serviceApi = RetrofitGHServiceApiClient.getClient(context)
            .create(GHServiceApi::class.java)
        return RepositoryManager.getGHUserInMemoryRepository(serviceApi)
    }
}