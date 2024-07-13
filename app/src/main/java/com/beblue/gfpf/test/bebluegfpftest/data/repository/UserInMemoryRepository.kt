package com.beblue.gfpf.test.bebluegfpftest.data.repository

import com.beblue.gfpf.test.bebluegfpftest.data.domain.GHSearchUser
import com.beblue.gfpf.test.bebluegfpftest.data.domain.GHUser
import com.beblue.gfpf.test.bebluegfpftest.data.service.RetrofitServiceApiClient
import com.beblue.gfpf.test.bebluegfpftest.data.service.ServiceApi
import kotlinx.coroutines.rx2.await

class UserInMemoryRepository(private val mServiceApi: ServiceApi) : UserRepository {

    private var mCachedResults: List<GHUser>? = null

    override fun refreshData() {
        mCachedResults = null
    }

    // Given the mismatch error, it seems like mServiceApi.loadAllGHUsers() returns a Single<List<GHUser>>
    // instead of List<GHUser>.
    // Therefore, you need to handle the conversion from Single to a List using a coroutine-friendly approach.

    // You can use the await extension function from the kotlinx-coroutines-rx2 library to
    // await the result of an RxJava Single in a coroutine.
    // This allows you to keep the suspend functions in your repository interface while properly handling the RxJava types.
    //TODO GFPF - Remove all RxJava Code

    override suspend fun loadAllUsers(): List<GHUser> {
        //TODO GFPF - Remove all RxJava Code
        return mServiceApi.loadAllGHUsers().await()
    }

    override suspend fun searchUserByName(name: String): GHSearchUser {
        return mServiceApi.searchGHUserByName(name, RetrofitServiceApiClient.STARS_SORT_KEY, RetrofitServiceApiClient.DESC_ORDER_KEY).await()
    }

    override suspend fun loadUserById(id: Int): GHUser {
        return mServiceApi.loadGHUserById(id).await()
    }
}