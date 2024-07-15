package com.beblue.gfpf.test.bebluegfpftest.data.repository

import com.gfpf.github_api.data.repository.IGHUserRepository
import com.gfpf.github_api.data.service.GHServiceApi
import com.gfpf.github_api.data.service.RetrofitGHServiceApiClient
import com.gfpf.github_api.domain.user.GHSearchUser
import com.gfpf.github_api.domain.user.GHUser

class GHUserInMemoryRepository(private val mServiceApi: GHServiceApi) : IGHUserRepository {

    private var mCachedResults: List<GHUser>? = null

    override fun refreshData() {
        mCachedResults = null
    }

    override suspend fun loadAllUsers(): List<GHUser> {
        return mServiceApi.loadAllGHUsers()
    }

    override suspend fun searchUserByName(name: String): GHSearchUser {
        return mServiceApi.searchGHUserByName(
            name,
            RetrofitGHServiceApiClient.STARS_SORT_KEY,
            RetrofitGHServiceApiClient.DESC_ORDER_KEY
        )
    }

    override suspend fun loadUserById(id: Int): GHUser {
        return mServiceApi.loadGHUserById(id)
    }
}