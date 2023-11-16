package com.kurlic.labirints.web;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LaunchService {
    @GET("/launch/new")
    Call<Integer> launch(@Query("name") String name);
}
