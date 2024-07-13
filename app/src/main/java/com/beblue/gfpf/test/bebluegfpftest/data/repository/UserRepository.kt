package com.beblue.gfpf.test.bebluegfpftest.data.repository

import com.beblue.gfpf.test.bebluegfpftest.data.domain.GHSearchUser
import com.beblue.gfpf.test.bebluegfpftest.data.domain.GHUser

interface UserRepository {

    suspend fun searchUserByName(name: String): GHSearchUser

    suspend fun loadAllUsers(): List<GHUser>

    suspend fun loadUserById(id: Int): GHUser

    fun refreshData()
}
