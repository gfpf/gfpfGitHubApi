package com.beblue.gfpf.test.bebluegfpftest.presentation

import android.app.Application
import com.beblue.gfpf.test.bebluegfpftest.di.AppComponent
import com.beblue.gfpf.test.bebluegfpftest.di.DaggerAppComponent
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GithubApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this

        //initTimber()
        //initSplitCompat()
        initHilt()
    }

    // ============================================================================================
    //  Private init methods
    // ============================================================================================

    /**
     * Initialize Hilt dependency injection.
     */
    private fun initHilt() {
        //initCoreComponent()

        // Initialize Hilt
        appComponent = DaggerAppComponent.builder()
            //.application(this)
            //.coreComponent(coreComponent)
            .build()
        appComponent.inject(this)
    }

    companion object {

        lateinit var instance: GithubApp
            private set
    }
}