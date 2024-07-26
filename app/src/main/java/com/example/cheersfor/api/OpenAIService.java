package com.example.cheersfor.api;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OpenAIService {

    String BASE_URL = "https://api.openai.com/v1/";

    @Headers({
            "Content-Type: application/json",
            "your own AI code"
    })
    @POST("chat/completions")
    Call<OpenAIResponse> getChatCompletion(@Body OpenAIChatRequest request);

    class Factory {
        public static OpenAIService create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(OpenAIService.class);
        }
    }
}
