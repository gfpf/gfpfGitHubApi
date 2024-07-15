package com.gfpf.github_api.data.repository

import com.gfpf.github_api.domain.user.GHSearchUser
import com.gfpf.github_api.domain.user.GHUser

interface IGHUserRepository {

    suspend fun searchUserByName(name: String): GHSearchUser

    suspend fun loadAllUsers(): List<GHUser>

    suspend fun loadUserById(id: Int): GHUser

    fun refreshData()
}
