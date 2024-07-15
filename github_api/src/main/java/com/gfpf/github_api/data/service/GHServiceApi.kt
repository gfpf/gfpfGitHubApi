package com.gfpf.github_api.data.service

import com.gfpf.github_api.domain.user.GHSearchUser
import com.gfpf.github_api.domain.user.GHUser
import retrofit2.http.*

interface GHServiceApi {

    // Create GHUser
    @FormUrlEncoded
    @POST("users/new")
    suspend fun createGHUser(@Field("GHUser") vararg users: GHUser)

    // Fetch all Users
    @GET("users")
    suspend fun loadAllGHUsers(): List<GHUser>

    // Fetch a user by username
    @GET("search/users")
    suspend fun searchGHUserByName(
        @Query("q") username: String,
        @Query("sort") sort: String,
        @Query("order") order: String
    ): GHSearchUser

    // Fetch a user by ID
    @GET("user/{id}")
    suspend fun loadGHUserById(@Path("id") id: Int): GHUser?

    // Update single GHUser
    @FormUrlEncoded
    @PUT("user/{id}")
    suspend fun updateGHUser(
        @Path("id") id: Int,
        @Field("GHUser") ghUser: String
    )

    // Delete GHUser
    @DELETE("user/{id}")
    suspend fun deleteGHUser(@Path("id") id: Int)
}