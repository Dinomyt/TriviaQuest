package com.example.triviaquest.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenTdbService {
    @GET("api.php")
    Call<TriviaApiResponse> getQuestions(
            @Query("amount")     int amount,
            @Query("category")   int category,
            @Query("difficulty") String difficulty,
            @Query("type")       String type
    );
}