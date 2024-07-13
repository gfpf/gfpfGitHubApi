package com.beblue.gfpf.test.bebluegfpftest.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /*@Provides
    fun provideUserViewModel(application: Application): UserViewModel {
        return ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(UserViewModel::class.java)
    }*/

    /*@Provides
    @Singleton
    fun provideUserRepository(application: Application): UserRepository {
        return Injection.provideGHUserRepository(application)
    }*/
}