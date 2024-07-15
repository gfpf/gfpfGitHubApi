package com.gfpf.github_api.di.contract

import android.app.Application
import com.gfpf.github_api.data.repository.contract.IGHUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import com.gfpf.github_api.RepositoryInjection

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideUserRepository(application: Application): IGHUserRepository {
        return RepositoryInjection.provideGHUserRepository(application)
    }
}
