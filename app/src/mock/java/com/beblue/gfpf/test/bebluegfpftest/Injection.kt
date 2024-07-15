package com.beblue.gfpf.test.bebluegfpftest

import android.content.Context
import com.gfpf.github_api.data.FakeServiceApiImpl
import com.gfpf.github_api.data.repository.RepositoryManager
import com.gfpf.github_api.data.repository.contract.IGHUserRepository

object Injection {

    fun provideGHUserRepository(context: Context): IGHUserRepository {
        return RepositoryManager.getGHUserInMemoryRepository(FakeServiceApiImpl())
    }
}
