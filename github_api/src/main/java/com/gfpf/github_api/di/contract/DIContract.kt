package com.gfpf.github_api.di.contract

import dagger.Module

interface DependencyInjectionContract {
    fun getModules(): List<Module>
}