package com.beblue.gfpf.test.bebluegfpftest.di

import com.beblue.gfpf.test.bebluegfpftest.presentation.user.view.UserViewModel
import com.beblue.gfpf.test.bebluegfpftest.presentation.user.view.contract.IUserDetailFrag
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped


//TODO GFPF - Move to api module
@Module
@InstallIn(FragmentComponent::class)
object UserDetailModule {

    @Provides
    @FragmentScoped // Adjust scope as needed
    fun provideUserDetailActionListener(viewModel: UserViewModel): IUserDetailFrag.ActionListener {
        return viewModel // Assuming UserViewModel implements ActionListener
    }
}
