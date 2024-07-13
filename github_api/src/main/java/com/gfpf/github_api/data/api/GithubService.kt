package com.gfpf.github_api.data.api

import com.gfpf.github_api.domain.user.GHSearchUser
import com.gfpf.github_api.domain.user.GHUser
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    /*@GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): GHUser*/

    // Create GHUser
    @FormUrlEncoded
    @POST("users/new")
    suspend fun createGHUser(@Field("GHUser") users: List<GHUser>): Unit

    // Fetch all Users
    @GET("users")
    suspend fun loadAllGHUsers(): List<GHUser>

    // Search for a user by username
    @GET("search/users")
    suspend fun searchGHUserByName(
        @Query("q") username: String,
        @Query("sort") sort: String,
        @Query("order") order: String
    ): GHSearchUser

    // Fetch a user by id
    @GET("user/{id}")
    suspend fun loadGHUserById(@Path("id") id: Int): GHUser

    // Update a single GHUser
    @FormUrlEncoded
    @PUT("user/{id}")
    suspend fun updateGHUser(
        @Path("id") id: Int,
        @Field("GHUser") GHUser: String
    ): Unit

    // Delete GHUser
    @DELETE("user/{id}")
    suspend fun deleteGHUser(@Path("id") id: Int): Unit
}
