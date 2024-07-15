package com.gfpf.github_api

import android.content.Context
import com.gfpf.github_api.data.repository.RepositoryManager
import com.gfpf.github_api.data.repository.contract.IGHUserRepository
import com.gfpf.github_api.data.service.GHServiceApi
import com.gfpf.github_api.data.service.RetrofitGHServiceApiClient

object RepositoryInjection {

    fun provideGHUserRepository(context: Context): IGHUserRepository {
        val serviceApi = RetrofitGHServiceApiClient.getClient(context)
            .create(GHServiceApi::class.java)
        return RepositoryManager.getGHUserInMemoryRepository(serviceApi)
    }
}