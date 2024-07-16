package com.gfpf.github_api.data.repository.contract

import com.gfpf.github_api.domain.user.GHRepository
import com.gfpf.github_api.domain.user.GHSearchUser
import com.gfpf.github_api.domain.user.GHTag
import com.gfpf.github_api.domain.user.GHUser

interface IGHUserRepository {
    suspend fun searchUserByName(name: String): GHSearchUser
    suspend fun loadAllUsers(): List<GHUser>
    suspend fun loadUserById(id: Int): GHUser?
    suspend fun loadUserRepos(username: String): List<GHRepository>
    suspend fun loadRepoTags(owner: String, repo: String): List<GHTag>
    fun refreshData()
}