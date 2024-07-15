package com.beblue.gfpf.test.bebluegfpftest.di

import com.beblue.gfpf.test.bebluegfpftest.presentation.GithubApp
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    /**
     * Inject dependencies on application.
     *
     * @param application The sample application.
     */
    fun inject(application: GithubApp)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }
}
