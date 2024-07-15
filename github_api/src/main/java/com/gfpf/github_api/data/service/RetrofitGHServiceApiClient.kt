package com.gfpf.github_api.data.service

import android.content.Context
import com.itkacher.okprofiler.BuildConfig
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitGHServiceApiClient {
    private const val BASE_URL = "https://api.github.com/"
    private const val TOKEN: String = "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"

    const val STARS_SORT_KEY = "stars"
    const val DESC_ORDER_KEY = "desc"
    const val USERS_URL_PATH_KEY = "users/"
    private const val REQUEST_TIMEOUT = 10

    private lateinit var retrofit: Retrofit
    private lateinit var okHttpClient: OkHttpClient

    fun getClient(context: Context): Retrofit {
        if (!::okHttpClient.isInitialized) {
            initOkHttp(context)
        }

        if (!::retrofit.isInitialized) {
            // Create Moshi instance with KotlinJsonAdapterFactory
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }
        return retrofit
    }

    private fun initOkHttp(context: Context) {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)

        //TODO GFPF - Make OkHttp Profiler log view works (alt+8)
        if (BuildConfig.DEBUG)
            httpClient.addInterceptor(OkHttpProfilerInterceptor())

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpClient.addInterceptor(interceptor)

        httpClient.addInterceptor(Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Authorization", "Bearer $TOKEN")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
            val request = requestBuilder.build()
            chain.proceed(request)
        })
        okHttpClient = httpClient.build()
    }
}