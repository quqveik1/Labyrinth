package com.kurlic.labirints.web;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TestService {
    @GET("/greeting")
    Call<String> getGreeting();
}
