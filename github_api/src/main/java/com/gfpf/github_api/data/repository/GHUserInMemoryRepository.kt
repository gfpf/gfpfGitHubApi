package com.gfpf.github_api.data.repository

import android.util.Log
import com.gfpf.github_api.data.repository.contract.IGHUserRepository
import com.gfpf.github_api.data.service.GHServiceApi
import com.gfpf.github_api.data.service.RetrofitGHServiceApiClient
import com.gfpf.github_api.domain.user.GHRepository
import com.gfpf.github_api.domain.user.GHSearchUser
import com.gfpf.github_api.domain.user.GHTag
import com.gfpf.github_api.domain.user.GHUser
import retrofit2.HttpException

class GHUserInMemoryRepository(private val mServiceApi: GHServiceApi) : IGHUserRepository {

    private var mCachedResults: List<GHUser>? = null

    override fun refreshData() {
        mCachedResults = null
    }

    override suspend fun loadAllUsers(): List<GHUser> {
        //TODO GFPF - Add Room caching strategy
        if (mCachedResults == null) {
            mCachedResults = mServiceApi.loadAllGHUsers()
        }
        return mCachedResults!!
        //return mServiceApi.loadAllGHUsers()
    }

    override suspend fun searchUserByName(name: String): GHSearchUser {
        return mServiceApi.searchGHUserByName(
            name,
            RetrofitGHServiceApiClient.STARS_SORT_KEY,
            RetrofitGHServiceApiClient.DESC_ORDER_KEY
        )
    }

    override suspend fun loadUserById(id: Int): GHUser? {
        return mServiceApi.loadGHUserById(id)
    }

    override suspend fun loadUserRepos(username: String): List<GHRepository> {
        return try {
            mServiceApi.loadGHUserRepos(username)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                Log.e("GFPF_API_ERROR", "Resource not found: ${e.message}")
            } else {
                Log.e("GFPF_API_ERROR", "HTTP error: ${e.message}")
            }
            emptyList() // Return an empty list if an HttpException occurs
        } catch (e: Exception) {
            Log.e("GFPF_API_ERROR", "Unknown error: ${e.message}")
            emptyList() // Return an empty list if an HttpException occurs
        }
    }

    override suspend fun loadRepoTags(owner: String, repo: String): List<GHTag> {
        return mServiceApi.loadGHRepositoryTags(owner, repo)
    }
}