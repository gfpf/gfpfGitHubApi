package com.beblue.gfpf.test.bebluegfpftest.data.repository

import com.gfpf.github_api.data.repository.IGHUserRepository
import com.gfpf.github_api.data.service.GHServiceApi

object RepositoryManager {

    private var repository: IGHUserRepository? = null

    @Synchronized
    fun getGHUserInMemoryRepository(serviceApi: GHServiceApi): IGHUserRepository {
        if (repository == null) {
            repository = GHUserInMemoryRepository(serviceApi)
        }
        return repository!!
    }
}