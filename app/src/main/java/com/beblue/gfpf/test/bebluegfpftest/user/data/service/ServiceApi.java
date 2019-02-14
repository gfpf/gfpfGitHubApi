package com.beblue.gfpf.test.bebluegfpftest.user.data.service;

import com.beblue.gfpf.test.bebluegfpftest.user.data.domain.GHUser;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceApi {

    // Create GHUser
    @FormUrlEncoded
    @POST("users/new")
    void createGHUser(@Field("GHUser") GHUser... users);

    // Fetch all Users
    @GET("users")
    Single<List<GHUser>> loadAllGHUsers();

    // Fetch a user by username
    @GET("search/users")
    Single<GHUser> searchGHUserByName(
            @Query("q") String username
            , @Query("sort") String sort
            , @Query("order") String order);

    // Fetch all Users
    @GET("user/{id}")
    Single<GHUser> loadGHUserById(@Path("id") int id);

    // Update single GHUser
    @FormUrlEncoded
    @PUT("user/{id}")
    Completable updateGHUser(
            @Path("id") int id
            , @Field("GHUser") String GHUser);

    // Delete GHUser
    @DELETE("user/{id}")
    Completable deleteGHUser(@Path("id") int id);
}
