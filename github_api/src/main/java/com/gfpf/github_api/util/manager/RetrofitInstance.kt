package com.gfpf.github_api.util.manager

import com.gfpf.github_api.data.api.GithubService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.github.com/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val githubService: GithubService by lazy {
        retrofit.create(GithubService::class.java)
    }
}
