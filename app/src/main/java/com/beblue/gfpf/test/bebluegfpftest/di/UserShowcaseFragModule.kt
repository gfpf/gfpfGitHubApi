package com.beblue.gfpf.test.bebluegfpftest.di

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.beblue.gfpf.test.bebluegfpftest.RepositoryInjection
import com.gfpf.github_api.data.repository.IGHUserRepository
import com.beblue.gfpf.test.bebluegfpftest.databinding.UserShowcaseFragBinding
import com.beblue.gfpf.test.bebluegfpftest.presentation.user.adapter.UserRecyclerViewAdapter
import com.beblue.gfpf.test.bebluegfpftest.presentation.user.view.UserShowcaseFrag
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(FragmentComponent::class)
object UserShowcaseFragModule {

    @Provides
    fun provideUserShowcaseFragBinding(
        fragment: UserShowcaseFrag
    ): UserShowcaseFragBinding {
        return UserShowcaseFragBinding.inflate(fragment.layoutInflater)
    }
}

@Module
@InstallIn(FragmentComponent::class)
object LinearLayoutManagerModule {

    @Provides
    fun provideContext(fragment: Fragment): Context {
        return fragment.requireContext()
    }

    @Provides
    fun provideLinearLayoutManager(context: Context): LinearLayoutManager {
        return LinearLayoutManager(context)
    }
}

@Module
@InstallIn(FragmentComponent::class)
object UserRecyclerViewAdapterModule {

    @Provides
    fun provideUserRecyclerViewClickListener(fragment: Fragment): UserRecyclerViewAdapter.UserRecyclerViewClickListener {
        return fragment as UserRecyclerViewAdapter.UserRecyclerViewClickListener
    }

    @Provides
    fun provideUserRecyclerViewAdapter(
        context: Context,
        listener: UserRecyclerViewAdapter.UserRecyclerViewClickListener
    ): UserRecyclerViewAdapter {
        return UserRecyclerViewAdapter(context, listener)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideUserRepository(application: Application): IGHUserRepository {
        return RepositoryInjection.provideGHUserRepository(application)
    }
}
