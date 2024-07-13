package com.gfpf.github_api.data.repository.contract

import com.gfpf.github_api.domain.user.GHUser

interface GithubRepositoryContract {
    suspend fun getAllUsers(): List<GHUser>
    suspend fun searchUserByUsername(username: String): GHUser?
    suspend fun getUserById(id: Int): GHUser?
    suspend fun updateUser(user: GHUser)
    suspend fun deleteUser(id: Int)
}