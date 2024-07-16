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
        return try {
            //TODO GFPF - Add Room caching strategy
            if (mCachedResults == null) {
                mCachedResults = mServiceApi.loadAllGHUsers()
            }
            mCachedResults!!
        } catch (e: HttpException) {
            if (e.code() == 404) {
                Log.e("GFPF_API_ERROR", "Resource not found: ${e.message}")
            } else {
                Log.e("GFPF_API_ERROR", "HTTP error: ${e.message}")
            }
            emptyList() // Return an empty list if an HttpException occurs
        } catch (e: Exception) {
            Log.e("GFPF_API_ERROR", "Unknown error: ${e.message}")
            emptyList() // Return an empty list if an Exception occurs
        }
    }

    override suspend fun searchUserByName(name: String): GHSearchUser? {
        return try {
            mServiceApi.searchGHUserByName(
                name,
                RetrofitGHServiceApiClient.STARS_SORT_KEY,
                RetrofitGHServiceApiClient.DESC_ORDER_KEY
            )
        } catch (e: HttpException) {
            if (e.code() == 404) {
                Log.e("GFPF_API_ERROR", "Resource not found: ${e.message}")
            } else {
                Log.e("GFPF_API_ERROR", "HTTP error: ${e.message}")
            }
            null // Return null if an HttpException occurs
        } catch (e: Exception) {
            Log.e("GFPF_API_ERROR", "Unknown error: ${e.message}")
            null // Return null if an HttpException occurs
        }


    }

    override suspend fun loadUserById(id: Int): GHUser? {
        return try {
            mServiceApi.loadGHUserById(id)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                Log.e("GFPF_API_ERROR", "Resource not found: ${e.message}")
            } else {
                Log.e("GFPF_API_ERROR", "HTTP error: ${e.message}")
            }
            null // Return null if an HttpException occurs
        } catch (e: Exception) {
            Log.e("GFPF_API_ERROR", "Unknown error: ${e.message}")
            null // Return null if an HttpException occurs
        }

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
            emptyList() // Return an empty list if an Exception occurs
        }
    }

    override suspend fun loadRepoTags(owner: String, repo: String): List<GHTag> {
        return try {
            mServiceApi.loadGHRepositoryTags(owner, repo)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                Log.e("GFPF_API_ERROR", "Resource not found: ${e.message}")
            } else {
                Log.e("GFPF_API_ERROR", "HTTP error: ${e.message}")
            }
            emptyList() // Return an empty list if an HttpException occurs
        } catch (e: Exception) {
            Log.e("GFPF_API_ERROR", "Unknown error: ${e.message}")
            emptyList() // Return an empty list if an Exception occurs
        }
    }
}